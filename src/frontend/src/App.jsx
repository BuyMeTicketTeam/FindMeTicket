import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Index from './pages/index';
import Register from './pages/Register';
import Header from './components/Header';
import Confirm from './pages/Confirm';
import './App.css';
import './styles/header.css';
import './styles/login.css';
import './styles/register.css';
import './styles/confirm.css';

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route exact path="/" element={<Index />} />
        <Route path="/register" element={<Register />} />
        <Route path="/confirm" element={<Confirm />} />
      </Routes>
    </Router>
  );
}

export default App;
