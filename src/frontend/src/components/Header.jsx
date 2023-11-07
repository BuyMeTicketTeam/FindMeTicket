import React, { useState } from 'react';
import LoginBtn from './LoginBtn';
import LanguageSelect from './LanguageSelect';
import Popup from './Popup';

export default function Header() {
  const [popupLogin, changePopup] = useState(false);
  return (
    <header className="header">
      <div className="logo"><a href="/"><img src="../img/logo.png" alt="logo" /></a></div>
      <ul className="menu">
        <li className="menu__item"><a href="/">News</a></li>
        <li className="menu__item"><a href="/">Reviews</a></li>
        <li className="menu__item"><a href="/">Tourist places</a></li>
        <li className="menu__item"><a href="/">Popular routes</a></li>
      </ul>
      <LanguageSelect />
      <LoginBtn status={false} changePopup={changePopup} />
      {popupLogin && <Popup changePopup={changePopup} />}
    </header>
  );
}
