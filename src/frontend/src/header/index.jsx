/* eslint-disable no-shadow */
import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Select from 'react-select';
import LoginBtn from './LoginBtn';
import './header.scss';
import logo from './logo.svg';

export default function Header({
  authorization, updateAuthValue, language, setLanguage,
}) {
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'header' });
  const languages = [
    { value: 'UA', label: 'UA' },
    { value: 'ENG', label: 'ENG' },
  ];

  function getSystemLanguage() {
    const systemLanguage = navigator.language.split('-')[0];
    if (systemLanguage !== 'uk') {
      return ({ value: 'ENG', label: 'ENG' });
    }
    return null;
  }

  function setLanguageToStorage(language) {
    localStorage.setItem('lang', JSON.stringify(language));
  }

  function getLanguageFromStorage() {
    return JSON.parse(localStorage.getItem('lang'));
  }

  function getLanguage() {
    const savedLanguage = getLanguageFromStorage();
    if (savedLanguage) {
      return savedLanguage;
    }
    return getSystemLanguage();
  }

  function displayLanguage(languageParam) {
    const language = languageParam || getLanguage();
    setLanguage(language);
    setLanguageToStorage(language);
    i18n.changeLanguage(language.value);
  }

  function handleLanguageChange(lang) {
    displayLanguage(lang);
    window.location.reload();
    sessionStorage.removeItem('ticketsData');
  }

  useEffect(() => {
    displayLanguage();
  }, []);
  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src={logo} alt="logo" /></Link></div>
      <ul className="menu">
        <li className="menu__item"><a href="/">{t('news')}</a></li>
        <li className="menu__item"><a href="/">{t('reviews')}</a></li>
        <li className="menu__item"><a href="/">{t('tourist-places')}</a></li>
        <li className="menu__item"><a href="/">{t('popular-places')}</a></li>
      </ul>
      <Select
        data-testid="language-select"
        options={languages}
        classNamePrefix="react-select"
        placeholder={null}
        value={language}
        isSearchable={false}
        onChange={(lang) => handleLanguageChange(lang)}
      />

      <LoginBtn
        status={authorization}
        updateAuthValue={updateAuthValue}
      />

    </header>
  );
}
