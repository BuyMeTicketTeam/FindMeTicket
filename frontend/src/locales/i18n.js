/* eslint-disable import/no-extraneous-dependencies */
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import { uk } from './uk/uk';
import { eng } from './eng/eng';

i18n.use(initReactI18next).init({
  resources: {
    Ua: { translation: uk },
    Eng: { translation: eng },
  }, // Where we're gonna put translations' files
  lng: 'Eng', // Set the initial language of the App
});
