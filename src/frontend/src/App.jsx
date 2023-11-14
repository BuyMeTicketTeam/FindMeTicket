/* eslint-disable max-len */
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Index from './main/index';
import Register from './register';
import Header from './components/Header';
import Confirm from './confirm/index';
import Reset from './reset/index';
import ChangePassword from './changePassword/index';
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
      <Routes>
        <Route exact path="/" element={<Index />} />
        <Route path="/register" element={<Register />} />
        <Route path="/confirm" element={<Confirm changePopup={changePopup} />} />
        <Route path="/reset" element={<Reset />} />
        <Route path="/change-password" element={<ChangePassword changePopup={changePopup} />} />
      </Routes>
    </Router>
  );
}

export default App;
