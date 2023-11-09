import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LoginBtn from './LoginBtn';
import LanguageSelect from './LanguageSelect';
import Login from './Login';

export default function Header() {
  const [popupLogin, changePopup] = useState(false);
  return (
    <header data-testid="header" className="header">
      <div className="logo"><Link to="/"><img src="../img/logo.png" alt="logo" /></Link></div>
      <ul className="menu">
        <li className="menu__item"><a href="/">News</a></li>
        <li className="menu__item"><a href="/">Reviews</a></li>
        <li className="menu__item"><a href="/">Tourist places</a></li>
        <li className="menu__item"><a href="/">Popular routes</a></li>
      </ul>
      <LanguageSelect />
      <LoginBtn status={false} changePopup={changePopup} />
      {popupLogin && <Login changePopup={changePopup} />}
    </header>
  );
}
