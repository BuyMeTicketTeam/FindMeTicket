/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import Header from './components/Header';
import Routers from './routers';
// import './testServer';
import './App.css';
import './styles/header.css';
import './styles/login.css';
import './locales/i18n';
import Cookie from './components/cookie';

function App() {
  const [cookies] = useCookies();
  const [authorization, onAuthorization] = useState(false);
  const [popupLogin, changePopup] = useState(false);
  useEffect(() => {
    if (cookies?.remember_me) {
      onAuthorization(true);
    }
  }, []);
  return (
    <Router>
      <Header authorization={authorization} onAuthorization={onAuthorization} changePopup={changePopup} popupLogin={popupLogin} />
      <Routers changePopup={changePopup} />
      <Cookie />
    </Router>

  );
}

export default App;
