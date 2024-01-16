import React, { useEffect } from 'react';
import {
  Routes, Route, Navigate, useLocation, useNavigate,
} from 'react-router-dom';
import Reset from '../reset';
import Register from '../register';
import Confirm from '../confirm';
import Index from '../main';
import ChangePassword from '../changePassword';

export default function Routers({ setPopup, language }) {
  const location = useLocation();
  const navigation = useNavigate();
  useEffect(() => {
    if (location.pathname === '/ua' || location.pathname === '/eng') {
      navigation(`/${language.value.toLowerCase()}`);
    }
  }, [language]);

  return (
    <Routes>
      <Route path="/" element={<Navigate to={`/${language.value}`} />} />
      <Route path="/:locale" element={<Index />} />
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm setPopup={setPopup} />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/change-password" element={<ChangePassword setPopup={setPopup} />} />
    </Routes>
  );
}
