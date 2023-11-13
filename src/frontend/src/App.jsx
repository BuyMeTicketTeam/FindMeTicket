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
  return (
    <Router>
      <Header authorization={authorization} onAuthorization={onAuthorization} />
      <Routes>
        <Route exact path="/" element={<Index />} />
        <Route path="/register" element={<Register />} />
        <Route path="/confirm" element={<Confirm />} />
        <Route path="/reset" element={<Reset />} />
        <Route path="/change-password" element={<ChangePassword />} />
      </Routes>
    </Router>
  );
}

export default App;
