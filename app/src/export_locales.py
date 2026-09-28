import json
import os
import re

lang_map = {
    'ENGLISH': 'en',
    'TELUGU': 'te',
    'HINDI': 'hi',
    'TAMIL': 'ta',
    'KANNADA': 'kn',
    'MALAYALAM': 'ml',
    'MARATHI': 'mr',
    'BENGALI': 'bn',
    'GUJARATI': 'gu',
    'PUNJABI': 'pa',
    'ODIA': 'or'
}

translations = {code: {} for code in lang_map.values()}

files = [
    'app/src/main/java/com/example/service/localization/Translations.kt',
    'app/src/main/java/com/example/service/localization/TranslationsExtended.kt'
]

current_key = None

for filepath in files:
    if not os.path.exists(filepath):
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        for line in f:
            line_str = line.strip()
            # Check for key definition: "key" to mapOf(
            key_match = re.match(r'^\"([a-zA-Z0-9_\.]+)\"\s*to\s*mapOf', line_str)
            if key_match:
                current_key = key_match.group(1)
                continue
            if current_key and line_str.startswith('AppLanguage.'):
                # Pattern: AppLanguage.NAME to "value"
                entry_match = re.match(r'^AppLanguage\.([A-Z]+)\s*to\s*\"(.*)\",?$', line_str)
                if entry_match:
                    lang_name = entry_match.group(1)
                    val = entry_match.group(2)
                    if val.endswith('",'):
                        val = val[:-2]
                    elif val.endswith('"'):
                        val = val[:-1]
                    # replace escaped quotes
                    val = val.replace('\\"', '"').replace('\\n', '\n')
                    if lang_name in lang_map:
                        code = lang_map[lang_name]
                        translations[code][current_key] = val

# Also ensure collector.collectEwaste and hazard are present
for code in lang_map.values():
    if 'collect_ewaste' in translations[code]:
        translations[code]['collector'] = {'collectEwaste': translations[code]['collect_ewaste']}
        translations[code]['collector.collectEwaste'] = translations[code]['collect_ewaste']
    if 'hazard' in translations[code]:
        translations[code]['hazard'] = translations[code]['hazard']

os.makedirs('locales', exist_ok=True)
os.makedirs('app/src/main/assets/locales', exist_ok=True)

for code, d in translations.items():
    p1 = f'locales/{code}.json'
    p2 = f'app/src/main/assets/locales/{code}.json'
    with open(p1, 'w', encoding='utf-8') as f:
        json.dump(d, f, ensure_ascii=False, indent=2)
    with open(p2, 'w', encoding='utf-8') as f:
        json.dump(d, f, ensure_ascii=False, indent=2)
    print(f'{code}: {len(d)} keys exported')
