/* eslint-disable no-shadow */
import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Select from 'react-select';
import useLanguage from '../../hook/useLanguage';
import LoginBtn from '../LoginBtn/index';
import './header.scss';
import logo from '../../images/logo.svg';
import planetIcon from '../../images/language.svg';

const languages = [
  { value: 'Ua', label: 'Укр' },
  { value: 'Eng', label: 'Eng' },
];

export default function Header() {
  const { language, setLanguageValue } = useLanguage(languages);
  const { t } = useTranslation('translation', { keyPrefix: 'header' });

  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src={logo} alt="logo" /></Link></div>
      <ul className="menu">
        <li className="menu__item"><Link to="/">{t('reviews')}</Link></li>
        <li className="menu__item"><Link to="/tourist-places">{t('tourist-places')}</Link></li>
      </ul>
      <div className="language-select">
        <img src={planetIcon} alt="Language-icon" />
        <Select
          data-testid="language-select"
          options={languages}
          classNamePrefix="react-select"
          placeholder={null}
          value={language}
          isSearchable={false}
          onChange={(lang) => setLanguageValue(lang.value)}
        />
      </div>
      <LoginBtn />
    </header>
  );
}
