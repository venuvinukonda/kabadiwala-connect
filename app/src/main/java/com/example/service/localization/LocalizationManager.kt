package com.example.service.localization

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LocalizationManager {

    private const val PREFS_NAME = "kabadiwala_localization_prefs"
    private const val KEY_SELECTED_LANGUAGE = "selected_language_code"

    private var appContext: Context? = null
    private var prefs: SharedPreferences? = null

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun initialize(context: Context) {
        appContext = context.applicationContext
        prefs = appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedCode = prefs?.getString(KEY_SELECTED_LANGUAGE, AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        _currentLanguage.value = AppLanguage.fromCode(savedCode)
    }

    fun setLanguage(lang: AppLanguage, context: Context? = null) {
        _currentLanguage.value = lang
        val p = prefs ?: context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) ?: appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        p?.edit()?.putString(KEY_SELECTED_LANGUAGE, lang.code)?.apply()
    }

    /**
     * Resolves a localized string by key with fallback to English, and safely formats arguments if provided.
     */
    fun getString(key: String, vararg args: Any): String {
        return getString(key, _currentLanguage.value, *args)
    }

    fun getString(key: String, lang: AppLanguage = _currentLanguage.value, vararg args: Any): String {
        val raw = Translations.stringMap[key]?.get(lang)
            ?: TranslationsExtended.stringMap[key]?.get(lang)
            ?: TranslationsForms.stringMap[key]?.get(lang)
            ?: Translations.stringMap[key]?.get(AppLanguage.ENGLISH)
            ?: TranslationsExtended.stringMap[key]?.get(AppLanguage.ENGLISH)
            ?: TranslationsForms.stringMap[key]?.get(AppLanguage.ENGLISH)
            ?: key

        return if (args.isNotEmpty()) {
            try {
                String.format(raw, *args)
            } catch (e: Exception) {
                Log.w("LocalizationManager", "Formatting error for key: $key", e)
                raw
            }
        } else {
            raw
        }
    }

    /**
     * Localized dynamic welcome greeting matching cultural conventions.
     * e.g., Telugu: "వెంకటేష్ గారికి స్వాగతం" as requested in user specification.
     */
    fun getWelcomeGreeting(name: String, lang: AppLanguage = _currentLanguage.value): String {
        val cleanName = name.ifBlank { "Mitra" }
        return when (lang) {
            AppLanguage.TELUGU -> "${cleanName} గారికి స్వాగతం"
            AppLanguage.HINDI -> "नमस्ते, ${cleanName}! आपका स्वागत है"
            AppLanguage.TAMIL -> "வணக்கம், ${cleanName}! வரவேற்கிறோம்"
            AppLanguage.KANNADA -> "ನಮಸ್ಕಾರ, ${cleanName}! ಸ್ವಾಗತ"
            AppLanguage.MALAYALAM -> "നമസ്കാരം, ${cleanName}! സ്വാഗതം"
            AppLanguage.MARATHI -> "नमस्कार, ${cleanName}! आपले स्वागत आहे"
            AppLanguage.BENGALI -> "নমস্কার, ${cleanName}! আপনাকে স্বাগতম"
            AppLanguage.GUJARATI -> "નમસ્તે, ${cleanName}! આપનું સ્વાગત છે"
            AppLanguage.PUNJABI -> "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ, ${cleanName}! ਜੀ ਆਇਆਂ ਨੂੰ"
            AppLanguage.ODIA -> "ନମସ୍କାର, ${cleanName}! ଆପଣଙ୍କୁ ସ୍ୱାଗତ"
            AppLanguage.ENGLISH -> "Welcome, ${cleanName}!"
        }
    }

    /**
     * Localized voice narration for the collector dashboard
     */
    fun getCollectorWelcomeVoice(name: String, lang: AppLanguage = _currentLanguage.value): String {
        val cleanName = name.ifBlank { "Mitra" }
        return when (lang) {
            AppLanguage.TELUGU -> "${cleanName} గారికి స్వాగతం! కొత్త స్క్రాప్‌ను జోడించడానికి ఆకుపచ్చని ఇ-వ్యర్థాల సేకరణ బటన్‌ను నొక్కండి లేదా మీ పికప్‌లు, సంపాదనలను తనిఖీ చేయండి."
            AppLanguage.HINDI -> "${cleanName}, स्वागत है! नया ई-कचरा जोड़ने के लिए हरे बटन को दबाएं, या अपने पिकअप, कमाई और सुरक्षा निर्देश देखें।"
            AppLanguage.TAMIL -> "வரவேற்கிறோம் ${cleanName}! புதிய கழிவுகளை சேர்க்க பச்சை நிற மின்-கழிவு சேகரிப்பு பொத்தானை அழுத்தவும், பிக்-அப் மற்றும் வருவாயைச் சரிபார்க்கவும்."
            AppLanguage.KANNADA -> "ಸ್ವಾಗತ ${cleanName}! ಹೊಸ ತ್ಯಾಜ್ಯ ಸೇರಿಸಲು ಹಸಿರು ಗುಂಡಿಯನ್ನು ಒತ್ತಿ, ಅಥವಾ ಪಿಕಪ್ ಮತ್ತು ಗಳಿಕೆಯನ್ನು ಪರಿಶೀಲಿಸಿ."
            AppLanguage.MALAYALAM -> "സ്വാഗതം ${cleanName}! പുതിയ ഇ-വേസ്റ്റ് ചേർക്കാൻ പച്ച ബട്ടൺ അമർത്തുക, അല്ലെങ്കിൽ പിക്കപ്പുകളും വരുമാനവും പരിശോധിക്കുക."
            AppLanguage.MARATHI -> "स्वागत आहे ${cleanName}! नवीन ई-कचरा जोडण्यासाठी हिरवे बटण दाबा, किंवा तुमचे पिकअप आणि कमाई तपासा."
            AppLanguage.BENGALI -> "স্বাগতম ${cleanName}! নতুন ই-বর্জ্য যোগ করতে সবুজ বোতাম টিপুন, বা আপনার পিকআপ এবং উপার্জন দেখুন।"
            AppLanguage.GUJARATI -> "સ્વાગત છે ${cleanName}! નવો ઈ-કચરો ઉમેરવા માટે લીલું બટન દબાવો, અથવા તમારા પિકઅપ અને કમાણી તપાસો."
            AppLanguage.PUNJABI -> "ਜੀ ਆਇਆਂ ਨੂੰ ${cleanName}! ਨਵਾਂ ਈ-ਕੂੜਾ ਜੋੜਨ ਲਈ ਹਰਾ ਬਟਨ ਦਬਾਓ, ਜਾਂ ਆਪਣੇ ਪਿਕਅੱਪ ਅਤੇ ਕਮਾਈ ਦੀ ਜਾਂਚ ਕਰੋ।"
            AppLanguage.ODIA -> "ସ୍ୱାଗତ ${cleanName}! ନୂତନ ଇ-ବର୍ଜ୍ୟ ଯୋଡିବା ପାଇଁ ସବୁଜ ବଟନ୍ ଦବାନ୍ତୁ, କିମ୍ବା ଆପଣଙ୍କ ପିକଅପ୍ ଓ ଆୟ ଯାଞ୍ଚ କରନ୍ତୁ।"
            AppLanguage.ENGLISH -> "Welcome $cleanName! Tap the green Collect E-Waste button to add new scrap, or check your pickups, earnings, and safety instructions."
        }
    }

    /**
     * Localized status labels for material lots, requests, and transactions.
     */
    fun getStatusLabel(status: String, lang: AppLanguage = _currentLanguage.value): String {
        return when (status.uppercase()) {
            "APPROVED" -> getString("status_approved", lang)
            "PENDING", "PENDING_VERIFICATION" -> getString("status_pending", lang)
            "COMPLETED" -> getString("status_completed", lang)
            "PAID" -> getString("status_paid", lang)
            "REQUESTED" -> when (lang) {
                AppLanguage.TELUGU -> "అభ్యర్థించబడింది"
                AppLanguage.HINDI -> "अनुरोधित"
                AppLanguage.TAMIL -> "கோரப்பட்டது"
                AppLanguage.KANNADA -> "ವಿನಂತಿಸಲಾಗಿದೆ"
                AppLanguage.MALAYALAM -> "അഭ്യർത്ഥിച്ചു"
                AppLanguage.MARATHI -> "विनंती केली"
                AppLanguage.BENGALI -> "অনুরোধ করা হয়েছে"
                AppLanguage.GUJARATI -> "વિનંતી કરેલ"
                AppLanguage.PUNJABI -> "ਬੇਨਤੀ ਕੀਤੀ"
                AppLanguage.ODIA -> "ଅନୁରୋଧିତ"
                AppLanguage.ENGLISH -> "Requested"
            }
            "ACCEPTED", "SCHEDULED" -> when (lang) {
                AppLanguage.TELUGU -> "స్వీకరించబడింది"
                AppLanguage.HINDI -> "स्वीकृत / निर्धारित"
                AppLanguage.TAMIL -> "ஏற்றுக்கொள்ளப்பட்டது"
                AppLanguage.KANNADA -> "ಸ್ವೀಕರಿಸಲಾಗಿದೆ"
                AppLanguage.MALAYALAM -> "സ്വീകരിച്ചു"
                AppLanguage.MARATHI -> "स्वीकारले"
                AppLanguage.BENGALI -> "গৃহীত"
                AppLanguage.GUJARATI -> "સ્વીકારાયેલ"
                AppLanguage.PUNJABI -> "ਮਨਜ਼ੂਰ"
                AppLanguage.ODIA -> "ଗୃହୀତ"
                AppLanguage.ENGLISH -> "Accepted"
            }
            "REJECTED" -> when (lang) {
                AppLanguage.TELUGU -> "తిరస్కరించబడింది"
                AppLanguage.HINDI -> "अस्वीकृत"
                AppLanguage.TAMIL -> "நிராகரிக்கப்பட்டது"
                AppLanguage.KANNADA -> "ತಿರಸ್ಕರಿಸಲಾಗಿದೆ"
                AppLanguage.MALAYALAM -> "നിരസിച്ചു"
                AppLanguage.MARATHI -> "नाकारले"
                AppLanguage.BENGALI -> "প্রত্যাখ্যাত"
                AppLanguage.GUJARATI -> "અસ્વીકાર્ય"
                AppLanguage.PUNJABI -> "ਰੱਦ"
                AppLanguage.ODIA -> "ପ୍ରତ୍ୟାଖ୍ୟାତ"
                AppLanguage.ENGLISH -> "Rejected"
            }
            else -> status.replace("_", " ")
        }
    }
}
