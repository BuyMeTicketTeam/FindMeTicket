/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
// import Cookies from 'universal-cookie';
import Header from './header/index';
import Routers from './routers';
import CookieBanner from './cookieBanner/cookie';
// import './testServer';
import './App.scss';
import './locales/i18n';

function App() {
  const [authorization, setAuthorization] = useState(false);
  const [popupLogin, setPopup] = useState(false);
  const [language, setLanguage] = useState({ value: 'UA', label: 'UA' });
  // const cookies = new Cookies();
  function checkAuth(value) {
    const authValue = sessionStorage.getItem('auth');
    if (authValue === 'true' && value === undefined) {
      setAuthorization(authValue);
      return;
    }
    setAuthorization(value);
    sessionStorage.setItem('auth', value);
  }
  useEffect(() => {
    checkAuth();
  }, []);
  return (
    <Router>
      <Header
        language={language}
        setLanguage={setLanguage}
        authorization={authorization}
        setAuthorization={(value) => checkAuth(value)}
        setPopup={setPopup}
        popupLogin={popupLogin}
      />
      <Routers setPopup={setPopup} language={language} />
      <CookieBanner />
    </Router>

  );
}

export default App;
