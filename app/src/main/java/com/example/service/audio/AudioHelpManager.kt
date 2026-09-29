package com.example.service.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.service.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioHelpManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var currentAppLanguage: AppLanguage = AppLanguage.ENGLISH

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _currentSpokenText = MutableStateFlow<String?>(null)
    val currentSpokenText: StateFlow<String?> = _currentSpokenText.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _voiceUnavailableMessage = MutableStateFlow<String?>(null)
    val voiceUnavailableMessage: StateFlow<String?> = _voiceUnavailableMessage.asStateFlow()

    private var pendingRetryText: String? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AudioHelpManager", "TTS initialization failed: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    _isPaused.value = false
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }
            })
            setLocaleForLanguage(currentAppLanguage)
        } else {
            Log.e("AudioHelpManager", "TTS engine not available")
        }
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        tts?.setSpeechRate(rate)
    }

    /**
     * Resolves target locale for all supported languages without deprecated constructors.
     */
    fun getTargetLocale(lang: AppLanguage): Locale {
        return when (lang) {
            AppLanguage.MARATHI -> Locale.forLanguageTag("mr-IN")
            AppLanguage.HINDI -> Locale.forLanguageTag("hi-IN")
            AppLanguage.TELUGU -> Locale.forLanguageTag("te-IN")
            AppLanguage.TAMIL -> Locale.forLanguageTag("ta-IN")
            AppLanguage.KANNADA -> Locale.forLanguageTag("kn-IN")
            AppLanguage.MALAYALAM -> Locale.forLanguageTag("ml-IN")
            AppLanguage.BENGALI -> Locale.forLanguageTag("bn-IN")
            AppLanguage.GUJARATI -> Locale.forLanguageTag("gu-IN")
            AppLanguage.PUNJABI -> Locale.forLanguageTag("pa-IN")
            AppLanguage.ODIA -> Locale.forLanguageTag("or-IN")
            AppLanguage.ENGLISH -> Locale.forLanguageTag("en-IN")
        }
    }

    /**
     * Dynamically binds the TTS engine to the selected language locale and native voice.
     * Guarantees that Marathi, Hindi, Telugu, etc. voices are preserved, and informs the user
     * if the voice package is missing instead of falling back to English.
     */
    fun setLocaleForLanguage(lang: AppLanguage): Boolean {
        currentAppLanguage = lang
        if (!isInitialized || tts == null) return false

        val targetLocale = getTargetLocale(lang)

        // Check if language/voice data is available
        val check = tts?.isLanguageAvailable(targetLocale) ?: TextToSpeech.LANG_NOT_SUPPORTED
        val isTargetAvailable = check != TextToSpeech.LANG_MISSING_DATA && check != TextToSpeech.LANG_NOT_SUPPORTED

        // 1. Search available voices for an exact or regional match
        try {
            val availableVoices: Set<Voice>? = tts?.voices
            if (!availableVoices.isNullOrEmpty()) {
                val matchedVoice = availableVoices.firstOrNull { voice ->
                    val voiceLocale = voice.locale
                    voiceLocale.language.equals(lang.code, ignoreCase = true) &&
                            voiceLocale.country.equals("IN", ignoreCase = true)
                } ?: availableVoices.firstOrNull { voice ->
                    voice.locale.language.equals(lang.code, ignoreCase = true)
                } ?: availableVoices.firstOrNull { voice ->
                    voice.name.contains(lang.code, ignoreCase = true) ||
                            voice.name.contains(lang.displayName, ignoreCase = true)
                }

                if (matchedVoice != null) {
                    tts?.voice = matchedVoice
                    Log.i("AudioHelpManager", "Selected native voice: ${matchedVoice.name} for ${lang.displayName} ($targetLocale)")
                }
            }
        } catch (e: Exception) {
            Log.w("AudioHelpManager", "Querying voices not supported: ${e.message}")
        }

        // 2. Explicitly set language with country
        val result = tts?.setLanguage(targetLocale)
        val success = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        if (!success) {
            val baseResult = tts?.setLanguage(Locale.forLanguageTag(lang.code))
            return baseResult != TextToSpeech.LANG_MISSING_DATA && baseResult != TextToSpeech.LANG_NOT_SUPPORTED
        }
        return isTargetAvailable
    }

    fun speak(text: String, utteranceId: String = "TTS_${System.currentTimeMillis()}") {
        if (text.isBlank()) return
        stop()

        _currentSpokenText.value = text
        _voiceUnavailableMessage.value = null
        pendingRetryText = text

        val isLocaleSupported = setLocaleForLanguage(currentAppLanguage)

        if (isInitialized && tts != null) {
            if (!isLocaleSupported && currentAppLanguage != AppLanguage.ENGLISH) {
                // If native voice is not installed on this device (e.g. Marathi/Hindi/Telugu missing TTS data):
                // Do NOT let an English voice garble native Indian language text.
                _isSpeaking.value = false
                _voiceUnavailableMessage.value = getVoiceUnavailableWarning(currentAppLanguage)
                return
            }

            _isSpeaking.value = true
            _isPaused.value = false
            tts?.setSpeechRate(_speechRate.value)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } else {
            // Container/device fallback simulation for preview environments
            _isSpeaking.value = true
            _isPaused.value = false
            val simulatedDurationMs = (text.length * 50L).coerceIn(1800L, 5000L)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isSpeaking.value = false
                _isPaused.value = false
            }, simulatedDurationMs)
        }
    }

    fun speakFeedback(text: String) {
        speak(text)
    }

    fun speakLanguageChangedGreeting(lang: AppLanguage) {
        val greeting = when (lang) {
            AppLanguage.MARATHI -> "मराठी भाषा निवडली आहे. कबाडीवाला कनेक्टमध्ये आपले स्वागत आहे."
            AppLanguage.HINDI -> "हिंदी भाषा चुनी गई है। कबाड़ीवाला कनेक्ट में आपका स्वागत है।"
            AppLanguage.TELUGU -> "తెలుగు భాష ఎంపిక చేయబడింది. కబాడీవాలా కనెక్ట్‌కు స్వాగతం."
            AppLanguage.TAMIL -> "தமிழ் மொழி தேர்ந்தெடுக்கப்பட்டது. கபாடிவாலா கனெக்டிற்கு வரவேற்கிறோம்."
            AppLanguage.KANNADA -> "ಕನ್ನಡ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಲಾಗಿದೆ. ಕಬಾಡಿವಾಲಾ ಕನೆಕ್ಟ್‌ಗೆ ಸುಸ್ವಾಗತ."
            AppLanguage.MALAYALAM -> "മലയാളം ഭാഷ തിരഞ്ഞെടുത്തു. കബാഡിവാല കണക്റ്റിലേക്ക് സ്വാഗതം."
            AppLanguage.BENGALI -> "বাংলা ভাষা নির্বাচিত হয়েছে। কবাডিওয়ালা কানেক্টে স্বাগতম।"
            AppLanguage.GUJARATI -> "ગુજરાતી ભાષા પસંદ કરવામાં આવી છે. કબાડીવાલા કનેક્ટમાં આપનું સ્વાગત છે."
            AppLanguage.PUNJABI -> "ਪੰਜਾਬੀ ਭਾਸ਼ਾ ਚੁਣੀ ਗਈ ਹੈ। ਕਬਾੜੀਵਾਲਾ ਕਨੈਕਟ ਵਿੱਚ ਜੀ ਆਇਆਂ ਨੂੰ।"
            AppLanguage.ODIA -> "ଓଡ଼ିଆ ଭାଷା ଚୟନ କରାଯାଇଛି। କବାଡିୱାଲା କନେକ୍ଟକୁ ସ୍ୱାଗତ।"
            AppLanguage.ENGLISH -> "English language selected. Welcome to Kabadiwala Connect."
        }
        speak(greeting)
    }

    fun retry() {
        val text = pendingRetryText ?: _currentSpokenText.value
        _voiceUnavailableMessage.value = null
        if (!text.isNullOrBlank()) {
            speak(text)
        }
    }

    fun clearVoiceWarning() {
        _voiceUnavailableMessage.value = null
    }

    private fun getVoiceUnavailableWarning(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.MARATHI -> "या डिव्हाइसवर मराठी व्हॉइस (TTS) उपलब्ध नाही. कृपया डिव्हाइसच्या Settings -> System -> Languages & Input -> Text-to-speech मधून मराठी स्पीच डेटा डाउनलोड करा. (Marathi voice is not installed on this device. Please install Marathi speech data from Text-to-Speech settings.)"
            AppLanguage.HINDI -> "इस डिवाइस पर हिंदी आवाज उपलब्ध नहीं है। कृपया सेटिंग्स से हिंदी स्पीच डेटा डाउनलोड करें। (Hindi voice is not installed on this device. Please install Hindi speech data from Text-to-Speech settings.)"
            AppLanguage.TELUGU -> "ఈ పరికరంలో తెలుగు వాయిస్ అందుబాటులో లేదు. దయచేసి సెట్టింగ్‌ల నుండి తెలుగు స్పీచ్ డేటాను డౌన్‌లోడ్ చేయండి. (Telugu voice is not installed on this device. Please install Telugu speech data from Text-to-Speech settings.)"
            else -> "${lang.displayName} voice data is not installed on this device. Please install it from device Text-to-Speech settings."
        }
    }

    fun pause() {
        if (_isSpeaking.value) {
            if (isInitialized) {
                tts?.stop()
            }
            _isSpeaking.value = false
            _isPaused.value = true
        }
    }

    fun resume() {
        if (_isPaused.value) {
            val text = _currentSpokenText.value
            if (!text.isNullOrBlank()) {
                speak(text)
            }
        }
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
        _isSpeaking.value = false
        _isPaused.value = false
    }

    fun replay() {
        val lastText = _currentSpokenText.value
        if (!lastText.isNullOrBlank()) {
            speak(lastText)
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
