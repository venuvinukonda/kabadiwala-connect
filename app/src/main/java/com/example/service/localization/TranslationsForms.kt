package com.example.service.localization

/**
 * Translations for Registration forms, Forgot Password dialogs, and verification notices across all 11 supported languages.
 */
object TranslationsForms {

    val stringMap: Map<String, Map<AppLanguage, String>> = mapOf(
        "reg_status_pending_title" to mapOf(
            AppLanguage.ENGLISH to "Status: Pending Admin Approval",
            AppLanguage.TELUGU to "స్థితి: అడ్మిన్ ఆమోదం కోసం వేచి ఉంది",
            AppLanguage.HINDI to "स्थिति: एडमिन की मंजूरी लंबित है",
            AppLanguage.TAMIL to "நிலை: நிர்வாகி ஒப்புதலுக்காக நிலுவையில் உள்ளது",
            AppLanguage.KANNADA to "ಸ್ಥಿತಿ: ಅಡ್ಮಿನ್ ಅನುಮೋದನೆ ಬಾಕಿಯಿದೆ",
            AppLanguage.MALAYALAM to "നില: അഡ്മിൻ അംഗീകാരം കാത്തിരിക്കുന്നു",
            AppLanguage.MARATHI to "स्थिती: प्रशासकीय मंजुरी प्रलंबित",
            AppLanguage.BENGALI to "স্থিতি: অ্যাডমিন অনুমোদনের অপেক্ষায়",
            AppLanguage.GUJARATI to "સ્થિતિ: એડમિન મંજૂરી બાકી છે",
            AppLanguage.PUNJABI to "ਸਥਿਤੀ: ਐਡਮਿਨ ਮਨਜ਼ੂਰੀ ਬਕਾਇਆ",
            AppLanguage.ODIA to "ସ୍ଥିତି: ଆଡମିନ୍ ଅନୁମୋଦନ ବାକି ଅଛି"
        ),
        "reg_status_pending_desc" to mapOf(
            AppLanguage.ENGLISH to "Your registration has been submitted. You can use the application after administrator approval.",
            AppLanguage.TELUGU to "మీ నమోదు సమర్పించబడింది. నిర్వాహకుని ఆమోదం తర్వాత మీరు యాప్‌ని ఉపయోగించవచ్చు.",
            AppLanguage.HINDI to "आपका पंजीकरण जमा कर दिया गया है। व्यवस्थापक की मंजूरी के बाद आप ऐप का उपयोग कर सकते हैं।",
            AppLanguage.TAMIL to "உங்கள் பதிவு சமர்ப்பிக்கப்பட்டது. நிர்வாகி ஒப்புதலுக்குப் பிறகு நீங்கள் பயன்பாட்டைப் பயன்படுத்தலாம்.",
            AppLanguage.KANNADA to "ನಿಮ್ಮ ನೋಂದಣಿ ಸಲ್ಲಿಕೆಯಾಗಿದೆ. ನಿರ್ವಾಹಕರ ಅನುಮೋದನೆಯ ನಂತರ ನೀವು ಅಪ್ಲಿಕೇಶನ್ ಬಳಸಬಹುದು.",
            AppLanguage.MALAYALAM to "നിങ്ങളുടെ രജിസ്ട്രേഷൻ സമർപ്പിച്ചു. അഡ്മിൻ അംഗീകാരത്തിന് ശേഷം നിങ്ങൾക്ക് ആപ്പ് ഉപയോഗിക്കാം.",
            AppLanguage.MARATHI to "आपली नोंदणी सबमिट केली गेली आहे. प्रशासकीय मंजुरीनंतर आपण ॲप वापरू शकता.",
            AppLanguage.BENGALI to "আপনার নিবন্ধন জমা দেওয়া হয়েছে। অ্যাডমিন অনুমোদনের পরে আপনি অ্যাপটি ব্যবহার করতে পারেন।",
            AppLanguage.GUJARATI to "તમારી નોંધણી સબમિટ થઈ ગઈ છે. એડમિન મંજૂરી પછી તમે ઍપ વાપરી શકો છો.",
            AppLanguage.PUNJABI to "ਤੁਹਾਡੀ ਰਜਿਸਟ੍ਰੇਸ਼ਨ ਜਮ੍ਹਾ ਹੋ ਗਈ ਹੈ। ਐਡਮਿਨ ਮਨਜ਼ੂਰੀ ਤੋਂ ਬਾਅਦ ਤੁਸੀਂ ਐਪ ਵਰਤ ਸਕਦੇ ਹੋ।",
            AppLanguage.ODIA to "ଆପଣଙ୍କ ପଞ୍ଜୀକରଣ ଦାଖଲ ହୋଇଛି। ପ୍ରଶାସକ ଅନୁମୋଦନ ପରେ ଆପଣ ଆପ୍ ବ୍ୟବହାର କରିପାରିବେ।"
        ),
        "reg_back_to_role" to mapOf(
            AppLanguage.ENGLISH to "Back to Role Selection",
            AppLanguage.TELUGU to "పాత్ర ఎంపికకు తిరిగి వెళ్లండి",
            AppLanguage.HINDI to "भूमिका चयन पर वापस जाएं",
            AppLanguage.TAMIL to "பங்குத் தேர்வுக்குத் திரும்பு",
            AppLanguage.KANNADA to "ಪಾತ್ರ ಆಯ್ಕೆಗೆ ಹಿಂತಿರುಗಿ",
            AppLanguage.MALAYALAM to "റോൾ തിരഞ്ഞെടുപ്പിലേക്ക് മടങ്ങുക",
            AppLanguage.MARATHI to "भूमिका निवडीवर परत जा",
            AppLanguage.BENGALI to "ভূমিকা নির্বাচনে ফিরে যান",
            AppLanguage.GUJARATI to "ભૂમિકા પસંદગી પર પાછા જાઓ",
            AppLanguage.PUNJABI to "ਭੂਮਿਕਾ ਚੋਣ 'ਤੇ ਵਾਪਸ ਜਾਓ",
            AppLanguage.ODIA to "ଭୂମିକା ଚୟନକୁ ଫେରନ୍ତୁ"
        ),
        "reg_collector_title" to mapOf(
            AppLanguage.ENGLISH to "New E-Waste Collector",
            AppLanguage.TELUGU to "కొత్త ఇ-వ్యర్థాల సేకర్త",
            AppLanguage.HINDI to "नया ई-कचरा संग्राहक",
            AppLanguage.TAMIL to "புதிய மின்-கழிவு சேகரிப்பாளர்",
            AppLanguage.KANNADA to "ಹೊಸ ಇ-ತ್ಯಾಜ್ಯ ಸಂಗ್ರಾಹಕ",
            AppLanguage.MALAYALAM to "പുതിയ ഇ-വേസ്റ്റ് കളക്ടർ",
            AppLanguage.MARATHI to "नवीन ई-कचरा गोळा करणारा",
            AppLanguage.BENGALI to "নতুন ই-বর্জ্য সংগ্রাহক",
            AppLanguage.GUJARATI to "નવા ઈ-કચરા સંગ્રાહક",
            AppLanguage.PUNJABI to "ਨਵਾਂ ਈ-ਕੂੜਾ ਕੁਲੈਕਟਰ",
            AppLanguage.ODIA to "ନୂଆ ଇ-ବର୍ଜ୍ୟ ସଂଗ୍ରାହକ"
        ),
        "reg_collector_desc" to mapOf(
            AppLanguage.ENGLISH to "Fill in your details. Accounts are reviewed by Admin for safety verification.",
            AppLanguage.TELUGU to "మీ వివరాలను పూరించండి. భద్రతా ధృవీకరణ కోసం ఖాతాలను అడ్మిన్ సమీక్షిస్తారు.",
            AppLanguage.HINDI to "अपना विवरण भरें। सुरक्षा सत्यापन के लिए खातों की एडमिन द्वारा समीक्षा की जाती है।",
            AppLanguage.TAMIL to "உங்கள் விவரங்களை நிரப்பவும். பாதுகாப்பு சரிபார்ப்புக்காக கணக்குகள் மதிப்பாய்வு செய்யப்படுகின்றன.",
            AppLanguage.KANNADA to "ನಿಮ್ಮ ವಿವರಗಳನ್ನು ಭರ್ತಿ ಮಾಡಿ. ಸುರಕ್ಷತಾ ಪರಿಶೀಲನೆಗಾಗಿ ಖಾತೆಗಳನ್ನು ಅಡ್ಮಿನ್ ಪರಿಶೀಲಿಸುತ್ತಾರೆ.",
            AppLanguage.MALAYALAM to "നിങ്ങളുടെ വിവരങ്ങൾ പൂരിപ്പിക്കുക. അഡ്മിൻ പരിശോധിച്ച ശേഷം അക്കൗണ്ട് സജീവമാകും.",
            AppLanguage.MARATHI to "आपला तपशील भरा. सुरक्षिततेसाठी खाती प्रशासकाद्वारे तपासली जातात.",
            AppLanguage.BENGALI to "আপনার বিবরণ পূরণ করুন। সুরক্ষার জন্য অ্যাকাউন্ট অ্যাডমিন দ্বারা যাচাই করা হয়।",
            AppLanguage.GUJARATI to "તમારી વિગતો ભરો. સુરક્ષા માટે ખાતાઓની એડમિન દ્વારા સમીક્ષા કરવામાં આવે છે.",
            AppLanguage.PUNJABI to "ਆਪਣੇ ਵੇਰਵੇ ਭਰੋ। ਸੁਰੱਖਿਆ ਜਾਂਚ ਲਈ ਖਾਤਿਆਂ ਦੀ ਐਡਮਿਨ ਦੁਆਰਾ ਸਮੀਖਿਆ ਕੀਤੀ ਜਾਂਦੀ ਹੈ।",
            AppLanguage.ODIA to "ଆପଣଙ୍କ ବିବରଣୀ ପୂରଣ କରନ୍ତୁ। ସୁରକ୍ଷା ଯାଞ୍ଚ ପାଇଁ ଆଡମିନ୍ ସମୀକ୍ଷା କରିବେ।"
        ),
        "reg_voice_collector" to mapOf(
            AppLanguage.ENGLISH to "Registration form for e-waste collector. Please enter your name, phone, government ID, address, and preferred collection area.",
            AppLanguage.TELUGU to "ఇ-వ్యర్థాల సేకర్త రిజిస్ట్రేషన్ ఫారమ్. దయచేసి మీ పేరు, ఫోన్ నంబర్, గుర్తింపు కార్డు మరియు చిరునామా నమోదు చేయండి.",
            AppLanguage.HINDI to "ई-कचरा संग्राहक पंजीकरण फॉर्म। कृपया अपना नाम, फोन, सरकारी आईडी, पता और पसंदीदा संग्रह क्षेत्र दर्ज करें।",
            AppLanguage.TAMIL to "மின் கழிவு சேகரிப்பாளர் பதிவு படிவம். பெயர், தொலைபேசி, அரசு அடையாள அட்டை மற்றும் முகவரியை உள்ளிடவும்.",
            AppLanguage.KANNADA to "ಇ-ತ್ಯಾಜ್ಯ ಸಂಗ್ರಾಹಕರ ನೋಂದಣಿ ನಮೂನೆ. ದಯವಿಟ್ಟು ಹೆಸರು, ಫೋನ್, ಸರ್ಕಾರಿ ಗುರುತಿನ ಚೀಟಿ ಮತ್ತು ವಿಳಾಸ ನಮೂದಿಸಿ.",
            AppLanguage.MALAYALAM to "ഇ-വേസ്റ്റ് കളക്ടർ രജിസ്ട്രേഷൻ ഫോം. പേര്, ഫോൺ, ഐഡി, വിലാസം എന്നിവ നൽകുക.",
            AppLanguage.MARATHI to "ई-कचरा गोळा करणाऱ्याचा नोंदणी अर्ज. कृपया आपले नाव, फोन, ओळखपत्र आणि पत्ता प्रविष्ट करा.",
            AppLanguage.BENGALI to "ই-বর্জ্য সংগ্রাহক নিবন্ধন ফর্ম। নাম, ফোন এবং ঠিকানা লিখুন।",
            AppLanguage.GUJARATI to "ઈ-કચરા સંગ્રાહક નોંધણી ફોર્મ. કૃપા કરીને નામ, ફોન, સરકારી આઈડી અને સરનામું દાખલ કરો.",
            AppLanguage.PUNJABI to "ਈ-ਕੂੜਾ ਕੁਲੈਕਟਰ ਰਜਿਸਟ੍ਰੇਸ਼ਨ ਫਾਰਮ। ਕਿਰਪਾ ਕਰਕੇ ਨਾਮ, ਫ਼ੋਨ ਅਤੇ ਪਤਾ ਦਰਜ ਕਰੋ।",
            AppLanguage.ODIA to "ଇ-ବର୍ଜ୍ୟ ସଂଗ୍ରାହକ ପଞ୍ଜୀକରଣ ଫର୍ମ। ଦୟାକରି ନାମ, ଫୋନ୍ ଏବଂ ଠିକଣା ପ୍ରବେଶ କରନ୍ତୁ।"
        ),
        "reg_full_name" to mapOf(
            AppLanguage.ENGLISH to "Full Name *",
            AppLanguage.TELUGU to "పూర్తి పేరు *",
            AppLanguage.HINDI to "पूरा नाम *",
            AppLanguage.TAMIL to "முழு பெயர் *",
            AppLanguage.KANNADA to "ಪೂರ್ಣ ಹೆಸರು *",
            AppLanguage.MALAYALAM to "മുഴുവൻ പേര് *",
            AppLanguage.MARATHI to "पूर्ण नाव *",
            AppLanguage.BENGALI to "পুরো নাম *",
            AppLanguage.GUJARATI to "પૂરું નામ *",
            AppLanguage.PUNJABI to "ਪੂਰਾ ਨਾਮ *",
            AppLanguage.ODIA to "ପୂରା ନାମ *"
        ),
        "reg_phone_number" to mapOf(
            AppLanguage.ENGLISH to "Phone Number *",
            AppLanguage.TELUGU to "ఫోన్ నంబర్ *",
            AppLanguage.HINDI to "फ़ोन नंबर *",
            AppLanguage.TAMIL to "தொலைபேசி எண் *",
            AppLanguage.KANNADA to "ದೂರವಾಣಿ ಸಂಖ್ಯೆ *",
            AppLanguage.MALAYALAM to "ഫോൺ നമ്പർ *",
            AppLanguage.MARATHI to "फोन नंबर *",
            AppLanguage.BENGALI to "ফোন নম্বর *",
            AppLanguage.GUJARATI to "ફોન નંબર *",
            AppLanguage.PUNJABI to "ਫ਼ੋਨ ਨੰਬਰ *",
            AppLanguage.ODIA to "ଫୋନ୍ ନମ୍ବର *"
        ),
        "reg_govt_id" to mapOf(
            AppLanguage.ENGLISH to "Government ID (Aadhaar / Voter ID) *",
            AppLanguage.TELUGU to "ప్రభుత్వ గుర్తింపు కార్డు (ఆధార్ / ఓటర్ ID) *",
            AppLanguage.HINDI to "सरकारी पहचान पत्र (आधार / वोटर आईडी) *",
            AppLanguage.TAMIL to "அரசு அடையாள அட்டை (ஆதார் / வாக்காளர் அட்டை) *",
            AppLanguage.KANNADA to "ಸರ್ಕಾರಿ ಗುರುತಿನ ಚೀಟಿ (ಆಧಾರ್ / ವೋಟರ್ ಐಡಿ) *",
            AppLanguage.MALAYALAM to "സർക്കാർ തിരിച്ചറിയൽ കാർഡ് (ആധാർ / വോട്ടർ ഐഡി) *",
            AppLanguage.MARATHI to "सरकारी ओळखपत्र (आधार / मतदार ओळखपत्र) *",
            AppLanguage.BENGALI to "সরকারি পরিচয়পত্র (আধার / ভোটার আইডি) *",
            AppLanguage.GUJARATI to "સરકારી ઓળખપત્ર (આધાર / મતદાર કાર્ડ) *",
            AppLanguage.PUNJABI to "ਸਰਕਾਰੀ ਪਛਾਣ ਪੱਤਰ (ਆਧਾਰ / ਵੋਟਰ ਆਈਡੀ) *",
            AppLanguage.ODIA to "ସରକାରୀ ପରିଚୟ ପତ୍ର (ଆଧାର / ଭୋଟର ଆଇଡି) *"
        ),
        "reg_address" to mapOf(
            AppLanguage.ENGLISH to "Residential / Shop Address *",
            AppLanguage.TELUGU to "నివాస / దుకాణ చిరునామా *",
            AppLanguage.HINDI to "निवास / दुकान का पता *",
            AppLanguage.TAMIL to "முகவரி / கடை இடம் *",
            AppLanguage.KANNADA to "ನಿವಾಸ / ಅಂಗಡಿ ವಿಳಾಸ *",
            AppLanguage.MALAYALAM to "വിലാസം / കട സ്ഥിതി ചെയ്യുന്ന സ്ഥലം *",
            AppLanguage.MARATHI to "राहण्याचा / दुकानाचा पत्ता *",
            AppLanguage.BENGALI to "ঠিকানা / দোকানের অবস্থান *",
            AppLanguage.GUJARATI to "સરનામું / દુકાનનું સ્થાન *",
            AppLanguage.PUNJABI to "ਰਿਹਾਇਸ਼ੀ / ਦੁਕਾਨ ਦਾ ਪਤਾ *",
            AppLanguage.ODIA to "ଠିକଣା / ଦୋକାନ ସ୍ଥାନ *"
        ),
        "reg_city" to mapOf(
            AppLanguage.ENGLISH to "City",
            AppLanguage.TELUGU to "నగరం",
            AppLanguage.HINDI to "शहर",
            AppLanguage.TAMIL to "நகரம்",
            AppLanguage.KANNADA to "ನಗರ",
            AppLanguage.MALAYALAM to "നഗരം",
            AppLanguage.MARATHI to "शहर",
            AppLanguage.BENGALI to "শহর",
            AppLanguage.GUJARATI to "શહેર",
            AppLanguage.PUNJABI to "ਸ਼ਹਿਰ",
            AppLanguage.ODIA to "ସହର"
        ),
        "reg_pincode" to mapOf(
            AppLanguage.ENGLISH to "Pincode",
            AppLanguage.TELUGU to "పిన్‌కోడ్",
            AppLanguage.HINDI to "पिन कोड",
            AppLanguage.TAMIL to "அஞ்சல் குறியீடு",
            AppLanguage.KANNADA to "ಪಿನ್‌ಕೋಡ್",
            AppLanguage.MALAYALAM to "പിൻകോഡ്",
            AppLanguage.MARATHI to "पिनकोड",
            AppLanguage.BENGALI to "পিনকোড",
            AppLanguage.GUJARATI to "પિનકોડ",
            AppLanguage.PUNJABI to "ਪਿੰਨ ਕੋਡ",
            AppLanguage.ODIA to "ପିନକୋଡ୍"
        ),
        "reg_preferred_area" to mapOf(
            AppLanguage.ENGLISH to "Preferred Collection Area",
            AppLanguage.TELUGU to "ఇష్టపడే సేకరణ ప్రాంతం",
            AppLanguage.HINDI to "पसंदीदा संग्रह क्षेत्र",
            AppLanguage.TAMIL to "விருப்பமான சேகரிப்பு பகுதி",
            AppLanguage.KANNADA to "ಆದ್ಯತೆಯ ಸಂಗ್ರಹ ಪ್ರದೇಶ",
            AppLanguage.MALAYALAM to "താൽപ്പര്യമുള്ള ശേഖരണ പ്രദേശം",
            AppLanguage.MARATHI to "पसंतीचे संकलन क्षेत्र",
            AppLanguage.BENGALI to "পছন্দের সংগ্রহের এলাকা",
            AppLanguage.GUJARATI to "પસંદગીનું કલેક્શન ક્ષેત્ર",
            AppLanguage.PUNJABI to "ਤਰਜੀਹੀ ਸੰਗ੍ਰਹਿ ਖੇਤਰ",
            AppLanguage.ODIA to "ପସନ୍ଦର ସଂଗ୍ରହ ଅଞ୍ଚଳ"
        ),
        "reg_spoken_languages" to mapOf(
            AppLanguage.ENGLISH to "Languages Known",
            AppLanguage.TELUGU to "తెలిసిన భాషలు",
            AppLanguage.HINDI to "ज्ञात भाषाएँ",
            AppLanguage.TAMIL to "தெரிந்த மொழிகள்",
            AppLanguage.KANNADA to "ತಿಳಿದಿರುವ ಭಾಷೆಗಳು",
            AppLanguage.MALAYALAM to "അറിയാവുന്ന ഭാഷകൾ",
            AppLanguage.MARATHI to "माहित असलेल्या भाषा",
            AppLanguage.BENGALI to "জানা ভাষা",
            AppLanguage.GUJARATI to "જાણીતી ભાષાઓ",
            AppLanguage.PUNJABI to "ਜਾਣੀਆਂ ਭਾਸ਼ਾਵਾਂ",
            AppLanguage.ODIA to "ଜଣାଥିବା ଭାଷା"
        ),
        "reg_submit_collector" to mapOf(
            AppLanguage.ENGLISH to "Submit Collector Registration",
            AppLanguage.TELUGU to "సేకర్త నమోదును సమర్పించండి",
            AppLanguage.HINDI to "संग्राहक पंजीकरण जमा करें",
            AppLanguage.TAMIL to "சேகரிப்பாளர் பதிவைச் சமர்ப்பிக்கவும்",
            AppLanguage.KANNADA to "ಸಂಗ್ರಾಹಕ ನೋಂದಣಿಯನ್ನು ಸಲ್ಲಿಸಿ",
            AppLanguage.MALAYALAM to "കളക്ടർ രജിസ്ട്രേഷൻ സമർപ്പിക്കുക",
            AppLanguage.MARATHI to "गोळा करणारा नोंदणी सबमिट करा",
            AppLanguage.BENGALI to "সংগ্রাহক নিবন্ধন জমা দিন",
            AppLanguage.GUJARATI to "કલેક્ટર નોંધણી સબમિટ કરો",
            AppLanguage.PUNJABI to "ਕੁਲੈਕਟਰ ਰਜਿਸਟ੍ਰੇਸ਼ਨ ਜਮ੍ਹਾ ਕਰੋ",
            AppLanguage.ODIA to "ସଂଗ୍ରାହକ ପଞ୍ଜୀକରଣ ଦାଖଲ କରନ୍ତୁ"
        ),
        "reg_auth_verification_title" to mapOf(
            AppLanguage.ENGLISH to "Pending Authorization Verification",
            AppLanguage.TELUGU to "అధీకృత ధృవీకరణ పెండింగ్‌లో ఉంది",
            AppLanguage.HINDI to "प्राधिकरण सत्यापन लंबित है",
            AppLanguage.TAMIL to "அங்கீகார சரிபார்ப்பு நிலுவையில் உள்ளது",
            AppLanguage.KANNADA to "ಪ್ರಾಧಿಕಾರ ಪರಿಶೀಲನೆ ಬಾಕಿಯಿದೆ",
            AppLanguage.MALAYALAM to "അംഗീകാര പരിശോധന തീർപ്പുകൽപ്പിച്ചിട്ടില്ല",
            AppLanguage.MARATHI to "अधिकृतता पडताळणी प्रलंबित",
            AppLanguage.BENGALI to "অনুমোদন যাচাই মুলতুবি রয়েছে",
            AppLanguage.GUJARATI to "અધિકૃતતા ચકાસણી બાકી છે",
            AppLanguage.PUNJABI to "ਅਧਿਕਾਰ ਤਸਦੀਕ ਬਕਾਇਆ",
            AppLanguage.ODIA to "ଅଧିକୃତ ଯାଞ୍ଚ ବାକି ଅଛି"
        ),
        "reg_auth_verification_desc" to mapOf(
            AppLanguage.ENGLISH to "The administrator must verify the uploaded government authorization certificate before activating your recycler account.",
            AppLanguage.TELUGU to "మీ రీసైక్లర్ ఖాతాను సక్రియం చేయడానికి ముందు నిర్వాహకుడు అప్‌లోడ్ చేసిన ప్రభుత్వ లైసెన్స్‌ను ధృవీకరించాలి.",
            AppLanguage.HINDI to "आपके रीसाइक्लर खाते को सक्रिय करने से पहले व्यवस्थापक को अपलोड किए गए सरकारी प्राधिकरण प्रमाणपत्र को सत्यापित करना होगा।",
            AppLanguage.TAMIL to "உங்கள் மறுசுழற்சி கணக்கைச் செயல்படுத்தும் முன் நிர்வாகி பதிவேற்றிய அரசாங்க உரிமத்தைச் சரிபார்க்க வேண்டும்.",
            AppLanguage.KANNADA to "ಖಾತೆಯನ್ನು ಸಕ್ರಿಯಗೊಳಿಸುವ ಮೊದಲು ನಿರ್ವಾಹಕರು ಅಪ್‌ಲೋಡ್ ಮಾಡಿದ ಸರ್ಕಾರಿ ಪರವಾನಗಿಯನ್ನು ಪರಿಶೀಲಿಸಬೇಕು.",
            AppLanguage.MALAYALAM to "അക്കൗണ്ട് സജീവമാക്കുന്നതിന് മുമ്പ് അഡ്മിൻ സർക്കാർ ലൈസൻസ് പരിശോധിക്കേണ്ടതുണ്ട്.",
            AppLanguage.MARATHI to "आपले रिसायकलर खाते सक्रिय करण्यापूर्वी प्रशासकाने अपलोड केलेले सरकारी प्रमाणपत्र तपासणे आवश्यक आहे.",
            AppLanguage.BENGALI to "অ্যাকাউন্ট সক্রিয় করার আগে অ্যাডমিনকে সরকারি লাইসেন্স যাচাই করতে হবে।",
            AppLanguage.GUJARATI to "ખાતું સક્રિય કરતાં પહેલાં એડમિન દ્વારા સરકારી પ્રમાણપત્ર ચકાસવું આવશ્યક છે.",
            AppLanguage.PUNJABI to "ਖਾਤਾ ਸਰਗਰਮ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ ਐਡਮਿਨ ਦੁਆਰਾ ਸਰਕਾਰੀ ਲਾਇਸੰਸ ਦੀ ਪੁਸ਼ਟੀ ਕਰਨੀ ਜ਼ਰੂਰੀ ਹੈ।",
            AppLanguage.ODIA to "ଖାତା ସକ୍ରିୟ କରିବା ପୂର୍ବରୁ ଆଡମିନ୍ ସରକାରୀ ଲାଇସେନ୍ସ ଯାଞ୍ଚ କରିବା ଆବଶ୍ୟକ।"
        ),
        "reg_recycler_title" to mapOf(
            AppLanguage.ENGLISH to "Authorized Recycler Setup",
            AppLanguage.TELUGU to "అధీకృత రీసైక్లర్ నమోదు",
            AppLanguage.HINDI to "अधिकृत रीसाइक्लर सेटअप",
            AppLanguage.TAMIL to "அங்கீகரிக்கப்பட்ட மறுசுழற்சி அமைப்பு",
            AppLanguage.KANNADA to "ಅಧಿಕೃತ ಮರುಬಳಕೆದಾರ ಸೆಟಪ್",
            AppLanguage.MALAYALAM to "അംഗീകൃത റീസൈക്ലർ സജ്ജീകരണം",
            AppLanguage.MARATHI to "अधिकृत रिसायकलर नोंदणी",
            AppLanguage.BENGALI to "অনুমোদিত রিসাইক্লার সেটআপ",
            AppLanguage.GUJARATI to "અધિકૃત રિસાયકલર સેટઅપ",
            AppLanguage.PUNJABI to "ਅਧਿਕਾਰਤ ਰੀਸਾਈਕਲਰ ਸੈੱਟਅੱਪ",
            AppLanguage.ODIA to "ଅଧିକୃତ ରିସାଇକ୍ଲର ସେଟଅପ୍"
        ),
        "reg_recycler_desc" to mapOf(
            AppLanguage.ENGLISH to "Upload CPCB/SPCB authorization license details for compliance approval.",
            AppLanguage.TELUGU to "నిబంధనల ఆమోదం కోసం CPCB/SPCB అధికార లైసెన్స్ వివరాలను సమర్పించండి.",
            AppLanguage.HINDI to "अनुपालन अनुमोदन के लिए सीपीसीबी/एसपीसीबी प्राधिकरण लाइसेंस विवरण अपलोड करें।",
            AppLanguage.TAMIL to "ஒப்புதலுக்காக CPCB/SPCB அங்கீகார உரிம விவரங்களை உள்ளிடவும்.",
            AppLanguage.KANNADA to "ಅನುಮೋದನೆಗಾಗಿ CPCB/SPCB ಪರವಾನಗಿ ವಿವರಗಳನ್ನು ಅಪ್‌ಲೋಡ್ ಮಾಡಿ.",
            AppLanguage.MALAYALAM to "അംഗീകാരത്തിനായി CPCB/SPCB ലൈസൻസ് വിവരങ്ങൾ നൽകുക.",
            AppLanguage.MARATHI to "प्रदूषण नियंत्रण मंडळ (CPCB/SPCB) परवाना तपशील सबमिट करा.",
            AppLanguage.BENGALI to "অনুমোদনের জন্য CPCB/SPCB লাইসেন্সের বিবরণ জমা দিন।",
            AppLanguage.GUJARATI to "મંજૂરી માટે CPCB/SPCB લાયસન્સ વિગતો અપલોડ કરો.",
            AppLanguage.PUNJABI to "ਮਨਜ਼ੂਰੀ ਲਈ CPCB/SPCB ਲਾਇਸੈਂਸ ਦੇ ਵੇਰਵੇ ਅੱਪਲੋਡ ਕਰੋ।",
            AppLanguage.ODIA to "ଅନୁମୋଦନ ପାଇଁ CPCB/SPCB ଲାଇସେନ୍ସ ବିବରଣୀ ପ୍ରଦାନ କରନ୍ତୁ।"
        ),
        "reg_voice_recycler" to mapOf(
            AppLanguage.ENGLISH to "Authorized recycler registration. Enter company name, authorized person, pollution control board certificate number, and processing capacity.",
            AppLanguage.TELUGU to "అధీకృత రీసైక్లర్ రిజిస్ట్రేషన్. కంపెనీ పేరు, అధికారి పేరు, కాలుష్య నియంత్రణ మండలి లైసెన్స్ నంబర్ మరియు ప్రాసెసింగ్ సామర్థ్యాన్ని నమోదు చేయండి.",
            AppLanguage.HINDI to "अधिकृत रीसाइक्लर पंजीकरण। कंपनी का नाम, अधिकृत व्यक्ति, प्रदूषण नियंत्रण बोर्ड प्रमाणपत्र संख्या और प्रसंस्करण क्षमता दर्ज करें।",
            AppLanguage.TAMIL to "அங்கீகரிக்கப்பட்ட மறுசுழற்சி பதிவு. நிறுவனத்தின் பெயர், சான்றிதழ் எண் மற்றும் செயலாக்க திறனை உள்ளிடவும்.",
            AppLanguage.KANNADA to "ಅಧಿಕೃತ ಮರುಬಳಕೆದಾರ ನೋಂದಣಿ. ಕಂಪನಿ ಹೆಸರು, ಪರವಾನಗಿ ಸಂಖ್ಯೆ ಮತ್ತು ಸಂಸ್ಕರಣಾ ಸಾಮರ್ಥ್ಯವನ್ನು ನಮೂದಿಸಿ.",
            AppLanguage.MALAYALAM to "റീസൈക്ലർ രജിസ്ട്രേഷൻ. കമ്പനിയുടെ പേര്, സർട്ടിഫിക്കറ്റ് നമ്പർ, ശേഷി എന്നിവ രേഖപ്പെടുത്തുക.",
            AppLanguage.MARATHI to "अधिकृत रिसायकलर नोंदणी. कंपनीचे नाव, अधिकृत व्यक्ती, प्रदूषण नियंत्रण मंडळ प्रमाणपत्र क्रमांक आणि प्रक्रिया क्षमता प्रविष्ट करा.",
            AppLanguage.BENGALI to "অনুমোদিত রিসাইক্লার নিবন্ধন। কোম্পানির নাম, সার্টিফিকেট নম্বর এবং ক্ষমতা লিখুন।",
            AppLanguage.GUJARATI to "અધિકૃત રિસાયકલર નોંધણી. કંપનીનું નામ, સર્ટિફિકેટ નંબર અને ક્ષમતા દાખલ કરો.",
            AppLanguage.PUNJABI to "ਅਧਿਕਾਰਤ ਰੀਸਾਈਕਲਰ ਰਜਿਸਟ੍ਰੇਸ਼ਨ। ਕੰਪਨੀ ਦਾ ਨਾਮ, ਸਰਟੀਫਿਕੇਟ ਨੰਬਰ ਅਤੇ ਸਮਰੱਥਾ ਦਰਜ ਕਰੋ।",
            AppLanguage.ODIA to "ରିସାଇକ୍ଲର ପଞ୍ଜୀକରଣ। କମ୍ପାନୀ ନାମ, ପ୍ରମାଣପତ୍ର ନମ୍ବର ଏବଂ କ୍ଷମତା ପ୍ରବେଶ କରନ୍ତୁ।"
        ),
        "reg_business_name" to mapOf(
            AppLanguage.ENGLISH to "Company / Recycling Facility Name *",
            AppLanguage.TELUGU to "కంపెనీ / రీసైక్లింగ్ యూనిట్ పేరు *",
            AppLanguage.HINDI to "कंपनी / रीसाइक्लिंग प्लांट का नाम *",
            AppLanguage.TAMIL to "நிறுவனம் / மறுசுழற்சி ஆலை பெயர் *",
            AppLanguage.KANNADA to "ಕಂಪನಿ / ಮರುಬಳಕೆ ಘಟಕದ ಹೆಸರು *",
            AppLanguage.MALAYALAM to "കമ്പനി / റീസൈക്ലിംഗ് പ്ലാന്റ് പേര് *",
            AppLanguage.MARATHI to "कंपनी / रिसायकलिंग प्लांटचे नाव *",
            AppLanguage.BENGALI to "কোম্পানি / রিসাইক্লিং প্ল্যান্টের নাম *",
            AppLanguage.GUJARATI to "કંપની / રિસાયકલિંગ પ્લાન્ટનું નામ *",
            AppLanguage.PUNJABI to "ਕੰਪਨੀ / ਰੀਸਾਈਕਲਿੰਗ ਪਲਾਂਟ ਦਾ ਨਾਮ *",
            AppLanguage.ODIA to "କମ୍ପାନୀ / ରିସାଇକ୍ଲିଂ ପ୍ଲାଣ୍ଟ ନାମ *"
        ),
        "reg_authorized_person" to mapOf(
            AppLanguage.ENGLISH to "Authorized Person's Name *",
            AppLanguage.TELUGU to "అధీకృత వ్యక్తి పేరు *",
            AppLanguage.HINDI to "अधिकृत व्यक्ति का नाम *",
            AppLanguage.TAMIL to "அங்கீகரிக்கப்பட்ட நபரின் பெயர் *",
            AppLanguage.KANNADA to "ಅಧಿಕೃತ ವ್ಯಕ್ತಿಯ ಹೆಸರು *",
            AppLanguage.MALAYALAM to "അധികാരപ്പെടുത്തിയ വ്യക്തിയുടെ പേര് *",
            AppLanguage.MARATHI to "अधिकृत व्यक्तीचे नाव *",
            AppLanguage.BENGALI to "অনুমোদিত ব্যক্তির নাম *",
            AppLanguage.GUJARATI to "અધિકૃત વ્યક્તિનું નામ *",
            AppLanguage.PUNJABI to "ਅਧਿਕਾਰਤ ਵਿਅਕਤੀ ਦਾ ਨਾਮ *",
            AppLanguage.ODIA to "ଅଧିକୃତ ବ୍ୟକ୍ତିଙ୍କ ନାମ *"
        ),
        "reg_contact_phone" to mapOf(
            AppLanguage.ENGLISH to "Contact Phone *",
            AppLanguage.TELUGU to "సంప్రదింపు ఫోన్ *",
            AppLanguage.HINDI to "संपर्क फ़ोन *",
            AppLanguage.TAMIL to "தொடர்பு எண் *",
            AppLanguage.KANNADA to "ಸಂಪರ್ಕ ಫೋನ್ *",
            AppLanguage.MALAYALAM to "ബന്ധപ്പെടേണ്ട നമ്പർ *",
            AppLanguage.MARATHI to "संपर्क फोन *",
            AppLanguage.BENGALI to "যোগাযোগ ফোন *",
            AppLanguage.GUJARATI to "સંપર્ક ફોન *",
            AppLanguage.PUNJABI to "ਸੰਪਰਕ ਫ਼ੋਨ *",
            AppLanguage.ODIA to "ଯୋଗାଯୋଗ ଫୋନ୍ *"
        ),
        "reg_cpcb_license" to mapOf(
            AppLanguage.ENGLISH to "Govt Authorization / Cert Number *",
            AppLanguage.TELUGU to "ప్రభుత్వ అధికార / లైసెన్స్ నంబర్ *",
            AppLanguage.HINDI to "सरकारी प्राधिकरण / प्रमाणपत्र संख्या *",
            AppLanguage.TAMIL to "அரசு அங்கீகார சான்றிதழ் எண் *",
            AppLanguage.KANNADA to "ಸರ್ಕಾರಿ ಪ್ರಮಾಣಪತ್ರ ಸಂಖ್ಯೆ *",
            AppLanguage.MALAYALAM to "സർക്കാർ സർട്ടിഫിക്കറ്റ് നമ്പർ *",
            AppLanguage.MARATHI to "शासकीय परवाना / प्रमाणपत्र क्रमांक *",
            AppLanguage.BENGALI to "সরকারি লাইসেন্স / সার্টিফিকেট নম্বর *",
            AppLanguage.GUJARATI to "સરકારી સર્ટિફિકેટ નંબર *",
            AppLanguage.PUNJABI to "ਸਰਕਾਰੀ ਲਾਇਸੰਸ ਨੰਬਰ *",
            AppLanguage.ODIA to "ସରକାରୀ ପ୍ରମାଣପତ୍ର ନମ୍ବର *"
        ),
        "reg_cert_authority" to mapOf(
            AppLanguage.ENGLISH to "Issuing Authority (e.g. CPCB / MPCB)",
            AppLanguage.TELUGU to "జారీ చేసిన ప్రాధికార సంస్థ (ఉదా. CPCB / SPCB)",
            AppLanguage.HINDI to "जारीकर्ता प्राधिकरण (उदा. सीपीसीबी / एसपीसीबी)",
            AppLanguage.TAMIL to "வழங்கிய ஆணையம் (CPCB / TNPCB)",
            AppLanguage.KANNADA to "ನೀಡಿದ ಪ್ರಾಧಿಕಾರ (CPCB / KSPCB)",
            AppLanguage.MALAYALAM to "നൽകിയ അതോറിറ്റി (CPCB / PCB)",
            AppLanguage.MARATHI to "देणारा प्राधिकरण (उदा. CPCB / MPCB)",
            AppLanguage.BENGALI to "প্রদানকারী কর্তৃপক্ষ (CPCB / PCB)",
            AppLanguage.GUJARATI to "જારી કરનાર સત્તામંડળ (CPCB / GPCB)",
            AppLanguage.PUNJABI to "ਜਾਰੀ ਕਰਨ ਵਾਲੀ ਅਥਾਰਟੀ (CPCB / PPCB)",
            AppLanguage.ODIA to "ପ୍ରଦାନକାରୀ କର୍ତ୍ତୃପକ୍ଷ (CPCB / SPCB)"
        ),
        "reg_issue_date" to mapOf(
            AppLanguage.ENGLISH to "Issue Date",
            AppLanguage.TELUGU to "జారీ తేదీ",
            AppLanguage.HINDI to "जारी करने की तारीख",
            AppLanguage.TAMIL to "வழங்கப்பட்ட தேதி",
            AppLanguage.KANNADA to "ನೀಡಿದ ದಿನಾಂಕ",
            AppLanguage.MALAYALAM to "നൽകിയ തീയതി",
            AppLanguage.MARATHI to "जारी केलेली तारीख",
            AppLanguage.BENGALI to "ইস্যু তারিখ",
            AppLanguage.GUJARATI to "જારી તારીખ",
            AppLanguage.PUNJABI to "ਜਾਰੀ ਕਰਨ ਦੀ ਮਿਤੀ",
            AppLanguage.ODIA to "ପ୍ରଦାନ ତାରିଖ"
        ),
        "reg_expiry_date" to mapOf(
            AppLanguage.ENGLISH to "Expiry Date",
            AppLanguage.TELUGU to "గడువు ముగింపు తేదీ",
            AppLanguage.HINDI to "समाप्ति तिथि",
            AppLanguage.TAMIL to "காலாவதி தேதி",
            AppLanguage.KANNADA to "ಅಂತ್ಯಗೊಳ್ಳುವ ದಿನಾಂಕ",
            AppLanguage.MALAYALAM to "കാലഹരണപ്പെടുന്ന തീയതി",
            AppLanguage.MARATHI to "समाप्ती तारीख",
            AppLanguage.BENGALI to "মেয়াদ শেষ",
            AppLanguage.GUJARATI to "સમાપ્તિ તારીખ",
            AppLanguage.PUNJABI to "ਮਿਆਦ ਪੁੱਗਣ ਦੀ ਮਿਤੀ",
            AppLanguage.ODIA to "ସମାପ୍ତି ତାରିଖ"
        ),
        "reg_accepted_categories" to mapOf(
            AppLanguage.ENGLISH to "Accepted E-Waste Categories",
            AppLanguage.TELUGU to "స్వీకరించే ఇ-వ్యర్థాల వర్గాలు",
            AppLanguage.HINDI to "स्वीकृत ई-कचरा श्रेणियां",
            AppLanguage.TAMIL to "ஏற்றுக்கொள்ளப்பட்ட மின்-கழிவு வகைகள்",
            AppLanguage.KANNADA to "ಸ್ವೀಕರಿಸುವ ಇ-ತ್ಯಾಜ್ಯ ವರ್ಗಗಳು",
            AppLanguage.MALAYALAM to "സ്വീകരിക്കുന്ന ഇ-വേസ്റ്റ് വിഭാഗങ്ങൾ",
            AppLanguage.MARATHI to "स्वीकारले जाणारे ई-कचरा प्रकार",
            AppLanguage.BENGALI to "গৃহীত ই-বর্জ্য বিভাগ",
            AppLanguage.GUJARATI to "સ્વીકાર્ય ઈ-કચરા શ્રેણીઓ",
            AppLanguage.PUNJABI to "ਸਵੀਕਾਰ ਕੀਤੀਆਂ ਈ-ਕੂੜਾ ਸ਼੍ਰੇਣੀਆਂ",
            AppLanguage.ODIA to "ଗୃହୀତ ଇ-ବର୍ଜ୍ୟ ବର୍ଗ"
        ),
        "reg_daily_capacity" to mapOf(
            AppLanguage.ENGLISH to "Daily Capacity (kg)",
            AppLanguage.TELUGU to "రోజువారీ సామర్థ్యం (కేజీలు)",
            AppLanguage.HINDI to "दैनिक क्षमता (किग्रा)",
            AppLanguage.TAMIL to "தினசரி திறன் (கிலோ)",
            AppLanguage.KANNADA to "ದೈನಂದಿನ ಸಾಮರ್ಥ್ಯ (ಕೆಜಿ)",
            AppLanguage.MALAYALAM to "പ്രതിദിന ശേഷി (കിലോ)",
            AppLanguage.MARATHI to "दैनिक क्षमता (किलो)",
            AppLanguage.BENGALI to "দৈনিক ক্ষমতা (কেজি)",
            AppLanguage.GUJARATI to "દૈનિક ક્ષમતા (કિગ્રા)",
            AppLanguage.PUNJABI to "ਰੋਜ਼ਾਨਾ ਸਮਰੱਥਾ (ਕਿਲੋ)",
            AppLanguage.ODIA to "ଦୈନିକ କ୍ଷମତା (କିଲୋ)"
        ),
        "reg_min_lot_qty" to mapOf(
            AppLanguage.ENGLISH to "Min Lot Qty (kg)",
            AppLanguage.TELUGU to "కనీస లాట్ పరిమాణం (కేజీలు)",
            AppLanguage.HINDI to "न्यूनतम लॉट मात्रा (किग्रा)",
            AppLanguage.TAMIL to "குறைந்தபட்ச அளவு (கிலோ)",
            AppLanguage.KANNADA to "ಕನಿಷ್ಠ ಪ್ರಮಾಣ (ಕೆಜಿ)",
            AppLanguage.MALAYALAM to "കുറഞ്ഞ അളവ് (കിലോ)",
            AppLanguage.MARATHI to "किमान लॉट प्रमाण (किलो)",
            AppLanguage.BENGALI to "সর্বনিম্ন পরিমাণ (কেজি)",
            AppLanguage.GUJARATI to "ન્યૂનતમ જથ્થો (કિગ્રા)",
            AppLanguage.PUNJABI to "ਘੱਟੋ-ਘੱਟ ਮਾਤਰਾ (ਕਿਲੋ)",
            AppLanguage.ODIA to "ସର୍ବନିମ୍ନ ପରିମାଣ (କିଲୋ)"
        ),
        "reg_submit_recycler" to mapOf(
            AppLanguage.ENGLISH to "Submit for Authorization Verification",
            AppLanguage.TELUGU to "అధీకృత ధృవీకరణ కోసం సమర్పించండి",
            AppLanguage.HINDI to "प्राधिकरण सत्यापन के लिए जमा करें",
            AppLanguage.TAMIL to "சரிபார்ப்புக்கு சமர்ப்பிக்கவும்",
            AppLanguage.KANNADA to "ಪರಿಶೀಲನೆಗಾಗಿ ಸಲ್ಲಿಸಿ",
            AppLanguage.MALAYALAM to "പരിശോധനയ്ക്കായി സമർപ്പിക്കുക",
            AppLanguage.MARATHI to "पडताळणीसाठी सबमिट करा",
            AppLanguage.BENGALI to "যাচাইয়ের জন্য জমা দিন",
            AppLanguage.GUJARATI to "ચકાસણી માટે સબમિટ કરો",
            AppLanguage.PUNJABI to "ਤਸਦੀਕ ਲਈ ਜਮ੍ਹਾ ਕਰੋ",
            AppLanguage.ODIA to "ଯାଞ୍ଚ ପାଇଁ ଦାଖଲ କରନ୍ତୁ"
        ),
        "forgot_password_title" to mapOf(
            AppLanguage.ENGLISH to "Reset Password",
            AppLanguage.TELUGU to "పాస్‌వర్డ్ రీసెట్ చేయండి",
            AppLanguage.HINDI to "पासवर्ड रीसेट करें",
            AppLanguage.TAMIL to "கடவுச்சொல்லை மீட்டமை",
            AppLanguage.KANNADA to "ಪಾಸ್‌ವರ್ಡ್ ಮರುಹೊಂದಿಸಿ",
            AppLanguage.MALAYALAM to "പാസ്‌വേഡ് പുനഃസജ്ജമാക്കുക",
            AppLanguage.MARATHI to "पासवर्ड रीसेट करा",
            AppLanguage.BENGALI to "পাসওয়ার্ড রিসেট করুন",
            AppLanguage.GUJARATI to "પાસવર્ડ રીસેટ કરો",
            AppLanguage.PUNJABI to "ਪਾਸਵਰਡ ਰੀਸੈੱਟ ਕਰੋ",
            AppLanguage.ODIA to "ପାସୱାର୍ଡ ରିସେଟ୍ କରନ୍ତୁ"
        ),
        "forgot_password_desc" to mapOf(
            AppLanguage.ENGLISH to "Verify your registered phone or username to reset your access credentials.",
            AppLanguage.TELUGU to "మీ ఆధారాలను రీసెట్ చేయడానికి మీ నమోదు చేసుకున్న ఫోన్ లేదా వినియోగదారు పేరును ధృవీకరించండి.",
            AppLanguage.HINDI to "अपनी साख रीसेट करने के लिए अपने पंजीकृत फोन या उपयोगकर्ता नाम को सत्यापित करें।",
            AppLanguage.TAMIL to "கடவுச்சொல்லை மீட்டமைக்க பதிவுசெய்யப்பட்ட தொலைபேசி எண்ணை உள்ளிடவும்.",
            AppLanguage.KANNADA to "ಮರುಹೊಂದಿಸಲು ನಿಮ್ಮ ನೋಂದಾಯಿತ ಫೋನ್ ಅಥವಾ ಬಳಕೆದಾರ ಹೆಸರನ್ನು ಪರಿಶೀಲಿಸಿ.",
            AppLanguage.MALAYALAM to "പാസ്‌വേഡ് മാറ്റാൻ രജിസ്റ്റർ ചെയ്ത ഫോൺ നമ്പർ നൽകുക.",
            AppLanguage.MARATHI to "आपला पासवर्ड रीसेट करण्यासाठी नोंदणीकृत फोन किंवा वापरकर्तानाव तपासा.",
            AppLanguage.BENGALI to "পাসওয়ার্ড রিসেট করতে নিবন্ধিত ফোন বা ব্যবহারকারীর নাম যাচাই করুন।",
            AppLanguage.GUJARATI to "પાસવર્ડ રીસેટ કરવા માટે તમારો રજિસ્ટર્ડ ફોન નંબર ચકાસો.",
            AppLanguage.PUNJABI to "ਪਾਸਵਰਡ ਰੀਸੈੱਟ ਕਰਨ ਲਈ ਰਜਿਸਟਰਡ ਫ਼ੋਨ ਨੰਬਰ ਦੀ ਜਾਂਚ ਕਰੋ।",
            AppLanguage.ODIA to "ପାସୱାର୍ଡ ରିସେଟ୍ କରିବାକୁ ପଞ୍ଜୀକୃତ ଫୋନ୍ ନମ୍ବର ଯାଞ୍ଚ କରନ୍ତୁ।"
        ),
        "send_otp" to mapOf(
            AppLanguage.ENGLISH to "Send Verification OTP",
            AppLanguage.TELUGU to "ధృవీకరణ OTP పంపండి",
            AppLanguage.HINDI to "सत्यापन ओटीपी भेजें",
            AppLanguage.TAMIL to "OTP அனுப்பவும்",
            AppLanguage.KANNADA to "ಪರಿಶೀಲನಾ OTP ಕಳುಹಿಸಿ",
            AppLanguage.MALAYALAM to "OTP അയയ്ക്കുക",
            AppLanguage.MARATHI to "ओटीपी (OTP) पाठवा",
            AppLanguage.BENGALI to "ওটিপি পাঠান",
            AppLanguage.GUJARATI to "ઓટીપી મોકલો",
            AppLanguage.PUNJABI to "ਓਟੀਪੀ ਭੇਜੋ",
            AppLanguage.ODIA to "ଓଟିପି ପଠାନ୍ତୁ"
        ),
        "otp_sent_info" to mapOf(
            AppLanguage.ENGLISH to "OTP sent to registered mobile. Verification Code: 7829",
            AppLanguage.TELUGU to "నమోదిత మొబైల్‌కు OTP పంపబడింది. కోడ్: 7829",
            AppLanguage.HINDI to "पंजीकृत मोबाइल पर ओटीपी भेजा गया। कोड: 7829",
            AppLanguage.TAMIL to "OTP மொபைலுக்கு அனுப்பப்பட்டது. குறியீடு: 7829",
            AppLanguage.KANNADA to "ಮೊಬೈಲ್‌ಗೆ OTP ಕಳುಹಿಸಲಾಗಿದೆ. ಕೋಡ್: 7829",
            AppLanguage.MALAYALAM to "രജിസ്റ്റർ ചെയ്ത മൊബൈലിലേക്ക് ഒടിപി അയച്ചു. കോഡ്: 7829",
            AppLanguage.MARATHI to "नोंदणीकृत मोबाइलवर ओटीपी पाठवला. कोड: 7829",
            AppLanguage.BENGALI to "ওটিপি পাঠানো হয়েছে। কোড: 7829",
            AppLanguage.GUJARATI to "ઓટીપી મોકલાયો છે. કોડ: 7829",
            AppLanguage.PUNJABI to "ਓਟੀਪੀ ਭੇਜਿਆ ਗਿਆ। ਕੋਡ: 7829",
            AppLanguage.ODIA to "ଓଟିପି ପଠାଗଲା। କୋଡ୍: 7829"
        ),
        "otp_label" to mapOf(
            AppLanguage.ENGLISH to "4-digit OTP",
            AppLanguage.TELUGU to "4-అంకెల OTP",
            AppLanguage.HINDI to "4-अंकीय ओटीपी",
            AppLanguage.TAMIL to "4-இலக்க OTP",
            AppLanguage.KANNADA to "4-ಅಂಕಿಯ OTP",
            AppLanguage.MALAYALAM to "4-അക്ക ഒടിപി",
            AppLanguage.MARATHI to "4-अंकी ओटीपी",
            AppLanguage.BENGALI to "৪-সংখ্যার ওটিপি",
            AppLanguage.GUJARATI to "4-અંકનો ઓટીપી",
            AppLanguage.PUNJABI to "4-ਅੰਕਾਂ ਵਾਲਾ ਓਟੀਪੀ",
            AppLanguage.ODIA to "୪-ଅଙ୍କ ବିଶିଷ୍ଟ ଓଟିପି"
        ),
        "new_password" to mapOf(
            AppLanguage.ENGLISH to "New Password",
            AppLanguage.TELUGU to "కొత్త పాస్‌వర్డ్",
            AppLanguage.HINDI to "नया पासवर्ड",
            AppLanguage.TAMIL to "புதிய கடவுச்சொல்",
            AppLanguage.KANNADA to "ಹೊಸ ಪಾಸ್‌ವರ್ಡ್",
            AppLanguage.MALAYALAM to "പുതിയ പാസ്‌വേഡ്",
            AppLanguage.MARATHI to "नवीन पासवर्ड",
            AppLanguage.BENGALI to "নতুন পাসওয়ার্ড",
            AppLanguage.GUJARATI to "નવો પાસવર્ડ",
            AppLanguage.PUNJABI to "ਨਵਾਂ ਪਾਸਵਰਡ",
            AppLanguage.ODIA to "ନୂତନ ପାସୱାର୍ଡ"
        ),
        "confirm_new_password" to mapOf(
            AppLanguage.ENGLISH to "Confirm New Password",
            AppLanguage.TELUGU to "కొత్త పాస్‌వర్డ్‌ను నిర్ధారించండి",
            AppLanguage.HINDI to "नए पासवर्ड की पुष्टि करें",
            AppLanguage.TAMIL to "புதிய கடவுச்சொல்லை உறுதிப்படுத்தவும்",
            AppLanguage.KANNADA to "ಹೊಸ ಪಾಸ್‌ವರ್ಡ್ ದೃಢೀಕರಿಸಿ",
            AppLanguage.MALAYALAM to "പുതിയ പാസ്‌വേഡ് സ്ഥിരീകരിക്കുക",
            AppLanguage.MARATHI to "नवीन पासवर्डची पुष्टी करा",
            AppLanguage.BENGALI to "নতুন পাসওয়ার্ড নিশ্চিত করুন",
            AppLanguage.GUJARATI to "નવા પાસવર્ડની પુષ્ટિ કરો",
            AppLanguage.PUNJABI to "ਨਵੇਂ ਪਾਸਵਰਡ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ",
            AppLanguage.ODIA to "ନୂତନ ପାସୱାର୍ଡ ନିଶ୍ଚିତ କରନ୍ତୁ"
        ),
        "update_password" to mapOf(
            AppLanguage.ENGLISH to "Update Password",
            AppLanguage.TELUGU to "పాస్‌వర్డ్‌ను నవీకరించండి",
            AppLanguage.HINDI to "पासवर्ड अपडेट करें",
            AppLanguage.TAMIL to "கடவுச்சொல்லைப் புதுப்பிக்கவும்",
            AppLanguage.KANNADA to "ಪಾಸ್‌ವರ್ಡ್ ನವೀಕರಿಸಿ",
            AppLanguage.MALAYALAM to "പാസ്‌വേഡ് അപ്‌ഡേറ്റ് ചെയ്യുക",
            AppLanguage.MARATHI to "पासवर्ड अपडेट करा",
            AppLanguage.BENGALI to "পাসওয়ার্ড আপডেট করুন",
            AppLanguage.GUJARATI to "પાસવર્ડ અપડેટ કરો",
            AppLanguage.PUNJABI to "ਪਾਸਵਰਡ ਅੱਪਡੇਟ ਕਰੋ",
            AppLanguage.ODIA to "ପାସୱାର୍ଡ ଅପଡେଟ୍ କରନ୍ତୁ"
        ),
        "enter_id_password_error" to mapOf(
            AppLanguage.ENGLISH to "Please enter both identifier and password",
            AppLanguage.TELUGU to "దయచేసి యూజర్‌నేమ్ మరియు పాస్‌వర్డ్ రెండింటినీ నమోదు చేయండి",
            AppLanguage.HINDI to "कृपया पहचानकर्ता और पासवर्ड दोनों दर्ज करें",
            AppLanguage.TAMIL to "அடையாளம் மற்றும் கடவுச்சொல் இரண்டையும் உள்ளிடவும்",
            AppLanguage.KANNADA to "ದಯವಿಟ್ಟು ಬಳಕೆದಾರ ಹೆಸರು ಮತ್ತು ಪಾಸ್‌ವರ್ಡ್ ಎರಡನ್ನೂ ನಮೂದಿಸಿ",
            AppLanguage.MALAYALAM to "ദയവായി ഉപയോക്തൃനാമവും പാസ്‌വേഡും രേഖപ്പെടുത്തുക",
            AppLanguage.MARATHI to "कृपया वापरकर्तानाव आणि पासवर्ड दोन्ही प्रविष्ट करा",
            AppLanguage.BENGALI to "দয়া করে ব্যবহারকারীর নাম এবং পাসওয়ার্ড উভয়ই লিখুন",
            AppLanguage.GUJARATI to "કૃપા કરીને ઓળખ અને પાસવર્ડ બંને દાખલ કરો",
            AppLanguage.PUNJABI to "ਕਿਰਪਾ ਕਰਕੇ ਯੂਜ਼ਰਨੇਮ ਅਤੇ ਪਾਸਵਰਡ ਦੋਵੇਂ ਦਰਜ ਕਰੋ",
            AppLanguage.ODIA to "ଦୟାକରି ୟୁଜରନେମ୍ ଏବଂ ପାସୱାର୍ଡ ଉଭୟ ପ୍ରବେଶ କରନ୍ତୁ"
        ),
        "val_valid_mobile" to mapOf(
            AppLanguage.ENGLISH to "Please enter a valid mobile number.",
            AppLanguage.MARATHI to "कृपया वैध मोबाईल क्रमांक प्रविष्ट करा.",
            AppLanguage.HINDI to "कृपया एक वैध मोबाइल नंबर दर्ज करें।",
            AppLanguage.TELUGU to "దయచేసి సరైన మొబైల్ నంబర్‌ను నమోదు చేయండి."
        ),
        "val_password_mismatch" to mapOf(
            AppLanguage.ENGLISH to "Passwords do not match.",
            AppLanguage.MARATHI to "पासवर्ड जुळत नाहीत.",
            AppLanguage.HINDI to "पासवर्ड मेल नहीं खाते।",
            AppLanguage.TELUGU to "పాస్‌వర్డ్‌లు సరిపోలడం లేదు."
        ),
        "val_field_required" to mapOf(
            AppLanguage.ENGLISH to "This field is required.",
            AppLanguage.MARATHI to "हे फील्ड आवश्यक आहे.",
            AppLanguage.HINDI to "यह फ़ील्ड आवश्यक है।",
            AppLanguage.TELUGU to "ఈ ఫీల్డ్ తప్పనిసరి."
        ),
        "reg_success_waiting_approval" to mapOf(
            AppLanguage.ENGLISH to "Registration submitted successfully. Your account is waiting for administrator approval.",
            AppLanguage.MARATHI to "नोंदणी यशस्वीरित्या सबमिट केली. आपले खाते प्रशासकीय मंजुरीच्या प्रतीक्षेत आहे.",
            AppLanguage.HINDI to "पंजीकरण सफलतापूर्वक जमा हो गया। आपका खाता व्यवस्थापक की मंजूरी की प्रतीक्षा कर रहा है।",
            AppLanguage.TELUGU to "నమోదు విజయవంతంగా సమర్పించబడింది. మీ ఖాతా అడ్మిన్ ఆమోదం కోసం వేచి ఉంది."
        ),
        "account_status_label" to mapOf(
            AppLanguage.ENGLISH to "Account Status",
            AppLanguage.MARATHI to "खाते स्थिती",
            AppLanguage.HINDI to "खाता स्थिति",
            AppLanguage.TELUGU to "ఖాతా స్థితి"
        ),
        "status_pending_approval" to mapOf(
            AppLanguage.ENGLISH to "PENDING_APPROVAL",
            AppLanguage.MARATHI to "मंजुरी प्रलंबित (PENDING_APPROVAL)",
            AppLanguage.HINDI to "मंजूरी लंबित (PENDING_APPROVAL)",
            AppLanguage.TELUGU to "ఆమోదం వేచి ఉంది (PENDING_APPROVAL)"
        ),
        "status_pending_verification" to mapOf(
            AppLanguage.ENGLISH to "PENDING_VERIFICATION",
            AppLanguage.MARATHI to "पडताळणी प्रलंबित (PENDING_VERIFICATION)",
            AppLanguage.HINDI to "सत्यापन लंबित (PENDING_VERIFICATION)",
            AppLanguage.TELUGU to "ధృవీకరణ వేచి ఉంది (PENDING_VERIFICATION)"
        ),
        "already_have_account" to mapOf(
            AppLanguage.ENGLISH to "Already have an account?",
            AppLanguage.MARATHI to "आधीपासून खाते आहे का?",
            AppLanguage.HINDI to "क्या आपके पास पहले से खाता है?",
            AppLanguage.TELUGU to "ఇప్పటికే ఖాతా ఉందా?"
        ),
        "dont_have_account" to mapOf(
            AppLanguage.ENGLISH to "Don't have an account?",
            AppLanguage.MARATHI to "तुमचे खाते नाही का?",
            AppLanguage.HINDI to "क्या आपके पास खाता नहीं है?",
            AppLanguage.TELUGU to "ఖాతా లేదా?"
        ),
        "register_as_collector" to mapOf(
            AppLanguage.ENGLISH to "Register as E-waste Collector",
            AppLanguage.MARATHI to "ई-कचरा संकलक म्हणून नोंदणी करा",
            AppLanguage.HINDI to "ई-कचरा संग्राहक के रूप में पंजीकरण करें",
            AppLanguage.TELUGU to "ఇ-వ్యర్థాల సేకర్తగా నమోదు చేసుకోండి"
        ),
        "register_as_recycler" to mapOf(
            AppLanguage.ENGLISH to "Register as Authorized Recycler",
            AppLanguage.MARATHI to "अधिकृत रिसायकलर म्हणून नोंदणी करा",
            AppLanguage.HINDI to "अधिकृत पुनर्चक्रणकर्ता के रूप में पंजीकरण करें",
            AppLanguage.TELUGU to "అధీకృత రీసైక్లర్‌గా నమోదు చేసుకోండి"
        ),
        "confirm_password" to mapOf(
            AppLanguage.ENGLISH to "Confirm Password *",
            AppLanguage.MARATHI to "पासवर्डची पुष्टी करा *",
            AppLanguage.HINDI to "पासवर्ड की पुष्टि करें *",
            AppLanguage.TELUGU to "పాస్‌వర్డ్‌ను నిర్ధారించండి *"
        ),
        "email_optional" to mapOf(
            AppLanguage.ENGLISH to "Email Address (Optional)",
            AppLanguage.MARATHI to "ईमेल पत्ता (पर्यायी)",
            AppLanguage.HINDI to "ईमेल पता (वैकल्पिक)",
            AppLanguage.TELUGU to "ఈమెయిల్ చిరునామా (ఐచ్ఛికం)"
        ),
        "preferred_collection_area" to mapOf(
            AppLanguage.ENGLISH to "Preferred Collection Area *",
            AppLanguage.MARATHI to "पसंतीचे संकलन क्षेत्र *",
            AppLanguage.HINDI to "पसंदीदा संग्रह क्षेत्र *",
            AppLanguage.TELUGU to "ప్రాధాన్య సేకరణ ప్రాంతం *"
        ),
        "govt_cert_upload" to mapOf(
            AppLanguage.ENGLISH to "Government Authorization / Certificate *",
            AppLanguage.MARATHI to "शासकीय अधिकृतता / प्रमाणपत्र *",
            AppLanguage.HINDI to "सरकारी प्राधिकरण / प्रमाणपत्र *",
            AppLanguage.TELUGU to "ప్రభుత్వ అధికార పత్రం / సర్టిఫికేట్ *"
        ),
        "certificate_attached" to mapOf(
            AppLanguage.ENGLISH to "Certificate Attached",
            AppLanguage.MARATHI to "प्रमाणपत्र जोडले गेले",
            AppLanguage.HINDI to "प्रमाणपत्र संलग्न",
            AppLanguage.TELUGU to "సర్టిఫికేట్ జతచేయబడింది"
        ),
        "upload_certificate_btn" to mapOf(
            AppLanguage.ENGLISH to "Attach Certificate Document / PDF / Image",
            AppLanguage.MARATHI to "प्रमाणपत्र दस्तऐवज / PDF / फोटो जोडा",
            AppLanguage.HINDI to "प्रमाणपत्र दस्तावेज़ / पीडीएफ / चित्र संलग्न करें",
            AppLanguage.TELUGU to "సర్టిఫికేట్ పత్రం / PDF / చిత్రం జతచేయండి"
        ),
        "retry" to mapOf(
            AppLanguage.ENGLISH to "Retry",
            AppLanguage.MARATHI to "पुन्हा प्रयत्न करा",
            AppLanguage.HINDI to "पुनः प्रयास करें",
            AppLanguage.TELUGU to "మళ్లీ ప్రయత్నించండి"
        ),
        "admin_compliance_title" to mapOf(
            AppLanguage.ENGLISH to "System Operations & Compliance",
            AppLanguage.MARATHI to "प्रणाली संचालन आणि अनुपालन",
            AppLanguage.HINDI to "सिस्टम संचालन और अनुपालन",
            AppLanguage.TELUGU to "సిస్టమ్ కార్యకలాపాలు మరియు వర్తింపు"
        ),
        "admin_compliance_desc" to mapOf(
            AppLanguage.ENGLISH to "Manage CPCB/SPCB authorizations, collectors, and resolve disputes",
            AppLanguage.MARATHI to "CPCB/SPCB अधिकृतता, संकलक व्यवस्थापित करा आणि वाद सोडवा",
            AppLanguage.HINDI to "CPCB/SPCB प्राधिकरण, संग्राहकों का प्रबंधन और विवाद समाधान",
            AppLanguage.TELUGU to "CPCB/SPCB అధికారాలు, సేకర్తలను నిర్వహించండి మరియు వివాదాలను పరిష్కరించండి"
        ),
        "reg_approvals_title" to mapOf(
            AppLanguage.ENGLISH to "Registration Approvals",
            AppLanguage.MARATHI to "नोंदणी मंजुरी",
            AppLanguage.HINDI to "पंजीकरण अनुमोदन",
            AppLanguage.TELUGU to "నమోదు ఆమోదాలు"
        ),
        "pending_verification_desc" to mapOf(
            AppLanguage.ENGLISH to "Collectors & Recyclers waiting for license verification",
            AppLanguage.MARATHI to "संकलक आणि रिसायकलर्स परवाना पडताळणीच्या प्रतीक्षेत",
            AppLanguage.HINDI to "संग्राहक और पुनर्चक्रणकर्ता लाइसेंस सत्यापन की प्रतीक्षा में",
            AppLanguage.TELUGU to "లైసెన్స్ ధృవీకరణ కోసం వేచి ఉన్న సేకర్తలు & రీసైక్లర్లు"
        ),
        "analytics_logs_title" to mapOf(
            AppLanguage.ENGLISH to "Analytics & Audit Logs",
            AppLanguage.MARATHI to "विश्लेषण आणि ऑडिट नोंदी",
            AppLanguage.HINDI to "एनालिटिक्स और ऑडिट लॉग",
            AppLanguage.TELUGU to "విశ్లేషణలు మరియు ఆడిట్ లాగ్‌లు"
        ),
        "analytics_logs_desc" to mapOf(
            AppLanguage.ENGLISH to "Category distribution, collection trends & compliance logs",
            AppLanguage.MARATHI to "श्रेणी वितरण, संकलन कल आणि अनुपालन नोंदी",
            AppLanguage.HINDI to "श्रेणी वितरण, संग्रह रुझान और अनुपालन लॉग",
            AppLanguage.TELUGU to "వర్గం పంపిణీ, సేకరణ పోకడలు & వర్తింపు లాగ్‌లు"
        ),
        "approve" to mapOf(
            AppLanguage.ENGLISH to "Approve",
            AppLanguage.MARATHI to "मंजूर करा",
            AppLanguage.HINDI to "स्वीकृत करें",
            AppLanguage.TELUGU to "ఆమోదించండి"
        ),
        "reject" to mapOf(
            AppLanguage.ENGLISH to "Reject",
            AppLanguage.MARATHI to "नाकारा",
            AppLanguage.HINDI to "अस्वीकार करें",
            AppLanguage.TELUGU to "తిరస్కరించండి"
        ),
        "total_scrap" to mapOf(
            AppLanguage.ENGLISH to "Total Scrap",
            AppLanguage.MARATHI to "एकूण भंगार",
            AppLanguage.HINDI to "कुल स्क्रैप",
            AppLanguage.TELUGU to "మొత్తం స్క్రాప్"
        ),
        "requests_label" to mapOf(
            AppLanguage.ENGLISH to "Requests",
            AppLanguage.MARATHI to "विनंत्या",
            AppLanguage.HINDI to "अनुरोध",
            AppLanguage.TELUGU to "అభ్యర్థనలు"
        ),
        "new_requests_count" to mapOf(
            AppLanguage.ENGLISH to "New",
            AppLanguage.MARATHI to "नवीन",
            AppLanguage.HINDI to "नया",
            AppLanguage.TELUGU to "కొత్తది"
        ),
        "no_pending_requests" to mapOf(
            AppLanguage.ENGLISH to "No pending requests from collectors.",
            AppLanguage.MARATHI to "संकलकांकडून कोणतीही प्रलंबित विनंती नाही.",
            AppLanguage.HINDI to "संग्राहकों से कोई लंबित अनुरोध नहीं है।",
            AppLanguage.TELUGU to "సేకర్తల నుండి పెండింగ్ అభ్యర్థనలు లేవు."
        ),
        "new_lot_request" to mapOf(
            AppLanguage.ENGLISH to "NEW LOT REQUEST",
            AppLanguage.MARATHI to "नवीन लॉट विनंती",
            AppLanguage.HINDI to "नया लॉट अनुरोध",
            AppLanguage.TELUGU to "కొత్త లాట్ అభ్యర్థన"
        ),
        "material_label" to mapOf(
            AppLanguage.ENGLISH to "Material",
            AppLanguage.MARATHI to "साहित्य",
            AppLanguage.HINDI to "सामग्री",
            AppLanguage.TELUGU to "పదార్థం"
        ),
        "quantity_label" to mapOf(
            AppLanguage.ENGLISH to "Quantity",
            AppLanguage.MARATHI to "प्रमाण",
            AppLanguage.HINDI to "मात्रा",
            AppLanguage.TELUGU to "పరిమాణం"
        ),
        "collector_label" to mapOf(
            AppLanguage.ENGLISH to "Collector",
            AppLanguage.MARATHI to "संकलक",
            AppLanguage.HINDI to "संग्राहक",
            AppLanguage.TELUGU to "సేకర్త"
        ),
        "location_label" to mapOf(
            AppLanguage.ENGLISH to "Location",
            AppLanguage.MARATHI to "ठिकाण",
            AppLanguage.HINDI to "स्थान",
            AppLanguage.TELUGU to "ప్రాంతం"
        ),
        "schedule_van_pickup" to mapOf(
            AppLanguage.ENGLISH to "Schedule Van Pickup",
            AppLanguage.MARATHI to "व्हॅन पिकअप शेड्यूल करा",
            AppLanguage.HINDI to "वैन पिकअप शेड्यूल करें",
            AppLanguage.TELUGU to "వ్యాన్ పికప్ షెడ్యూల్ చేయండి"
        ),
        "offered_buying_rates" to mapOf(
            AppLanguage.ENGLISH to "Offered Buying Rates (₹ / kg)",
            AppLanguage.MARATHI to "ऑफर केलेले खरेदी दर (₹ / किलो)",
            AppLanguage.HINDI to "प्रस्तावित खरीद दरें (₹ / किग्रा)",
            AppLanguage.TELUGU to "ఆఫర్ చేసిన కొనుగోలు ధరలు (₹ / కిలో)"
        ),
        "update_buying_prices" to mapOf(
            AppLanguage.ENGLISH to "Update your current purchasing prices for collectors.",
            AppLanguage.MARATHI to "संकलकांसाठी आपल्या खरेदी किमती अद्यतनित करा.",
            AppLanguage.HINDI to "संग्राहकों के लिए अपने वर्तमान खरीद मूल्य अपडेट करें।",
            AppLanguage.TELUGU to "సేకర్తల కోసం మీ ప్రస్తుత కొనుగోలు ధరలను అప్‌డేట్ చేయండి."
        ),
        "pickup_rules" to mapOf(
            AppLanguage.ENGLISH to "Pickup Rules",
            AppLanguage.MARATHI to "पिकअप नियम",
            AppLanguage.HINDI to "पिकअप नियम",
            AppLanguage.TELUGU to "పికప్ నియమాలు"
        ),
        "offer_doorstep_pickup" to mapOf(
            AppLanguage.ENGLISH to "Offer Doorstep Pickup Availability",
            AppLanguage.MARATHI to "घरोघरी पिकअप सेवा उपलब्ध करा",
            AppLanguage.HINDI to "घर-घर पिकअप उपलब्धता प्रदान करें",
            AppLanguage.TELUGU to "డోర్‌స్టెప్ పికప్ లభ్యతను అందించండి"
        ),
        "min_lot_qty" to mapOf(
            AppLanguage.ENGLISH to "Min Lot Qty (kg)",
            AppLanguage.MARATHI to "किमान लॉट प्रमाण (किलो)",
            AppLanguage.HINDI to "न्यूनतम लॉट मात्रा (किग्रा)",
            AppLanguage.TELUGU to "కనిష్ట లాట్ పరిమాణం (కిలో)"
        ),
        "pickup_radius" to mapOf(
            AppLanguage.ENGLISH to "Pickup Radius (km)",
            AppLanguage.MARATHI to "पिकअप त्रिज्या (किमी)",
            AppLanguage.HINDI to "पिकअप दायरा (किमी)",
            AppLanguage.TELUGU to "పికప్ వ్యాసార్థం (కిమీ)"
        ),
        "save_rates_settings" to mapOf(
            AppLanguage.ENGLISH to "Save Rates & Settings",
            AppLanguage.MARATHI to "दर आणि सेटिंग्ज जतन करा",
            AppLanguage.HINDI to "दरें और सेटिंग्स सहेजें",
            AppLanguage.TELUGU to "ధరలు మరియు సెట్టింగ్‌లను సేవ్ చేయండి"
        ),
        "govt_auth_certificate" to mapOf(
            AppLanguage.ENGLISH to "GOVERNMENT AUTHORIZATION CERTIFICATE",
            AppLanguage.MARATHI to "शासकीय अधिकृतता प्रमाणपत्र",
            AppLanguage.HINDI to "सरकारी प्राधिकरण प्रमाणपत्र",
            AppLanguage.TELUGU to "ప్రభుత్వ అధికార ధృవీకరణ పత్రం"
        ),
        "lot_id_label" to mapOf(
            AppLanguage.ENGLISH to "Lot ID",
            AppLanguage.MARATHI to "लॉट आयडी",
            AppLanguage.HINDI to "लॉट आईडी",
            AppLanguage.TELUGU to "లాట్ ఐడి"
        ),
        "agreed_rate_label" to mapOf(
            AppLanguage.ENGLISH to "Agreed Rate",
            AppLanguage.MARATHI to "मंजूर दर",
            AppLanguage.HINDI to "सहमति दर",
            AppLanguage.TELUGU to "ఒప్పుకున్న రేటు"
        ),
        "actual_weight_label" to mapOf(
            AppLanguage.ENGLISH to "Actual Inspected Weight (kg) *",
            AppLanguage.MARATHI to "प्रत्यक्ष तपासलेले वजन (किलो) *",
            AppLanguage.HINDI to "वास्तविक निरीक्षण वजन (किग्रा) *",
            AppLanguage.TELUGU to "వాస్తవ తనిఖీ బరువు (కిలో) *"
        ),
        "final_payable" to mapOf(
            AppLanguage.ENGLISH to "Final Payable Amount",
            AppLanguage.MARATHI to "अंतिम देय रक्कम",
            AppLanguage.HINDI to "अंतिम देय राशि",
            AppLanguage.TELUGU to "తుది చెల్లించవలసిన మొత్తం"
        ),
        "handover_confirm_note" to mapOf(
            AppLanguage.ENGLISH to "Both Collector and Recycler confirm weight and handover in person.",
            AppLanguage.MARATHI to "संकलक आणि रिसायकलर दोघेही समोरासमोर वजन आणि हस्तांतरण निश्चित करतात.",
            AppLanguage.HINDI to "संग्राहक और पुनर्चक्रणकर्ता दोनों व्यक्तिगत रूप से वजन और हस्तांतरण की पुष्टि करते हैं।",
            AppLanguage.TELUGU to "సేకర్త మరియు రీసైక్లర్ ఇద్దరూ స్వయంగా బరువు మరియు బదిలీని ధృవీకరిస్తారు."
        ),
        "confirm_handover_payment" to mapOf(
            AppLanguage.ENGLISH to "Confirm Handover & Record Payment",
            AppLanguage.MARATHI to "हस्तांतरण निश्चित करा आणि देयकाची नोंद करा",
            AppLanguage.HINDI to "हस्तांतरण की पुष्टि करें और भुगतान रिकॉर्ड करें",
            AppLanguage.TELUGU to "బదిలీని నిర్ధారించండి మరియు చెల్లింపును రికార్డ్ చేయండి"
        ),
        "paid_cleared" to mapOf(
            AppLanguage.ENGLISH to "Paid / Cleared",
            AppLanguage.MARATHI to "पैसे दिले / पूर्ण झाले",
            AppLanguage.HINDI to "भुगतान किया / चुकता",
            AppLanguage.TELUGU to "చెల్లించబడింది / పూర్తయింది"
        ),
        "pending_dues" to mapOf(
            AppLanguage.ENGLISH to "Pending Dues",
            AppLanguage.MARATHI to "प्रलंबित थकबाकी",
            AppLanguage.HINDI to "बकाया राशि",
            AppLanguage.TELUGU to "పెండింగ్ బకాయిలు"
        ),
        "volume_handled" to mapOf(
            AppLanguage.ENGLISH to "Volume Handled",
            AppLanguage.MARATHI to "हाताळलेले प्रमाण",
            AppLanguage.HINDI to "संभाली गई मात्रा",
            AppLanguage.TELUGU to "హ్యాండిల్ చేసిన పరిమాణం"
        ),
        "search_records_placeholder" to mapOf(
            AppLanguage.ENGLISH to "Search by category, Txn ID, or party...",
            AppLanguage.MARATHI to "श्रेणी, व्यवहार आयडी किंवा पक्षाद्वारे शोधा...",
            AppLanguage.HINDI to "श्रेणी, लेन-देन आईडी या पार्टी द्वारा खोजें...",
            AppLanguage.TELUGU to "వర్గం, లావాదేవీ ఐడి లేదా పార్టీ ద్వారా శోధించండి..."
        ),
        "no_matching_records" to mapOf(
            AppLanguage.ENGLISH to "No matching transaction records.",
            AppLanguage.MARATHI to "कोणत्याही जुळणाऱ्या व्यवहार नोंदी नाहीत.",
            AppLanguage.HINDI to "कोई मेल खाने वाला लेन-देन रिकॉर्ड नहीं।",
            AppLanguage.TELUGU to "సరిపోలే లావాదేవీ రికార్డులు లేవు."
        ),
        "view_receipt" to mapOf(
            AppLanguage.ENGLISH to "View Receipt",
            AppLanguage.MARATHI to "पावती पहा",
            AppLanguage.HINDI to "रसीद देखें",
            AppLanguage.TELUGU to "రశీదు చూడండి"
        ),
        "receipt_title" to mapOf(
            AppLanguage.ENGLISH to "E-Waste Handover Receipt",
            AppLanguage.MARATHI to "ई-कचरा हस्तांतरण पावती",
            AppLanguage.HINDI to "ई-कचरा हस्तांतरण रसीद",
            AppLanguage.TELUGU to "ఇ-వ్యర్థాల బదిలీ రశీదు"
        ),
        "share_print_pdf" to mapOf(
            AppLanguage.ENGLISH to "Share / Print PDF",
            AppLanguage.MARATHI to "शेअर करा / PDF प्रिंट करा",
            AppLanguage.HINDI to "शेयर करें / पीडीएफ प्रिंट करें",
            AppLanguage.TELUGU to "భాగస్వామ్యం చేయండి / PDF ముద్రించండి"
        ),
        "close_btn" to mapOf(
            AppLanguage.ENGLISH to "Close",
            AppLanguage.MARATHI to "बंद करा",
            AppLanguage.HINDI to "बंद करें",
            AppLanguage.TELUGU to "మూసివేయి"
        ),
        "audio_speed" to mapOf(
            AppLanguage.ENGLISH to "Audio Speed",
            AppLanguage.MARATHI to "आवाज वेग",
            AppLanguage.HINDI to "आवाज गति",
            AppLanguage.TELUGU to "వాయిస్ వేగం"
        ),
        "audio_player_title" to mapOf(
            AppLanguage.ENGLISH to "Audio Narrator",
            AppLanguage.MARATHI to "ऑडिओ कथन",
            AppLanguage.HINDI to "ऑडियो विवरण",
            AppLanguage.TELUGU to "ఆడియో వివరణ"
        ),
        "voice_guidance_active" to mapOf(
            AppLanguage.ENGLISH to "Voice Guidance Active",
            AppLanguage.MARATHI to "आवाज मार्गदर्शन सुरू आहे",
            AppLanguage.HINDI to "ध्वनि मार्गदर्शन सक्रिय है",
            AppLanguage.TELUGU to "వాయిస్ గైడెన్స్ సక్రియంగా ఉంది"
        ),
        "now_speaking" to mapOf(
            AppLanguage.ENGLISH to "Speaking",
            AppLanguage.MARATHI to "वाचत आहे",
            AppLanguage.HINDI to "बोल रहा है",
            AppLanguage.TELUGU to "చెబుతోంది"
        ),
        "change_language" to mapOf(
            AppLanguage.ENGLISH to "Change Language",
            AppLanguage.MARATHI to "भाषा बदला",
            AppLanguage.HINDI to "भाषा बदलें",
            AppLanguage.TELUGU to "భాష మార్చండి"
        ),
        "voice_unavailable_title" to mapOf(
            AppLanguage.ENGLISH to "Voice Audio Unavailable",
            AppLanguage.MARATHI to "आवाज (TTS) डेटा उपलब्ध नाही",
            AppLanguage.HINDI to "आवाज (TTS) डेटा उपलब्ध नहीं है",
            AppLanguage.TELUGU to "వాయిస్ (TTS) డేటా అందుబాటులో లేదు"
        ),
        "marathi_region_label" to mapOf(
            AppLanguage.ENGLISH to "Maharashtra",
            AppLanguage.MARATHI to "महाराष्ट्र",
            AppLanguage.HINDI to "महाराष्ट्र",
            AppLanguage.TELUGU to "మహారాష్ట్ర"
        ),
        "camera_capture_title" to mapOf(
            AppLanguage.ENGLISH to "Take Live E-Waste Photo",
            AppLanguage.MARATHI to "ई-कचऱ्याचा थेट फोटो घ्या",
            AppLanguage.HINDI to "ई-कचरे की लाइव फोटो लें",
            AppLanguage.TELUGU to "ఇ-వ్యర్థాల ప్రత్యక్ష ఫోటో తీయండి"
        ),
        "camera_capture_desc" to mapOf(
            AppLanguage.ENGLISH to "Capture clear photo of material for instant AI classification & weight check",
            AppLanguage.MARATHI to "त्वरित AI वर्गीकरण आणि वजन तपासणीसाठी साहित्याचा स्पष्ट फोटो काढा",
            AppLanguage.HINDI to "त्वरित एआई वर्गीकरण और वजन जांच के लिए सामग्री की स्पष्ट फोटो लें",
            AppLanguage.TELUGU to "తక్షణ AI వర్గీకరణ మరియు బరువు తనిఖీ కోసం స్పష్టమైన ఫోటో తీయండి"
        ),
        "take_photo_btn" to mapOf(
            AppLanguage.ENGLISH to "Open Camera",
            AppLanguage.MARATHI to "कॅमेरा सुरू करा",
            AppLanguage.HINDI to "कैमरा खोलें",
            AppLanguage.TELUGU to "కెమెరా తెరవండి"
        ),
        "retake_photo_btn" to mapOf(
            AppLanguage.ENGLISH to "Retake Live Photo",
            AppLanguage.MARATHI to "पुन्हा फोटो काढा",
            AppLanguage.HINDI to "दोबारा फोटो लें",
            AppLanguage.TELUGU to "మళ్లీ ఫోటో తీయండి"
        ),
        "gallery_pick_btn" to mapOf(
            AppLanguage.ENGLISH to "Upload Gallery",
            AppLanguage.MARATHI to "गॅलरीतून निवडा",
            AppLanguage.HINDI to "गैलरी से चुनें",
            AppLanguage.TELUGU to "గ్యాలరీ నుండి"
        ),
        "camera_permission_required" to mapOf(
            AppLanguage.ENGLISH to "Camera permission is required to capture photos of e-waste lots.",
            AppLanguage.MARATHI to "ई-कचऱ्याचे फोटो काढण्यासाठी कॅमेरा परवानगी आवश्यक आहे.",
            AppLanguage.HINDI to "ई-कचरा सामग्री की फोटो लेने के लिए कैमरा अनुमति आवश्यक है।",
            AppLanguage.TELUGU to "ఇ-వ్యర్థాల ఫోటోలు తీయడానికి కెమెరా అనుమతి అవసరం."
        ),
        "photo_captured_success" to mapOf(
            AppLanguage.ENGLISH to "Live photo captured successfully!",
            AppLanguage.MARATHI to "थेट फोटो यशस्वीरित्या घेतला!",
            AppLanguage.HINDI to "लाइव फोटो सफलतापूर्वक ले ली गई!",
            AppLanguage.TELUGU to "లైవ్ ఫోటో విజయవంతంగా తీయబడింది!"
        ),
        "photo_attached" to mapOf(
            AppLanguage.ENGLISH to "Photo Attached",
            AppLanguage.MARATHI to "फोटो जोडला आहे",
            AppLanguage.HINDI to "फोटो संलग्न है",
            AppLanguage.TELUGU to "ఫోటో జోడించబడింది"
        )
    )
}
