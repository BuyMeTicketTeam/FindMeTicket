import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Select from 'react-select';
import LoginBtn from './LoginBtn';
import Login from './login/index';
import './header.css';

export default function Header({
  authorization, onAuthorization, changePopup, popupLogin,
}) {
  const [language, changeLanguage] = useState({ value: 'UA', label: 'UA' });
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'header' });
  const languages = [
    { value: 'UA', label: 'UA' },
    { value: 'ENG', label: 'ENG' },
  ];

  function languageFunc(languageParam) {
    if (languageParam) {
      sessionStorage.setItem('lang', JSON.stringify(languageParam));
      changeLanguage(languageParam);
      i18n.changeLanguage(languageParam.value);
      return;
    }
    if (sessionStorage.getItem('lang')) {
      const parseLang = JSON.parse(sessionStorage.getItem('lang'));
      changeLanguage(parseLang);
      i18n.changeLanguage(parseLang.value);
    }
  }
  useEffect(() => {
    languageFunc();
  }, []);
  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src="../img/logo.svg" alt="logo" /></Link></div>
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
        onChange={(lang) => languageFunc(lang)}
      />

      <LoginBtn
        status={authorization}
        onAuthorization={onAuthorization}
        changePopup={changePopup}
      />

      {popupLogin && <Login changePopup={changePopup} onAuthorization={onAuthorization} />}
    </header>
  );
}
