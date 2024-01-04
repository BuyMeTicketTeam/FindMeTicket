/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Header from './header/index';
import Routers from './routers';
import CookieBanner from './cookieBanner/cookie';
import './testServer';
import './App.css';
import './locales/i18n';

function App() {
  const [authorization, onAuthorization] = useState(false);
  const [popupLogin, changePopup] = useState(false);
  function checkAuth(value) {
    const authValue = sessionStorage.getItem('auth');
    if (authValue === 'true' && value === undefined) {
      onAuthorization(authValue);
      return;
    }
    onAuthorization(value);
    sessionStorage.setItem('auth', value);
  }
  useEffect(() => {
    checkAuth();
  }, []);
  return (
    <Router>
      <Header
        authorization={authorization}
        onAuthorization={(value) => checkAuth(value)}
        changePopup={changePopup}
        popupLogin={popupLogin}
      />
      <Routers changePopup={changePopup} />
      <CookieBanner />
    </Router>

  );
}

export default App;
