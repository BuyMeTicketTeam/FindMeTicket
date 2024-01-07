/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Header from './header/index';
import useAuthCheck from './hook/useAuthCheck';
import Routers from './routers';
// import './testServer';
import './App.css';
import './locales/i18n';

function App() {
  const { auth, updateAuthValue } = useAuthCheck();

  return (
    <Router>
      <Header
        authorization={auth}
        onAuthorization={(value) => updateAuthValue(value)}
      />
      <Routers updateAuthValue={updateAuthValue} />
    </Router>

  );
}

export default App;
