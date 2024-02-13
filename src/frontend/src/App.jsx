/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React, { useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';
import Header from './header/index';
import useAuthCheck from './hook/useAuthCheck';
import Routers from './routers';
import CookieBanner from './cookieBanner/cookie';
// import './testServer';
import Footer from './footer';
import './App.scss';
import './locales/i18n';

function App() {
  const { auth, updateAuthValue } = useAuthCheck();
  const [language, setLanguage] = useState({ value: 'UA', label: 'УКР' });
  const [ticketsData, setTicketsData] = useState([]);

  return (
    <Router>
      <div className="body">
        <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
          <Header
            language={language}
            setLanguage={setLanguage}
            authorization={auth}
            updateAuthValue={updateAuthValue}
          />
          <Routers updateAuthValue={updateAuthValue} language={language} ticketsData={ticketsData} setTicketsData={setTicketsData} />
          <CookieBanner />
          <Footer />
        </GoogleOAuthProvider>
      </div>
    </Router>

  );
}

export default App;
