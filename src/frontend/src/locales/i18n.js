/* eslint-disable import/no-extraneous-dependencies */
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import ukJSON from './uk.json';

i18n.use(initReactI18next).init({
  resources: {
    uk: { ...ukJSON },
  }, // Where we're gonna put translations' files
  lng: 'uk', // Set the initial language of the App
});
