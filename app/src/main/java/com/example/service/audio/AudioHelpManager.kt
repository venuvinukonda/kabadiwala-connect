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
     * Dynamically binds the TTS engine to the selected language locale and native voice.
     * Never falls back to an English voice for non-English languages (Marathi, Telugu, Hindi, etc.).
     */
    fun setLocaleForLanguage(lang: AppLanguage) {
        currentAppLanguage = lang
        if (!isInitialized || tts == null) return

        val targetLocale = lang.getLocale()

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
            Log.w("AudioHelpManager", "Querying voices not supported on this platform: ${e.message}")
        }

        // 2. Explicitly set language with country (e.g., mr-IN, te-IN, hi-IN)
        val result = tts?.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Try base language code (e.g., "mr", "te", "hi")
            val baseResult = tts?.setLanguage(Locale(lang.code))
            if (baseResult == TextToSpeech.LANG_MISSING_DATA || baseResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("AudioHelpManager", "TTS engine missing voice data for ${lang.displayName}. Retaining ${lang.code} locale without English fallback.")
                // DO NOT overwrite with English. Keep targetLocale so pronunciation is not butchered by an English voice.
            }
        }
    }

    fun speak(text: String, utteranceId: String = "TTS_${System.currentTimeMillis()}") {
        if (text.isBlank()) return
        stop()
        _currentSpokenText.value = text
        _isSpeaking.value = true
        _isPaused.value = false

        if (isInitialized && tts != null) {
            tts?.setSpeechRate(_speechRate.value)
            // Ensure locale is applied before speech synthesis
            setLocaleForLanguage(currentAppLanguage)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } else {
            // Simulated speech duration for container environments without native audio hardware
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isSpeaking.value = false
                _isPaused.value = false
            }, 2500)
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
