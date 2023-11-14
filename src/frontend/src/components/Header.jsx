import React, { useState } from 'react';
import { Link } from 'react-router-dom';
// eslint-disable-next-line import/no-extraneous-dependencies
import { useTranslation } from 'react-i18next';
import LoginBtn from './LoginBtn';
import LanguageSelect from './LanguageSelect';
import Login from './Login';

export default function Header({
  authorization, onAuthorization, changePopup, popupLogin,
}) {
  const [language, changeLanguage] = useState('Ua');
  const { t } = useTranslation('translation', { keyPrefix: 'header' });
  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src="../img/logo2.png" alt="logo" /></Link></div>
      <ul className="menu">
        <li className="menu__item"><a href="/">{t('news')}</a></li>
        <li className="menu__item"><a href="/">{t('reviews')}</a></li>
        <li className="menu__item"><a href="/">{t('tourist-places')}</a></li>
        <li className="menu__item"><a href="/">{t('popular-places')}</a></li>
      </ul>
      <LanguageSelect language={language} changeLanguage={changeLanguage} />
      <LoginBtn status={authorization} changePopup={changePopup} />
      {popupLogin && <Login changePopup={changePopup} onAuthorization={onAuthorization} />}
    </header>
  );
}
