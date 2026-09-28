package com.example.service.localization

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val localeTag: String
) {
    ENGLISH("en", "English", "English", "en-IN"),
    TELUGU("te", "Telugu", "తెలుగు", "te-IN"),
    HINDI("hi", "Hindi", "हिन्दी", "hi-IN"),
    TAMIL("ta", "Tamil", "தமிழ்", "ta-IN"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ", "kn-IN"),
    MALAYALAM("ml", "Malayalam", "മലയാളം", "ml-IN"),
    MARATHI("mr", "Marathi", "मराठी", "mr-IN"),
    BENGALI("bn", "Bengali", "বাংলা", "bn-IN"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી", "gu-IN"),
    PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ", "pa-IN"),
    ODIA("or", "Odia", "ଓଡ଼ିଆ", "or-IN");

    fun getLocale(): Locale {
        return when (this) {
            ENGLISH -> Locale("en", "IN")
            TELUGU -> Locale("te", "IN")
            HINDI -> Locale("hi", "IN")
            TAMIL -> Locale("ta", "IN")
            KANNADA -> Locale("kn", "IN")
            MALAYALAM -> Locale("ml", "IN")
            MARATHI -> Locale("mr", "IN")
            BENGALI -> Locale("bn", "IN")
            GUJARATI -> Locale("gu", "IN")
            PUNJABI -> Locale("pa", "IN")
            ODIA -> Locale("or", "IN")
        }
    }

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
