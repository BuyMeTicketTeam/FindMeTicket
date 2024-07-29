/* eslint-disable no-shadow */
import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Select from 'react-select';
import LoginBtn from './LoginBtn';
import './header.scss';
import logo from './logo.svg';
import globys from './language.svg';

export default function Header({
  authorization, updateAuthValue, language, setLanguage,
}) {
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'header' });
  const languages = [
    { value: 'UA', label: 'Укр' },
    { value: 'ENG', label: 'Eng' },
  ];

  function getSystemLanguage() {
    const systemLanguage = navigator.language.split('-')[0];
    if (systemLanguage !== 'uk') {
      return { value: 'ENG', label: 'Eng' };
    }
    return ({ value: 'UA', label: 'Укр' });
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
    return getSystemLanguage() ?? ({ value: 'ENG', label: 'Eng' });
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
  }

  useEffect(() => {
    displayLanguage();
  }, []);

  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src={logo} alt="logo" /></Link></div>
      <ul className="menu">
        <li className="menu__item"><Link to="/reviews">{t('reviews')}</Link></li>
        <li className="menu__item"><Link to="/tourist-places">{t('tourist-places')}</Link></li>
      </ul>
      <img src={globys} alt="Busfor" />
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
        username={authorization ? authorization.username : null}
      />

    </header>
  );
}
