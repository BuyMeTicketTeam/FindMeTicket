/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';
import Header from './header/index';
import useAuthCheck from './hook/useAuthCheck';
import Routers from './routers';
import './testServer';
import './App.css';
import './locales/i18n';

function App() {
  const { auth, updateAuthValue } = useAuthCheck();

  return (
    <Router>
      <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
        <Header
          authorization={auth}
          updateAuthValue={updateAuthValue}
        />
        <Routers updateAuthValue={updateAuthValue} />
      </GoogleOAuthProvider>
    </Router>

  );
}

export default App;
