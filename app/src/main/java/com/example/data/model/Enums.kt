package com.example.data.model

import com.example.service.localization.AppLanguage
import com.example.service.localization.LocalizationManager

enum class UserRole {
    COLLECTOR,
    RECYCLER,
    ADMIN
}

enum class UserStatus {
    PENDING,
    APPROVED,
    REJECTED,
    SUSPENDED
}

enum class RecyclerCertStatus {
    PENDING_VERIFICATION,
    APPROVED,
    REJECTED,
    EXPIRED,
    SUSPENDED
}

enum class LotStatus {
    DRAFT,
    CLASSIFIED,
    MATCHED,
    PICKUP_REQUESTED,
    COMPLETED
}

enum class PickupStatus {
    REQUESTED,
    ACCEPTED,
    PICKUP_SCHEDULED,
    PICKED_UP,
    HANDOVER_CONFIRMED,
    COMPLETED,
    REJECTED
}

enum class PaymentStatus {
    PENDING,
    RECORDED,
    PAID,
    FAILED,
    DISPUTED
}

enum class MaterialCondition(val displayName: String, val audioDescription: String, val priceMultiplier: Double) {
    WORKING("Working", "Device turns on and is fully functional", 1.4),
    PARTIALLY_WORKING("Partially Working", "Some components functional or minor screen or port issues", 1.1),
    DAMAGED("Damaged", "Physically cracked, broken chassis or damaged casing", 0.9),
    NON_WORKING("Non-Working", "Does not turn on or motherboard dead", 0.75),
    SCRAP("Scrap", "Broken into pieces, shredded or bare internal boards", 0.6),
    UNKNOWN("Unknown", "Condition not yet inspected or unverified", 0.7);

    fun getLocalizedName(lang: AppLanguage = LocalizationManager.currentLanguage.value): String = when (this) {
        WORKING -> when (lang) {
            AppLanguage.TELUGU -> "పనిచేస్తోంది (వర్కింగ్)"
            AppLanguage.HINDI -> "चालू हालत (वर्किंग)"
            AppLanguage.TAMIL -> "வேலை செய்கிறது"
            AppLanguage.KANNADA -> "ಕೆಲಸ ಮಾಡುತ್ತಿದೆ"
            AppLanguage.MALAYALAM -> "പ്രവർത്തിക്കുന്നു"
            AppLanguage.MARATHI -> "चालू स्थितीत (वर्किंग)"
            AppLanguage.BENGALI -> "চালু রয়েছে"
            AppLanguage.GUJARATI -> "ચાલુ હાલત"
            AppLanguage.PUNJABI -> "ਚਾਲੂ ਹਾਲਤ"
            AppLanguage.ODIA -> "କାର୍ଯ୍ୟକ୍ଷମ"
            AppLanguage.ENGLISH -> "Working"
        }
        PARTIALLY_WORKING -> when (lang) {
            AppLanguage.TELUGU -> "పాక్షికంగా పనిచేస్తోంది"
            AppLanguage.HINDI -> "आंशिक रूप से चालू"
            AppLanguage.TAMIL -> "பகுதியளவு வேலை செய்கிறது"
            AppLanguage.KANNADA -> "ಭಾಗಶಃ ಕೆಲಸ ಮಾಡುತ್ತಿದೆ"
            AppLanguage.MALAYALAM -> "ഭാഗികമായി പ്രവർത്തിക്കുന്നു"
            AppLanguage.MARATHI -> "अंशतः चालू"
            AppLanguage.BENGALI -> "আংশিক কার্যকর"
            AppLanguage.GUJARATI -> "અંશતઃ ચાલુ"
            AppLanguage.PUNJABI -> "ਅੰਸ਼ਕ ਤੌਰ 'ਤੇ ਚਾਲੂ"
            AppLanguage.ODIA -> "ଆଂଶିକ କାର୍ଯ୍ୟକ୍ଷମ"
            AppLanguage.ENGLISH -> "Partially Working"
        }
        DAMAGED -> when (lang) {
            AppLanguage.TELUGU -> "దెబ్బతిన్నది (పాడైంది)"
            AppLanguage.HINDI -> "क्षतिग्रस्त (डैमेज)"
            AppLanguage.TAMIL -> "சேதமடைந்தது"
            AppLanguage.KANNADA -> "ಹಾನಿಗೊಳಗಾಗಿದೆ"
            AppLanguage.MALAYALAM -> "കേടുപാടുകൾ സംഭവിച്ചു"
            AppLanguage.MARATHI -> "खराब / तुटलेले"
            AppLanguage.BENGALI -> "ক্ষতিগ্রস্ত"
            AppLanguage.GUJARATI -> "ક્ષતિગ્રસ્ત"
            AppLanguage.PUNJABI -> "ਨੁਕਸਾਨਿਆ ਗਿਆ"
            AppLanguage.ODIA -> "କ୍ଷତିଗ୍ରସ୍ତ"
            AppLanguage.ENGLISH -> "Damaged"
        }
        NON_WORKING -> when (lang) {
            AppLanguage.TELUGU -> "పనిచేయడం లేదు (డెడ్)"
            AppLanguage.HINDI -> "बंद / काम नहीं कर रहा"
            AppLanguage.TAMIL -> "வேலை செய்யவில்லை"
            AppLanguage.KANNADA -> "ಕೆಲಸ ಮಾಡುತ್ತಿಲ್ಲ"
            AppLanguage.MALAYALAM -> "പ്രവർത്തിക്കുന്നില്ല"
            AppLanguage.MARATHI -> "बंद / काम करत नाही"
            AppLanguage.BENGALI -> "অকেজো / কাজ করছে না"
            AppLanguage.GUJARATI -> "બંધ હાલત"
            AppLanguage.PUNJABI -> "ਬੰਦ / ਕੰਮ ਨਹੀਂ ਕਰਦਾ"
            AppLanguage.ODIA -> "ଅଚଳ"
            AppLanguage.ENGLISH -> "Non-Working"
        }
        SCRAP -> when (lang) {
            AppLanguage.TELUGU -> "పూర్తి స్క్రాప్ / విడిభాగాలు"
            AppLanguage.HINDI -> "कबाड़ / स्क्रैप"
            AppLanguage.TAMIL -> "பழைய இரும்பு / ஸ்கிராப்"
            AppLanguage.KANNADA -> "ಸ್ಕ್ರ್ಯಾಪ್ / ಮುರಿದ ಭಾಗಗಳು"
            AppLanguage.MALAYALAM -> "സ്ക്രാപ്പ്"
            AppLanguage.MARATHI -> "पूर्ण भंगार / स्क्रॅप"
            AppLanguage.BENGALI -> "স্ক্র্যাপ / ভাঙারি"
            AppLanguage.GUJARATI -> "ભંગાર / સ્ક્રેપ"
            AppLanguage.PUNJABI -> "ਕਬਾੜ / ਸਕ੍ਰੈਪ"
            AppLanguage.ODIA -> "ଭଙ୍ଗାରୁଜା / ସ୍କ୍ରାପ୍"
            AppLanguage.ENGLISH -> "Scrap"
        }
        UNKNOWN -> when (lang) {
            AppLanguage.TELUGU -> "తెలియదు / తనిఖీ చేయలేదు"
            AppLanguage.HINDI -> "अज्ञात / अनपेक्षित"
            AppLanguage.TAMIL -> "தெரியவில்லை"
            AppLanguage.KANNADA -> "ತಿಳಿದಿಲ್ಲ"
            AppLanguage.MALAYALAM -> "അജ്ഞാതം"
            AppLanguage.MARATHI -> "अज्ञात / तपासले नाही"
            AppLanguage.BENGALI -> "অজানা"
            AppLanguage.GUJARATI -> "અજાણ્યું"
            AppLanguage.PUNJABI -> "ਅਣਜਾਣ"
            AppLanguage.ODIA -> "ଅଜ୍ଞାତ"
            AppLanguage.ENGLISH -> "Unknown"
        }
    }

    fun getLocalizedAudio(lang: AppLanguage = LocalizationManager.currentLanguage.value): String = when (this) {
        WORKING -> when (lang) {
            AppLanguage.TELUGU -> "పరికరం ఆన్ అవుతుంది మరియు పూర్తి స్థాయిలో పనిచేస్తుంది"
            AppLanguage.HINDI -> "उपकरण चालू होता है और पूरी तरह से ठीक काम करता है"
            AppLanguage.TAMIL -> "சாதனம் ஆன் ஆகிறது மற்றும் முழுமையாக வேலை செய்கிறது"
            AppLanguage.KANNADA -> "ಸಾಧನ ಆನ್ ಆಗುತ್ತದೆ ಮತ್ತು ಸಂಪೂರ್ಣವಾಗಿ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತದೆ"
            AppLanguage.MALAYALAM -> "ഉപകരണം ഓണാകുന്നു, പൂർണ്ണമായും പ്രവർത്തിക്കുന്നു"
            AppLanguage.MARATHI -> "डिव्हाइस चालू होते आणि पूर्णपणे व्यवस्थित काम करते"
            AppLanguage.BENGALI -> "ডিভাইসটি চালু হয় এবং সম্পূর্ণ কার্যকরী"
            AppLanguage.GUJARATI -> "ડિવાઇસ ચાલુ થાય છે અને સંપૂર્ણપણે કાર્યરત છે"
            AppLanguage.PUNJABI -> "ਡਿਵਾਈਸ ਚਾਲੂ ਹੁੰਦਾ ਹੈ ਅਤੇ ਪੂਰੀ ਤਰ੍ਹਾਂ ਕੰਮ ਕਰਦਾ ਹੈ"
            AppLanguage.ODIA -> "ଡିଭାଇସ୍ ଚାଲୁ ହୁଏ ଏବଂ ସମ୍ପୂର୍ଣ୍ଣ କାର୍ଯ୍ୟକ୍ଷମ ଅଟେ"
            AppLanguage.ENGLISH -> audioDescription
        }
        PARTIALLY_WORKING -> when (lang) {
            AppLanguage.TELUGU -> "కొన్ని భాగాలు పనిచేస్తాయి లేదా చిన్న సమస్యలు ఉన్నాయి"
            AppLanguage.HINDI -> "कुछ हिस्से काम कर रहे हैं या स्क्रीन या पोर्ट में छोटी खराबी है"
            AppLanguage.TAMIL -> "சில பகுதிகள் வேலை செய்கின்றன அல்லது சிறிய குறைபாடுகள் உள்ளன"
            AppLanguage.KANNADA -> "ಕೆಲವು ಭಾಗಗಳು ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತವೆ ಅಥವಾ ಸಣ್ಣ ದೋಷಗಳಿವೆ"
            AppLanguage.MALAYALAM -> "ചില ഭാഗങ്ങൾ പ്രവർത്തിക്കുന്നു അല്ലെങ്കിൽ ചെറിയ പ്രശ്നങ്ങളുണ്ട്"
            AppLanguage.MARATHI -> "काही घटक काम करत आहेत किंवा किरकोळ समस्या आहे"
            AppLanguage.BENGALI -> "কিছু অংশ কাজ করছে বা ছোটখাটো ত্রুটি রয়েছে"
            AppLanguage.GUJARATI -> "કેટલાક ભાગો કામ કરે છે અથવા નાની ખામી છે"
            AppLanguage.PUNJABI -> "ਕੁਝ ਹਿੱਸੇ ਕੰਮ ਕਰ ਰਹੇ ਹਨ ਜਾਂ ਛੋਟੀਆਂ ਖਾਮੀਆਂ ਹਨ"
            AppLanguage.ODIA -> "କିଛି ଅଂଶ କାର୍ଯ୍ୟ କରୁଛି କିମ୍ବା ଛୋଟ ତ୍ରୁଟି ଅଛି"
            AppLanguage.ENGLISH -> audioDescription
        }
        DAMAGED -> when (lang) {
            AppLanguage.TELUGU -> "బాహ్యంగా పగిలినది లేదా కేసింగ్ దెబ్బతిన్నది"
            AppLanguage.HINDI -> "शारीरिक रूप से टूटा हुआ या स्क्रीन क्षतिग्रस्त"
            AppLanguage.TAMIL -> "உடைந்த அல்லது சேதமடைந்த உறை"
            AppLanguage.KANNADA -> "ಮುರಿದ ಅಥವಾ ಹಾನಿಗೊಳಗಾದ ಚಾಸಿಸ್"
            AppLanguage.MALAYALAM -> "തകർന്നതോ കേടുപാടുകൾ സംഭവിച്ചതോ ആയ ബോഡി"
            AppLanguage.MARATHI -> "शारीरिकदृष्ट्या तुटलेले किंवा कव्हर फुटलेले"
            AppLanguage.BENGALI -> "শারীরিকভাবে ভাঙা বা কেসিং ক্ষতিগ্রস্ত"
            AppLanguage.GUJARATI -> "તૂટેલું અથવા નુકસાન પામેલું બોડી"
            AppLanguage.PUNJABI -> "ਟੁੱਟਿਆ ਹੋਇਆ ਜਾਂ ਖ਼ਰਾਬ ਬਾਡੀ"
            AppLanguage.ODIA -> "ଭାଙ୍ଗିଯାଇଥିବା କିମ୍ବା ଆଘାତପ୍ରାପ୍ତ"
            AppLanguage.ENGLISH -> audioDescription
        }
        NON_WORKING -> when (lang) {
            AppLanguage.TELUGU -> "ఆన్ అవ్వడం లేదు లేదా మదర్‌బోర్డ్ పాడైపోయింది"
            AppLanguage.HINDI -> "चालू नहीं होता या मदरबोर्ड खराब हो चुका है"
            AppLanguage.TAMIL -> "ஆன் ஆகவில்லை அல்லது மதர்போர்டு பழுதடைந்துள்ளது"
            AppLanguage.KANNADA -> "ಆನ್ ಆಗುತ್ತಿಲ್ಲ ಅಥವಾ ಮದರ್‌ಬೋರ್ಡ್ ಕೆಟ್ಟಿದೆ"
            AppLanguage.MALAYALAM -> "ഓണാകുന്നില്ല അല്ലെങ്കിൽ മദർബോർഡ് തകരാറിലാണ്"
            AppLanguage.MARATHI -> "चालू होत नाही किंवा मदरबोर्ड खराब झाला आहे"
            AppLanguage.BENGALI -> "চালু হয় না বা মাদারবোর্ড নষ্ট"
            AppLanguage.GUJARATI -> "ચાલુ થતું નથી અથવા મધરબોર્ડ ખરાબ છે"
            AppLanguage.PUNJABI -> "ਚਾਲੂ ਨਹੀਂ ਹੁੰਦਾ ਜਾਂ ਮਦਰਬੋਰਡ ਖਰਾਬ ਹੈ"
            AppLanguage.ODIA -> "ଚାଲୁ ହେଉନାହିଁ କିମ୍ବା ମଦରବୋର୍ଡ ନଷ୍ଟ"
            AppLanguage.ENGLISH -> audioDescription
        }
        SCRAP -> when (lang) {
            AppLanguage.TELUGU -> "ముక్కలుగా విరిగిపోయింది లేదా పూర్తిగా విడిభాగాలు"
            AppLanguage.HINDI -> "टुकड़ों में टूटा हुआ, कटा हुआ या सिर्फ पुर्जे"
            AppLanguage.TAMIL -> "துண்டுகளாக உடைந்தது அல்லது உதிரி பாகங்கள்"
            AppLanguage.KANNADA -> "ತುಂಡುಗಳಾಗಿ ಮುರಿದಿದೆ ಅಥವಾ ಬಿಡಿಭಾಗಗಳು"
            AppLanguage.MALAYALAM -> "കഷണങ്ങളായി തകർന്നു അല്ലെങ്കിൽ ഭാഗങ്ങൾ മാത്രം"
            AppLanguage.MARATHI -> "तुकडे झालेले किंवा सुटे भाग"
            AppLanguage.BENGALI -> "টুকরো টুকরো বা খালি বোর্ড"
            AppLanguage.GUJARATI -> "ટુકડાઓમાં તૂટેલું અથવા સ્પેર પાર્ટ્સ"
            AppLanguage.PUNJABI -> "ਟੁਕੜਿਆਂ ਵਿੱਚ ਟੁੱਟਿਆ ਜਾਂ ਸਿਰਫ ਪੁਰਜ਼ੇ"
            AppLanguage.ODIA -> "ଖଣ୍ଡ ଖଣ୍ଡ ଭଙ୍ଗା କିମ୍ବା ଅଲଗା ଅଂଶ"
            AppLanguage.ENGLISH -> audioDescription
        }
        UNKNOWN -> when (lang) {
            AppLanguage.TELUGU -> "పరిస్థితి ఇంకా తనిఖీ చేయబడలేదు"
            AppLanguage.HINDI -> "स्थिति का अभी निरीक्षण नहीं किया गया है"
            AppLanguage.TAMIL -> "நிலை இன்னும் சரிபார்க்கப்படவில்லை"
            AppLanguage.KANNADA -> "ಸ್ಥಿತಿಯನ್ನು ಇನ್ನೂ ಪರಿಶೀಲಿಸಲಾಗಿಲ್ಲ"
            AppLanguage.MALAYALAM -> "അവസ്ഥ പരിശോധിച്ചിട്ടില്ല"
            AppLanguage.MARATHI -> "स्थितीची अद्याप तपासणी झालेली नाही"
            AppLanguage.BENGALI -> "অবস্থা এখনও পরীক্ষা করা হয়নি"
            AppLanguage.GUJARATI -> "સ્થિતિ હજી તપાસી નથી"
            AppLanguage.PUNJABI -> "ਸਥਿਤੀ ਅਜੇ ਜਾਂਚੀ ਨਹੀਂ ਗਈ"
            AppLanguage.ODIA -> "ସ୍ଥିତି ଏପର୍ଯ୍ୟନ୍ତ ଯାଞ୍ଚ ହୋଇନାହିଁ"
            AppLanguage.ENGLISH -> audioDescription
        }
    }
}
