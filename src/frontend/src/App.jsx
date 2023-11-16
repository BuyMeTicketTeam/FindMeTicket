/* eslint-disable max-len */
import React, { useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Header from './components/Header';
import Routers from './routers';
import './testServer';
import './App.css';
import './styles/header.css';
import './styles/login.css';
import './locales/i18n';

function App() {
  const [authorization, onAuthorization] = useState(false);
  const [popupLogin, changePopup] = useState(false);
  return (
    <Router>
      <Header authorization={authorization} onAuthorization={onAuthorization} changePopup={changePopup} popupLogin={popupLogin} />
      <Routers changePopup={changePopup} />
    </Router>
  );
}

export default App;
