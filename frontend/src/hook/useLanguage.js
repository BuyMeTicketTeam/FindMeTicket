import { useEffect, useState } from 'react';
import { getI18n } from 'react-i18next';

import { languagesList } from '../locales/languagesList';

export default function useLanguage() {
  const [language, setLanguage] = useState(languagesList[0]);

  function getSystemLanguage() {
    const systemLanguage = navigator.language.split('-')[0];
    switch (systemLanguage) {
      case 'uk':
        return 'Ua';
      default:
        return 'Eng';
    }
  }

  function setLanguageValue(lang) {
    getI18n().changeLanguage(lang);
    localStorage.setItem('lang', lang);
    const languageIndex = languagesList.findIndex((languageItem) => languageItem.value === lang);
    setLanguage(languagesList[languageIndex]);
  }

  useEffect(() => {
    const storageLanguage = localStorage.getItem('lang');
    if (storageLanguage) {
      setLanguageValue(storageLanguage);
      return;
    }
    setLanguageValue(getSystemLanguage());
  }, []);

  return { language, setLanguageValue, languagesList };
}
