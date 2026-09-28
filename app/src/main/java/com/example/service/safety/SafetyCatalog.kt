package com.example.service.safety

import com.example.service.localization.AppLanguage
import com.example.service.localization.LocalizationManager

data class SafetyItem(
    val id: String,
    val title: String,
    val description: String,
    val audioScript: String,
    val isCrucial: Boolean = false,
    val category: String = "Hazard Prevention",
    val titleTranslations: Map<AppLanguage, String> = emptyMap(),
    val descTranslations: Map<AppLanguage, String> = emptyMap(),
    val categoryTranslations: Map<AppLanguage, String> = emptyMap(),
    val audioTranslations: Map<AppLanguage, String> = emptyMap()
) {
    fun getTitle(lang: AppLanguage = LocalizationManager.currentLanguage.value): String {
        return titleTranslations[lang] ?: titleTranslations[AppLanguage.ENGLISH] ?: title
    }

    fun getDescription(lang: AppLanguage = LocalizationManager.currentLanguage.value): String {
        return descTranslations[lang] ?: descTranslations[AppLanguage.ENGLISH] ?: description
    }

    fun getCategory(lang: AppLanguage = LocalizationManager.currentLanguage.value): String {
        return categoryTranslations[lang] ?: categoryTranslations[AppLanguage.ENGLISH] ?: category
    }

    fun getAudioScript(lang: AppLanguage = LocalizationManager.currentLanguage.value): String {
        return audioTranslations[lang] ?: audioTranslations[AppLanguage.ENGLISH] ?: audioScript
    }
}

object SafetyCatalog {
    val items = listOf(
        SafetyItem(
            id = "SAFE_01",
            title = "Do Not Touch Leaking Batteries",
            description = "Battery chemicals like corrosive acids and lithium salts cause severe skin chemical burns. Always use thick rubber or nitrile gloves and store in plastic buckets.",
            audioScript = "Warning! Do not touch leaking batteries directly with bare hands. Battery acid causes severe chemical burns. Always wear rubber gloves and store in a dry bucket.",
            isCrucial = true,
            category = "Battery Safety",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Do Not Touch Leaking Batteries",
                AppLanguage.TELUGU to "కారుతున్న బ్యాటరీలను తాకవద్దు",
                AppLanguage.HINDI to "लीक हो रही बैटरियों को न छुएं",
                AppLanguage.TAMIL to "கசியும் பேட்டரிகளைத் தொடாதீர்கள்",
                AppLanguage.KANNADA to "ಸೋರುವ ಬ್ಯಾಟರಿಗಳನ್ನು ಮುಟ್ಟಬೇಡಿ",
                AppLanguage.MALAYALAM to "ചോരുന്ന ബാറ്ററികൾ തൊടരുത്",
                AppLanguage.MARATHI to "गळणाऱ्या बॅटऱ्यांना हात लावू नका",
                AppLanguage.BENGALI to "লিক হওয়া ব্যাটারি স্পর্শ করবেন না",
                AppLanguage.GUJARATI to "લીક થતી બેટરીઓને સ્પર્શ કરશો નહીં",
                AppLanguage.PUNJABI to "ਲੀਕ ਹੋ ਰਹੀਆਂ ਬੈਟਰੀਆਂ ਨੂੰ ਨਾ ਛੂਹੋ",
                AppLanguage.ODIA to "ଲିକ୍ ହେଉଥିବା ବ୍ୟାଟେରୀକୁ ଛୁଅନ୍ତୁ ନାହିଁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Battery chemicals like corrosive acids and lithium salts cause severe skin chemical burns. Always use thick rubber or nitrile gloves and store in plastic buckets.",
                AppLanguage.TELUGU to "యాసిడ్లు మరియు లిథియం లవణాలు తీవ్రమైన రసాయన కాలిన గాయాలను కలిగిస్తాయి. ఎల్లప్పుడూ మందపాటి రబ్బరు చేతి తొడుగులు వాడండి.",
                AppLanguage.HINDI to "बैटरी रसायन जैसे संक्षारक एसिड और लिथियम लवण गंभीर रासायनिक जलन पैदा करते हैं। हमेशा मोटे रबर के दस्ताने का प्रयोग करें।",
                AppLanguage.TAMIL to "அரிக்கும் அமிலங்கள் போன்ற இரசாயனங்கள் கடுமையான தீக்காயங்களை ஏற்படுத்தும். தடிமனான கையுறைகளைப் பயன்படுத்தவும்.",
                AppLanguage.KANNADA to "ಆಮ್ಲಗಳು ತೀವ್ರವಾದ ರಾಸಾಯನಿಕ ಸುಡುವಿಕೆಯನ್ನು ಉಂಟುಮಾಡುತ್ತವೆ. ಯಾವಾಗಲೂ ದಪ್ಪ ರಬ್ಬರ್ ಕೈಗವಸುಗಳನ್ನು ಬಳಸಿ.",
                AppLanguage.MALAYALAM to "ആസിഡുകൾ ഗുരുതരമായ പൊള്ളലിന് കാരണമാകും. എല്ലായ്പ്പോഴും കട്ടിയുള്ള റബ്ബർ കയ്യുറകൾ ഉപയോഗിക്കുക.",
                AppLanguage.MARATHI to "बॅटरीतील ॲसिड आणि रसायनांमुळे त्वचा भाजते. नेहमी जाड रबरचे हातमोजे वापरा आणि प्लास्टिकच्या बादलीत ठेवा.",
                AppLanguage.BENGALI to "ব্যাটারির রাসায়নিক পদার্থ ত্বকে মারাত্মক ক্ষত সৃষ্টি করে। সর্বদা মোটা রাবারের গ্লাভস ব্যবহার করুন।",
                AppLanguage.GUJARATI to "એસિડ ગંભીર દાઝવા તરફ દોરી જાય છે. હંમેશા જાડા રબરના મોજા વાપરો.",
                AppLanguage.PUNJABI to "ਬੈਟਰੀ ਐਸਿਡ ਚਮੜੀ ਨੂੰ ਸਾੜ ਸਕਦਾ ਹੈ। ਹਮੇਸ਼ਾ ਮੋਟੇ ਰਬੜ ਦੇ ਦਸਤਾਨੇ ਪਾਓ।",
                AppLanguage.ODIA to "ବ୍ୟାଟେରୀ ଏସିଡ୍ ଚର୍ମକୁ ପୋଡିପାରେ। ସର୍ବଦା ମୋଟା ରବର ଗ୍ଲୋଭସ୍ ବ୍ୟବହାର କରନ୍ତୁ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Battery Hazard",
                AppLanguage.TELUGU to "బ్యాటరీ ప్రమాదం",
                AppLanguage.HINDI to "बैटरी खतरा",
                AppLanguage.TAMIL to "பேட்டரி ஆபத்து",
                AppLanguage.KANNADA to "ಬ್ಯಾಟರಿ ಅಪಾಯ",
                AppLanguage.MALAYALAM to "ബാറ്ററി അപകടം",
                AppLanguage.MARATHI to "बॅटरी धोका",
                AppLanguage.BENGALI to "ব্যাটারি ঝুঁকি",
                AppLanguage.GUJARATI to "બેટરી જોખમ",
                AppLanguage.PUNJABI to "ਬੈਟਰੀ ਖਤਰਾ",
                AppLanguage.ODIA to "ବ୍ୟାଟେରୀ ବିପଦ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Warning! Do not touch leaking batteries directly with bare hands. Battery acid causes severe chemical burns. Always wear rubber gloves.",
                AppLanguage.TELUGU to "హెచ్చరిక! కారుతున్న బ్యాటరీలను నేరుగా చేతులతో తాకవద్దు. యాసిడ్ తీవ్రమైన గాయాలకు కారణమవుతుంది. ఎల్లప్పుడూ రబ్బరు చేతి తొడుగులు ధరించండి.",
                AppLanguage.HINDI to "चेतावनी! लीक हो रही बैटरियों को नंगे हाथों से न छुएं। बैटरी का एसिड गंभीर जलन पैदा करता है। हमेशा रबर के दस्ताने पहनें।",
                AppLanguage.TAMIL to "எச்சரிக்கை! கசியும் பேட்டரிகளை வெறுங்கைகளால் தொடாதீர்கள். ரப்பர் கையுறைகளை அணியுங்கள்.",
                AppLanguage.KANNADA to "ಎಚ್ಚರಿಕೆ! ಸೋರುತ್ತಿರುವ ಬ್ಯಾಟರಿಗಳನ್ನು ಬರಿಗೈಯಿಂದ ಮುಟ್ಟಬೇಡಿ. ರಬ್ಬರ್ ಗ್ಲೌಸ್ ಧರಿಸಿ.",
                AppLanguage.MALAYALAM to "മുന്നറിയിപ്പ്! ചോരുന്ന ബാറ്ററികൾ നഗ്നമായ കൈകളാൽ തൊടരുത്. റബ്ബർ കയ്യുറകൾ ധരിക്കുക.",
                AppLanguage.MARATHI to "सावधान! गळणाऱ्या बॅटऱ्यांना उघड्या हाताने स्पर्श करू नका. बॅटरी ॲसिडमुळे त्वचा भाजते. नेहमी रबरचे हातमोजे वापरा.",
                AppLanguage.BENGALI to "সতর্কতা! খালি হাতে লিক হওয়া ব্যাটারি স্পর্শ করবেন না। সর্বদা রাবার গ্লাভস পরুন।",
                AppLanguage.GUJARATI to "ચેતવણી! લીક થતી બેટરીઓને ખુલ્લા હાથથી સ્પર્શ કરશો નહીં. હંમેશા રબરના મોજા પહેરો.",
                AppLanguage.PUNJABI to "ਚੇਤਾਵਨੀ! ਲੀਕ ਹੋ ਰਹੀਆਂ ਬੈਟਰੀਆਂ ਨੂੰ ਨੰਗੇ ਹੱਥੀਂ ਨਾ ਛੂਹੋ। ਹਮੇਸ਼ਾ ਰਬੜ ਦੇ ਦਸਤਾਨੇ ਪਾਓ।",
                AppLanguage.ODIA to "ଚେତାବନୀ! ଲିକ୍ ହେଉଥିବା ବ୍ୟାଟେରୀକୁ ଖାଲି ହାତରେ ଛୁଅନ୍ତୁ ନାହିଁ। ସର୍ବଦା ରବର ଗ୍ଲୋଭସ୍ ପିନ୍ଧନ୍ତୁ।"
            )
        ),
        SafetyItem(
            id = "SAFE_02",
            title = "Never Burn Electronic Waste",
            description = "Burning circuit boards and PVC wires releases lethal dioxins, brominated furans, and lead vapor that permanently damage lungs and nervous systems.",
            audioScript = "Never burn electronic waste or wire cables! Smoke from burning electronics contains toxic lead, dioxins, and poisonous fumes that permanently harm your lungs.",
            isCrucial = true,
            category = "Air Hazard",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Never Burn Electronic Waste",
                AppLanguage.TELUGU to "ఎలక్ట్రానిక్ వ్యర్థాలను ఎప్పుడూ కాల్చవద్దు",
                AppLanguage.HINDI to "ई-कचरे को कभी न जलाएं",
                AppLanguage.TAMIL to "மின்-கழிவுகளை ஒருபோதும் எரிக்காதீர்கள்",
                AppLanguage.KANNADA to "ಇ-ತ್ಯಾಜ್ಯವನ್ನು ಎಂದಿಗೂ ಸುಡಬೇಡಿ",
                AppLanguage.MALAYALAM to "ഇ-വേസ്റ്റ് ഒരിക്കലും കത്തിക്കരുത്",
                AppLanguage.MARATHI to "ई-कचरा कधीही जाळू नका",
                AppLanguage.BENGALI to "ই-বর্জ্য কখনই পোড়াবেন না",
                AppLanguage.GUJARATI to "ઈ-કચરો ક્યારેય બાળશો નહીં",
                AppLanguage.PUNJABI to "ਈ-ਕੂੜੇ ਨੂੰ ਕਦੇ ਨਾ ਸਾੜੋ",
                AppLanguage.ODIA to "ଇ-ବର୍ଜ୍ୟକୁ କେବେ ଜଳାନ୍ତୁ ନାହିଁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Burning circuit boards and PVC wires releases lethal dioxins and lead vapor that permanently damage lungs.",
                AppLanguage.TELUGU to "సర్క్యూట్ బోర్డులను కాల్చడం వల్ల ఊపిరితిత్తులను దెబ్బతీసే విషపూరిత వాయువులు విడుదలవుతాయి.",
                AppLanguage.HINDI to "सर्किट बोर्ड और तारों को जलाने से जहरीला धुआं और सीसा निकलता है जो फेफड़ों को नुकसान पहुंचाता है।",
                AppLanguage.TAMIL to "மின் பலகைகளை எரிப்பது நுரையீரலை பாதிக்கும் நச்சு வாயுக்களை வெளியிடுகிறது.",
                AppLanguage.KANNADA to "ವೈರ್‌ಗಳನ್ನು ಸುಡುವುದರಿಂದ ಶ್ವಾಸಕೋಶಕ್ಕೆ ಹಾನಿ ಮಾಡುವ ವಿಷಕಾರಿ ಅನಿಲ ಬಿಡುಗಡೆಯಾಗುತ್ತದೆ.",
                AppLanguage.MALAYALAM to "സർക്യൂട്ട് ബോർഡുകൾ കത്തിക്കുന്നത് ശ്വാസകോശത്തെ തകർക്കുന്ന വിഷവാതകങ്ങൾ ഉണ്ടാക്കുന്നു.",
                AppLanguage.MARATHI to "सर्किट बोर्ड आणि वायर जाळल्याने विषारी वायू निघतो ज्यामुळे फुफ्फुसांचे कायमचे नुकसान होते.",
                AppLanguage.BENGALI to "সার্কিট বোর্ড পোড়ালে বিষাক্ত ধোঁয়া বের হয় যা ফুসফুসের মারাত্মক ক্ষতি করে।",
                AppLanguage.GUJARATI to "વાયર બાળવાથી ફેફસાંને નુકસાન પહોંચાડતો ઝેરી ધુમાડો નીકળે છે.",
                AppLanguage.PUNJABI to "ਈ-ਕਚਰੇ ਨੂੰ ਸਾੜਨ ਨਾਲ ਜ਼ਹਿਰੀਲਾ ਧੂੰਆਂ ਨਿਕਲਦਾ ਹੈ ਜੋ ਫੇਫੜਿਆਂ ਨੂੰ ਨੁਕਸਾਨ ਪਹੁੰਚਾਉਂਦਾ ਹੈ।",
                AppLanguage.ODIA to "ସର୍କିଟ୍ ବୋର୍ଡ ଜଳାଇବା ଦ୍ୱାରା ବିଷାକ୍ତ ଧୂଆଁ ବାହାରି ଫୁସଫୁସ ନଷ୍ଟ କରେ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Air Hazard",
                AppLanguage.TELUGU to "వాయు ప్రమాదం",
                AppLanguage.HINDI to "वायु खतरा",
                AppLanguage.TAMIL to "காற்று ஆபத்து",
                AppLanguage.KANNADA to "ವಾಯು ಅಪಾಯ",
                AppLanguage.MALAYALAM to "വായു അപകടം",
                AppLanguage.MARATHI to "हवा धोका",
                AppLanguage.BENGALI to "বায়ু ঝুঁকি",
                AppLanguage.GUJARATI to "હવા જોખમ",
                AppLanguage.PUNJABI to "ਹਵਾ ਖਤਰਾ",
                AppLanguage.ODIA to "ବାୟୁ ବିପଦ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Never burn electronic waste! Smoke contains toxic lead and poisonous fumes.",
                AppLanguage.TELUGU to "ఎలక్ట్రానిక్ వ్యర్థాలను ఎప్పుడూ కాల్చవద్దు! దీని పొగలో విషపూరితమైన సీసం మరియు వాయువులు ఉంటాయి.",
                AppLanguage.HINDI to "ई-कचरा कभी न जलाएं! इसके धुएं में जहरीला सीसा और गैसें होती हैं।",
                AppLanguage.TAMIL to "மின்-கழிவுகளை எரிக்காதீர்கள்! புகை நச்சு வாயுக்களைக் கொண்டுள்ளது.",
                AppLanguage.KANNADA to "ಇ-ತ್ಯಾಜ್ಯವನ್ನು ಸುಡಬೇಡಿ! ಹೊಗೆಯಲ್ಲಿ ವಿಷಕಾರಿ ಅಂಶಗಳಿರುತ್ತವೆ.",
                AppLanguage.MALAYALAM to "ഇ-വേസ്റ്റ് കത്തിക്കരുത്! പുകയിൽ വിഷാംശങ്ങൾ അടങ്ങിയിരിക്കുന്നു.",
                AppLanguage.MARATHI to "ई-कचरा कधीही जाळू नका! जळणाऱ्या धुरात विषारी शिसे आणि विषारी वायू असतात.",
                AppLanguage.BENGALI to "ই-বর্জ্য কখনই পোড়াবেন না! বিষাক্ত ধোঁয়া স্বাস্থ্যের ক্ষতি করে।",
                AppLanguage.GUJARATI to "ઈ-કચરો ક્યારેય બાળશો નહીં! ધુમાડામાં ઝેરી વાયુઓ હોય છે.",
                AppLanguage.PUNJABI to "ਈ-ਕੂੜਾ ਕਦੇ ਨਾ ਸਾੜੋ! ਧੂੰਏਂ ਵਿੱਚ ਜ਼ਹਿਰੀਲਾ ਸੀਸਾ ਹੁੰਦਾ ਹੈ।",
                AppLanguage.ODIA to "ଇ-ବର୍ଜ୍ୟକୁ କେବେ ଜଳାନ୍ତୁ ନାହିଁ! ଧୂଆଁରେ ବିଷାକ୍ତ ପଦାର୍ଥ ଥାଏ।"
            )
        ),
        SafetyItem(
            id = "SAFE_03",
            title = "Do Not Puncture or Break Batteries",
            description = "Swollen or punctured lithium-ion cells can explode violently or ignite into uncontrollable chemical fires.",
            audioScript = "Do not puncture, smash, or break phone or laptop batteries. Punctured lithium batteries can explode or catch fire instantly.",
            isCrucial = true,
            category = "Fire Hazard",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Do Not Puncture or Break Batteries",
                AppLanguage.TELUGU to "బ్యాటరీలను పగలగొట్టవద్దు లేదా గుచ్చవద్దు",
                AppLanguage.HINDI to "बैटरियों को न तोड़ें और न ही छेदें",
                AppLanguage.TAMIL to "பேட்டரிகளை உடைக்கவோ துளையிடவோ வேண்டாம்",
                AppLanguage.KANNADA to "ಬ್ಯಾಟರಿಗಳನ್ನು ಒಡೆಯಬೇಡಿ ಅಥವಾ ಚುಚ್ಚಬೇಡಿ",
                AppLanguage.MALAYALAM to "ബാറ്ററികൾ തുളയ്ക്കുകയോ തകർക്കുകയോ ചെയ്യരുത്",
                AppLanguage.MARATHI to "बॅटऱ्या फोडू नका किंवा टोचू नका",
                AppLanguage.BENGALI to "ব্যাটারি ভাঙবেন না বা ফুটো করবেন না",
                AppLanguage.GUJARATI to "બેટરી તોડશો નહીં અથવા કાણું પાડશો નહીં",
                AppLanguage.PUNJABI to "ਬੈਟਰੀਆਂ ਨੂੰ ਨਾ ਤੋੜੋ ਜਾਂ ਛੇਕ ਕਰੋ",
                AppLanguage.ODIA to "ବ୍ୟାଟେରୀକୁ ଭାଙ୍ଗନ୍ତୁ ନାହିଁ କିମ୍ବା ଫୋଡ଼ନ୍ତୁ ନାହିଁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Swollen or punctured lithium-ion cells can explode violently or ignite into chemical fires.",
                AppLanguage.TELUGU to "ఉబ్బిన లేదా రంధ్రం పడిన లిథియం బ్యాటరీలు పేలవచ్చు లేదా అగ్ని ప్రమాదానికి కారణం కావచ్చు.",
                AppLanguage.HINDI to "फूली हुई या छिद्रित लिथियम बैटरी फट सकती है या आग पकड़ सकती है।",
                AppLanguage.TAMIL to "வீங்கிய பேட்டரிகள் வெடிக்கலாம் அல்லது தீப்பிடிக்கலாம்.",
                AppLanguage.KANNADA to "ಉಬ್ಬಿದ ಲಿಥಿಯಂ ಬ್ಯಾಟರಿಗಳು ಸ್ಫೋಟಗೊಳ್ಳಬಹುದು ಅಥವಾ ಬೆಂಕಿ ಹೊತ್ತಿಕೊಳ್ಳಬಹುದು.",
                AppLanguage.MALAYALAM to "വീർത്ത ബാറ്ററികൾ പൊട്ടിത്തെറിക്കുകയോ തീപിടിക്കുകയോ ചെയ്യാം.",
                AppLanguage.MARATHI to "फुगलेल्या किंवा पंक्चर झालेल्या लिथियम बॅटऱ्यांचा स्फोट होऊ शकतो किंवा आग लागू शकते.",
                AppLanguage.BENGALI to "ফোলা লিথিয়াম ব্যাটারি বিস্ফোরিত হতে পারে বা আগুন ধরতে পারে।",
                AppLanguage.GUJARATI to "ફૂલેલી બેટરી વિસ્ફોટ થઈ શકે છે અથવા આગ લાગી શકે છે.",
                AppLanguage.PUNJABI to "ਫੁੱਲੀਆਂ ਬੈਟਰੀਆਂ ਧਮਾਕਾ ਕਰ ਸਕਦੀਆਂ ਹਨ ਜਾਂ ਅੱਗ ਫੜ ਸਕਦੀਆਂ ਹਨ।",
                AppLanguage.ODIA to "ଫୁଲିଯାଇଥିବା ବ୍ୟାଟେରୀ ବିସ୍ଫୋରଣ ହୋଇପାରେ କିମ୍ବା ନିଆଁ ଲାଗିପାରେ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Fire Hazard",
                AppLanguage.TELUGU to "అగ్ని ప్రమాదం",
                AppLanguage.HINDI to "आग का खतरा",
                AppLanguage.TAMIL to "தீ ஆபத்து",
                AppLanguage.KANNADA to "ಬೆಂಕಿ ಅಪಾಯ",
                AppLanguage.MALAYALAM to "തീപിടുത്ത അപകടം",
                AppLanguage.MARATHI to "आगीचा धोका",
                AppLanguage.BENGALI to "অগ্নি ঝুঁকি",
                AppLanguage.GUJARATI to "આગનું જોખમ",
                AppLanguage.PUNJABI to "ਅੱਗ ਦਾ ਖਤਰਾ",
                AppLanguage.ODIA to "ଅଗ୍ନି ବିପଦ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Do not puncture or break batteries. Punctured lithium batteries can explode or catch fire instantly.",
                AppLanguage.TELUGU to "బ్యాటరీలను పగలగొట్టవద్దు. రంధ్రం పడిన లిథియం బ్యాటరీలు పేలిపోవచ్చు లేదా వెంటనే మంటలు అంటుకోవచ్చు.",
                AppLanguage.HINDI to "बैटरियों को न तोड़ें। छिद्रित लिथियम बैटरियां फट सकती हैं या तुरंत आग पकड़ सकती हैं।",
                AppLanguage.TAMIL to "பேட்டரிகளை உடைக்க வேண்டாம். அவை உடனடியாக வெடிக்கும் அபாயம் கொண்டது.",
                AppLanguage.KANNADA to "ಬ್ಯಾಟರಿಗಳನ್ನು ಮುರಿಯಬೇಡಿ. ಅವು ತಕ್ಷಣವೇ ಸ್ಫೋಟಗೊಳ್ಳಬಹುದು.",
                AppLanguage.MALAYALAM to "ബാറ്ററികൾ പൊട്ടിക്കരുത്. അവ തൽക്ഷണം തീപിടിച്ചേക്കാം.",
                AppLanguage.MARATHI to "बॅटऱ्या फोडू नका किंवा कापू नका. पंक्चर झालेल्या बॅटऱ्या लगेच पेट घेऊ शकतात.",
                AppLanguage.BENGALI to "ব্যাটারি ভাঙবেন না। ফুটো হওয়া ব্যাটারিতে তৎক্ষণাৎ আগুন লাগতে পারে।",
                AppLanguage.GUJARATI to "બેટરી તોડશો નહીં. તેમાં તરત જ આગ લાગી શકે છે.",
                AppLanguage.PUNJABI to "ਬੈਟਰੀਆਂ ਨੂੰ ਨਾ ਤੋੜੋ। ਇਹ ਤੁਰੰਤ ਅੱਗ ਫੜ ਸਕਦੀਆਂ ਹਨ।",
                AppLanguage.ODIA to "ବ୍ୟାଟେରୀ ଭାଙ୍ଗନ୍ତୁ ନାହିଁ। ଏଥିରେ ତୁରନ୍ତ ନିଆଁ ଲାଗିପାରେ।"
            )
        ),
        SafetyItem(
            id = "SAFE_04",
            title = "Always Wear Heavy Protective Gloves",
            description = "Broken glass from CRT/LCD screens and jagged metal stamped chassis cause deep cuts and infection. Always wear cut-resistant gloves.",
            audioScript = "Always wear thick protective gloves when handling e-waste. This protects your hands from glass cuts, jagged metal, and hazardous chemical residue.",
            isCrucial = false,
            category = "Hazard Prevention",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Always Wear Heavy Protective Gloves",
                AppLanguage.TELUGU to "ఎల్లప్పుడూ మందపాటి చేతి తొడుగులు ధరించండి",
                AppLanguage.HINDI to "हमेशा भारी सुरक्षा दस्ताने पहनें",
                AppLanguage.TAMIL to "எப்போதும் தடிமனான பாதுகாப்பு கையுறைகளை அணியுங்கள்",
                AppLanguage.KANNADA to "ಯಾವಾಗಲೂ ದಪ್ಪ ರಕ್ಷಣಾತ್ಮಕ ಕೈಗವಸುಗಳನ್ನು ಧರಿಸಿ",
                AppLanguage.MALAYALAM to "എല്ലായ്പ്പോഴും കട്ടിയുള്ള സുരക്ഷാ കയ്യുറകൾ ധരിക്കുക",
                AppLanguage.MARATHI to "नेहमी जाड सुरक्षा हातमोजे वापरा",
                AppLanguage.BENGALI to "সর্বদা ভারী সুরক্ষামূলক গ্লাভস পরুন",
                AppLanguage.GUJARATI to "હંમેશા ભારે રક્ષણાત્મક મોજા પહેરો",
                AppLanguage.PUNJABI to "ਹਮੇਸ਼ਾ ਭਾਰੇ ਸੁਰੱਖਿਆ ਦਸਤਾਨੇ ਪਾਓ",
                AppLanguage.ODIA to "ସର୍ବଦା ମୋଟା ସୁରକ୍ଷା ଗ୍ଲୋଭସ୍ ପିନ୍ଧନ୍ତୁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Broken glass and jagged metal cause deep cuts and infection. Always wear cut-resistant gloves.",
                AppLanguage.TELUGU to "పగిలిన గాజు మరియు పదునైన లోహాలు లోతైన కోతలను కలిగిస్తాయి. కోత-నిరోధక చేతి తొడుగులు ధరించండి.",
                AppLanguage.HINDI to "टूटा हुआ कांच और नुकीला धातु गहरे घाव पैदा करते हैं। हमेशा कट-प्रतिरोधी दस्ताने पहनें।",
                AppLanguage.TAMIL to "உடைந்த கண்ணாடி மற்றும் கூர்மையான உலோகம் காயங்களை ஏற்படுத்துகிறது. கையுறைகளை அணியுங்கள்.",
                AppLanguage.KANNADA to "ಒಡೆದ ಗಾಜು ಮತ್ತು ಚೂಪಾದ ಲೋಹ ಗಾಯಗಳನ್ನು ಉಂಟುಮಾಡುತ್ತದೆ. ಕೈಗವಸುಗಳನ್ನು ಧರಿಸಿ.",
                AppLanguage.MALAYALAM to "പൊട്ടിയ ഗ്ലാസും മൂർച്ചയുള്ള ലോഹങ്ങളും മുറിവുകൾ ഉണ്ടാക്കുന്നു. കയ്യുറകൾ ധരിക്കുക.",
                AppLanguage.MARATHI to "काच आणि टोकदार धातूंमुळे गंभीर जखमा होतात. नेहमी मजबूत हातमोजे वापरा.",
                AppLanguage.BENGALI to "ভাঙা কাচ এবং ধারালো ধাতু গভীর ক্ষতের সৃষ্টি করে। সর্বদা গ্লাভস পরুন।",
                AppLanguage.GUJARATI to "તૂટેલો કાચ અને તીક્ષ્ણ ધાતુ ઊંડા ઘા કરી શકે છે. મોજા પહેરો.",
                AppLanguage.PUNJABI to "ਟੁੱਟਿਆ ਕੱਚ ਅਤੇ ਤਿੱਖਾ ਧਾਤ ਡੂੰਘੇ ਜ਼ਖਮ ਕਰ ਸਕਦੇ ਹਨ। ਦਸਤਾਨੇ ਪਾਓ।",
                AppLanguage.ODIA to "ଭଙ୍ଗା କାଚ ଏବଂ ଧାରୁଆ ଧାତୁ ଗଭୀର କ୍ଷତ ସୃଷ୍ଟି କରେ। ଗ୍ଲୋଭସ୍ ପିନ୍ଧନ୍ତୁ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Hazard Prevention",
                AppLanguage.TELUGU to "ప్రమాద నివారణ",
                AppLanguage.HINDI to "खतरा रोकथाम",
                AppLanguage.TAMIL to "ஆபத்து தடுப்பு",
                AppLanguage.KANNADA to "ಅಪಾಯ ತಡೆಗಟ್ಟುವಿಕೆ",
                AppLanguage.MALAYALAM to "അപകട പ്രതിരോധം",
                AppLanguage.MARATHI to "धोका प्रतिबंध",
                AppLanguage.BENGALI to "ঝুঁকি প্রতিরোধ",
                AppLanguage.GUJARATI to "જોખમ નિવારણ",
                AppLanguage.PUNJABI to "ਖਤਰਾ ਰੋਕਥਾਮ",
                AppLanguage.ODIA to "ବିପଦ ନିବାରଣ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Always wear thick protective gloves when handling e-waste.",
                AppLanguage.TELUGU to "ఇ-వ్యర్థాలను నిర్వహించేటప్పుడు ఎల్లప్పుడూ మందపాటి రక్షణ చేతి తొడుగులు ధరించండి.",
                AppLanguage.HINDI to "ई-कचरा संभालते समय हमेशा मोटे सुरक्षा दस्ताने पहनें।",
                AppLanguage.TAMIL to "மின் கழிவுகளை கையாளும் போது தடிமனான கையுறைகளை அணியுங்கள்.",
                AppLanguage.KANNADA to "ಇ-ತ್ಯಾಜ್ಯ ನಿರ್ವಹಿಸುವಾಗ ದಪ್ಪ ಕೈಗವಸುಗಳನ್ನು ಧರಿಸಿ.",
                AppLanguage.MALAYALAM to "ഇ-വേസ്റ്റ് കൈകാര്യം ചെയ്യുമ്പോൾ കട്ടിയുള്ള കയ്യുറകൾ ധരിക്കുക.",
                AppLanguage.MARATHI to "ई-कचरा हाताळताना नेहमी जाड संरक्षक हातमोजे वापरा.",
                AppLanguage.BENGALI to "ই-বর্জ্য নাড়াচাড়া করার সময় সর্বদা মোটা গ্লাভস পরুন।",
                AppLanguage.GUJARATI to "ઈ-કચરો સંભાળતી વખતે હંમેશા જાડા મોજા પહેરો.",
                AppLanguage.PUNJABI to "ਈ-ਕੂੜਾ ਸੰਭਾਲਣ ਸਮੇਂ ਹਮੇਸ਼ਾ ਮੋਟੇ ਦਸਤਾਨੇ ਪਾਓ।",
                AppLanguage.ODIA to "ଇ-ବର୍ଜ୍ୟ ପରିଚାଳନା ବେଳେ ସର୍ବଦା ମୋଟା ଗ୍ଲୋଭସ୍ ପିନ୍ଧନ୍ତୁ।"
            )
        ),
        SafetyItem(
            id = "SAFE_05",
            title = "Keep Damaged Batteries Away From Heat",
            description = "Store all recovered batteries in a shaded, ventilated place away from direct sunlight, open gas cookstoves, or open flames.",
            audioScript = "Keep all damaged or swollen batteries away from heat and open flames. Store them in a cool, ventilated container away from direct sunlight.",
            isCrucial = true,
            category = "Fire Hazard",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Keep Damaged Batteries Away From Heat",
                AppLanguage.TELUGU to "దెబ్బతిన్న బ్యాటరీలను వేడికి దూరంగా ఉంచండి",
                AppLanguage.HINDI to "क्षतिग्रस्त बैटरियों को गर्मी से दूर रखें",
                AppLanguage.TAMIL to "சேதமடைந்த பேட்டரிகளை வெப்பத்திலிருந்து விலக்கி வைக்கவும்",
                AppLanguage.KANNADA to "ಹಾನಿಗೊಳಗಾದ ಬ್ಯಾಟರಿಗಳನ್ನು ಶಾಖದಿಂದ ದೂರವಿಡಿ",
                AppLanguage.MALAYALAM to "കേടായ ബാറ്ററികൾ ചൂടിൽ നിന്ന് അകറ്റി നിർത്തുക",
                AppLanguage.MARATHI to "खराब झालेल्या बॅटऱ्या उष्णतेपासून दूर ठेवा",
                AppLanguage.BENGALI to "ক্ষতিগ্রস্ত ব্যাটারি তাপ থেকে দূরে রাখুন",
                AppLanguage.GUJARATI to "ક્ષતિગ્રસ્ત બેટરીઓને ગરમીથી દૂર રાખો",
                AppLanguage.PUNJABI to "ਖਰਾਬ ਬੈਟਰੀਆਂ ਨੂੰ ਗਰਮੀ ਤੋਂ ਦੂਰ ਰੱਖੋ",
                AppLanguage.ODIA to "କ୍ଷତିଗ୍ରସ୍ତ ବ୍ୟାଟେରୀକୁ ଉତ୍ତାପଠାରୁ ଦୂରରେ ରଖନ୍ତୁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Store batteries in a shaded place away from sunlight and open flames.",
                AppLanguage.TELUGU to "సూర్యరశ్మి మరియు మంటలకు దూరంగా బ్యాటరీలను నీడ ఉన్న ప్రదేశంలో నిల్వ చేయండి.",
                AppLanguage.HINDI to "बैटरियों को धूप और खुली लपटों से दूर छायादार जगह में रखें।",
                AppLanguage.TAMIL to "சூரிய ஒளி மற்றும் தீயிலிருந்து விலகி நிழலான இடத்தில் வைக்கவும்.",
                AppLanguage.KANNADA to "ಬಿಸಿಲು ಮತ್ತು ಜ್ವಾಲೆಯಿಂದ ದೂರವಿರುವ ಸ್ಥಳದಲ್ಲಿ ಇರಿಸಿ.",
                AppLanguage.MALAYALAM to "സൂര്യപ്രകാശത്തിൽ നിന്നും തീയിൽ നിന്നും അകലെ തണലുള്ള സ്ഥലത്ത് സൂക്ഷിക്കുക.",
                AppLanguage.MARATHI to "बॅटऱ्या सूर्यप्रकाश आणि आगीपासून दूर सावलीच्या आणि हवेशीर जागी ठेवा.",
                AppLanguage.BENGALI to "সূর্যের আলো ও আগুন থেকে দূরে ছায়াযুক্ত স্থানে ব্যাটারি রাখুন।",
                AppLanguage.GUJARATI to "સૂર્યપ્રકાશ અને આગથી દૂર છાંયડાવાળી જગ્યાએ બેટરી રાખો.",
                AppLanguage.PUNJABI to "ਬੈਟਰੀਆਂ ਨੂੰ ਧੁੱਪ ਅਤੇ ਅੱਗ ਤੋਂ ਦੂਰ ਛਾਂ ਵਿੱਚ ਰੱਖੋ।",
                AppLanguage.ODIA to "ସୂର୍ଯ୍ୟ କିରଣ ଓ ନିଆଁଠାରୁ ଦୂରରେ ଛାଇ ସ୍ଥାନରେ ବ୍ୟାଟେରୀ ରଖନ୍ତୁ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Fire Hazard",
                AppLanguage.TELUGU to "అగ్ని ప్రమాదం",
                AppLanguage.HINDI to "आग का खतरा",
                AppLanguage.TAMIL to "தீ ஆபத்து",
                AppLanguage.KANNADA to "ಬೆಂಕಿ ಅಪಾಯ",
                AppLanguage.MALAYALAM to "തീപിടുത്ത അപകടം",
                AppLanguage.MARATHI to "आगीचा धोका",
                AppLanguage.BENGALI to "অগ্নি ঝুঁকি",
                AppLanguage.GUJARATI to "આગનું જોખમ",
                AppLanguage.PUNJABI to "ਅੱਗ ਦਾ ਖਤਰਾ",
                AppLanguage.ODIA to "ଅଗ୍ନି ବିପଦ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Keep all damaged batteries away from heat and open flames.",
                AppLanguage.TELUGU to "అన్ని దెబ్బతిన్న బ్యాటరీలను వేడి మరియు మంటలకు దూరంగా ఉంచండి.",
                AppLanguage.HINDI to "सभी क्षतिग्रस्त बैटरियों को गर्मी और खुली लपटों से दूर रखें।",
                AppLanguage.TAMIL to "சேதமடைந்த பேட்டரிகளை வெப்பத்திலிருந்து விலக்கி வைக்கவும்.",
                AppLanguage.KANNADA to "ಹಾನಿಗೊಳಗಾದ ಬ್ಯಾಟರಿಗಳನ್ನು ಶಾಖದಿಂದ ದೂರವಿಡಿ.",
                AppLanguage.MALAYALAM to "കേടായ എല്ലാ ബാറ്ററികളും ചൂടിൽ നിന്ന് അകറ്റി നിർത്തുക.",
                AppLanguage.MARATHI to "खराब झालेल्या सर्व बॅटऱ्या उष्णता आणि आगीपासून दूर ठेवा.",
                AppLanguage.BENGALI to "সমস্ত ক্ষতিগ্রস্ত ব্যাটারি তাপ এবং খোলা আগুন থেকে দূরে রাখুন।",
                AppLanguage.GUJARATI to "બધી ક્ષતિગ્રસ્ત બેટરીઓને ગરમી અને આગથી દૂર રાખો.",
                AppLanguage.PUNJABI to "ਖਰਾਬ ਬੈਟਰੀਆਂ ਨੂੰ ਗਰਮੀ ਅਤੇ ਅੱਗ ਤੋਂ ਦੂਰ ਰੱਖੋ।",
                AppLanguage.ODIA to "ସମସ୍ତ କ୍ଷତିଗ୍ରସ୍ତ ବ୍ୟାଟେରୀକୁ ଉତ୍ତାପ ଏବଂ ନିଆଁଠାରୁ ଦୂରରେ ରଖନ୍ତୁ।"
            )
        ),
        SafetyItem(
            id = "SAFE_06",
            title = "Separate Hazardous Materials",
            description = "Do not mix broken fluorescent tubes, mercury switches, or swollen batteries with plain copper wire or chassis scrap.",
            audioScript = "Keep hazardous materials separated! Store batteries, fluorescent lamps, and circuit boards in separate bags from plain metals.",
            isCrucial = false,
            category = "Hazard Prevention",
            titleTranslations = mapOf(
                AppLanguage.ENGLISH to "Separate Hazardous Materials",
                AppLanguage.TELUGU to "ప్రమాదకర పదార్థాలను వేరు చేయండి",
                AppLanguage.HINDI to "खतरनाक सामग्रियों को अलग करें",
                AppLanguage.TAMIL to "அபாயகரமான பொருட்களைத் தனியாகப் பிரிக்கவும்",
                AppLanguage.KANNADA to "ಅಪಾಯಕಾರಿ ವಸ್ತುಗಳನ್ನು ಪ್ರತ್ಯೇಕಿಸಿ",
                AppLanguage.MALAYALAM to "അപകടകരമായ വസ്തുക്കൾ വേർതിരിക്കുക",
                AppLanguage.MARATHI to "धोकादायक साहित्य वेगळे करा",
                AppLanguage.BENGALI to "বিপজ্জনক উপকরণ আলাদা করুন",
                AppLanguage.GUJARATI to "જોખમી સામગ્રી અલગ કરો",
                AppLanguage.PUNJABI to "ਖਤਰਨਾਕ ਸਮੱਗਰੀ ਵੱਖ ਕਰੋ",
                AppLanguage.ODIA to "ବିପଦପୂର୍ଣ୍ଣ ସାମଗ୍ରୀ ଅଲଗା କରନ୍ତୁ"
            ),
            descTranslations = mapOf(
                AppLanguage.ENGLISH to "Do not mix fluorescent tubes or mercury switches with plain copper wire or chassis scrap.",
                AppLanguage.TELUGU to "ఫ్లోరోసెంట్ ట్యూబ్‌లు లేదా పాదరసం స్విచ్‌లను సాధారణ రాగి తీగతో కలపవద్దు.",
                AppLanguage.HINDI to "पारा स्विच या फ्लोरोसेंट ट्यूबों को सादे तांबे के तार के साथ न मिलाएं।",
                AppLanguage.TAMIL to "பாதரச சுவிட்சுகள் அல்லது டியூப் லைட்களை சாதாரண கம்பிகளுடன் கலக்க வேண்டாம்.",
                AppLanguage.KANNADA to "ಟ್ಯೂಬ್‌ಲೈಟ್ ಅಥವಾ ಪಾದರಸದ ಸ್ವಿಚ್‌ಗಳನ್ನು ಸಾಮಾನ್ಯ ತಂತಿಯೊಂದಿಗೆ ಬೆರೆಸಬೇಡಿ.",
                AppLanguage.MALAYALAM to "മെർക്കുറി സ്വിച്ചുകളോ ഫ്ലൂറസെൻ്റ് ട്യൂബുകളോ സാധാരണ വയറുകളുമായി കലർത്തരുത്.",
                AppLanguage.MARATHI to "ट्यूबलाईट किंवा पारा असलेली उपकरणे तांब्याच्या तारांमध्ये मिसळू नका.",
                AppLanguage.BENGALI to "পারদ সুইচ বা টিউবলাইট সাধারণ তারের সাথে মেশাবেন না।",
                AppLanguage.GUJARATI to "મર્ક્યુરી સ્વીચો અથવા ટ્યુબલાઇટને સાદા વાયરો સાથે ભેળવશો નહીં.",
                AppLanguage.PUNJABI to "ਟਿਊਬਲਾਈਟ ਜਾਂ ਪਾਰਾ ਸਵਿੱਚਾਂ ਨੂੰ ਸਾਦੇ ਤਾਂਬੇ ਦੀਆਂ ਤਾਰਾਂ ਨਾਲ ਨਾ ਮਿਲਾਓ।",
                AppLanguage.ODIA to "ପାରଦ ସୁଇଚ୍ କିମ୍ବା ଟ୍ୟୁବ୍ ଲାଇଟକୁ ସାଧାରଣ ତାର ସହିତ ମିଶାନ୍ତୁ ନାହିଁ।"
            ),
            categoryTranslations = mapOf(
                AppLanguage.ENGLISH to "Hazard Prevention",
                AppLanguage.TELUGU to "ప్రమాద నివారణ",
                AppLanguage.HINDI to "खतरा रोकथाम",
                AppLanguage.TAMIL to "ஆபத்து தடுப்பு",
                AppLanguage.KANNADA to "ಅಪಾಯ ತಡೆಗಟ್ಟುವಿಕೆ",
                AppLanguage.MALAYALAM to "അപകട പ്രതിരോധം",
                AppLanguage.MARATHI to "धोका प्रतिबंध",
                AppLanguage.BENGALI to "ঝুঁকি প্রতিরোধ",
                AppLanguage.GUJARATI to "જોખમ નિવારણ",
                AppLanguage.PUNJABI to "ਖਤਰਾ ਰੋਕਥਾਮ",
                AppLanguage.ODIA to "ବିପଦ ନିବାରଣ"
            ),
            audioTranslations = mapOf(
                AppLanguage.ENGLISH to "Keep hazardous materials separated! Store batteries and lamps in separate bags.",
                AppLanguage.TELUGU to "ప్రమాదకర పదార్థాలను వేరుగా ఉంచండి! బ్యాటరీలు మరియు దీపాలను ప్రత్యేక సంచులలో భద్రపరచండి.",
                AppLanguage.HINDI to "खतरनाक सामग्री को अलग रखें! बैटरी और लैंप को अलग बैग में स्टोर करें।",
                AppLanguage.TAMIL to "அபாயகரமான பொருட்களைத் தனித்தனியாக வைத்திருங்கள்.",
                AppLanguage.KANNADA to "ಅಪಾಯಕಾರಿ ವಸ್ತುಗಳನ್ನು ಪ್ರತ್ಯೇಕವಾಗಿ ಇರಿಸಿ.",
                AppLanguage.MALAYALAM to "അപകടകരമായ വസ്തുക്കൾ വെവ്വേറെ സൂക്ഷിക്കുക.",
                AppLanguage.MARATHI to "धोकादायक साहित्य वेगळे ठेवा! बॅटऱ्या आणि दिवे वेगळ्या पिशव्यांमध्ये ठेवा.",
                AppLanguage.BENGALI to "বিপজ্জনক জিনিস আলাদা রাখুন! ব্যাটারি আলাদা ব্যাগে রাখুন।",
                AppLanguage.GUJARATI to "જોખમી સામગ્રી અલગ રાખો! બેટરીઓ અલગ બેગમાં રાખો.",
                AppLanguage.PUNJABI to "ਖਤਰਨਾਕ ਸਮੱਗਰੀ ਵੱਖ ਰੱਖੋ! ਬੈਟਰੀਆਂ ਨੂੰ ਵੱਖਰੇ ਥੈਲਿਆਂ ਵਿੱਚ ਰੱਖੋ।",
                AppLanguage.ODIA to "ବିପଦପୂର୍ଣ୍ଣ ସାମଗ୍ରୀ ଅଲଗା ରଖନ୍ତୁ! ବ୍ୟାଟେରୀ ଅଲଗା ବ୍ୟାଗରେ ରଖନ୍ତୁ।"
            )
        )
    )
}
