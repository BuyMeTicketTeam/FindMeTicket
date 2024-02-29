/* eslint-disable no-shadow */
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Select from 'react-select';
import LoginBtn from './LoginBtn';
import Popup from './profile';
import './header.scss';
import logo from './logo.svg';

export default function Header({
  authorization, updateAuthValue, language, setLanguage,
}) {
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'header' });
  const [isprofilePopup, setIsProfilePopup] = useState(false);
  const [userAvatar, setUserAvatar] = useState(null);
  const languages = [
    { value: 'UA', label: 'УКР' },
    { value: 'ENG', label: 'ENG' },
  ];
  function getSystemLanguage() {
    const systemLanguage = navigator.language.split('-')[0];
    if (systemLanguage !== 'uk') {
      return ({ value: 'ENG', label: 'ENG' });
    }
    return ({ value: 'UA', label: 'УКР' });
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
    return getSystemLanguage() ?? ({ value: 'ENG', label: 'ENG' });
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
    if (authorization && authorization.status === 200) {
      setIsProfilePopup(true);
    } else {
      setIsProfilePopup(false);
    }
  }, [authorization, setIsProfilePopup]);

  useEffect(() => {
    displayLanguage();
  }, []);
  useEffect(() => {
    if (authorization && authorization.status === 200 && authorization.image) {
      setUserAvatar(authorization.image);
    } else {
      setUserAvatar(null);
    }
  }, [authorization, setUserAvatar]);

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
        setIsProfilePopup={setIsProfilePopup}
        status={authorization}
        updateAuthValue={updateAuthValue}
        username={authorization ? authorization.username : null}
      />

      {isprofilePopup && (
      <Popup
        setIsProfilePopup={setIsProfilePopup}
        updateAuthValue={updateAuthValue}
        status={authorization}
        username={authorization.username}
        userAvatar={userAvatar}
        setUserAvatar={setUserAvatar}
      />
      )}

    </header>
  );
}
