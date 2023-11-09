import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Index from './main/index';
import Register from './register';
import Header from './components/Header';
import Confirm from './confirm/index';
import Reset from './reset/index';
import './App.css';
import './styles/header.css';
import './styles/login.css';

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route exact path="/" element={<Index />} />
        <Route path="/register" element={<Register />} />
        <Route path="/confirm" element={<Confirm />} />
        <Route path="/reset" element={<Reset />} />
      </Routes>
    </Router>
  );
}

export default App;
