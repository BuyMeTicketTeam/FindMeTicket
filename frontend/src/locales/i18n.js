/* eslint-disable import/no-extraneous-dependencies */
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import ukJSON from './uk.json';
import engJSON from './eng.json';

i18n.use(initReactI18next).init({
  resources: {
    UA: { ...ukJSON },
    ENG: { ...engJSON },
  }, // Where we're gonna put translations' files
  lng: 'UA', // Set the initial language of the App
});
