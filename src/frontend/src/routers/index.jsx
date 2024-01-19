import React, { useEffect } from 'react';
import {
  Routes, Route, Navigate, useLocation, useNavigate,
} from 'react-router-dom';
import Reset from '../reset';
import Register from '../register';
import Confirm from '../confirm';
import Index from '../main';
import Login from '../header/login/index';
import ChangePassword from '../changePassword';

export default function Routers({ updateAuthValue, language }) {
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
      <Route path="/:locale" element={<Index />}>
        <Route path="login" element={<Login updateAuthValue={updateAuthValue} />} />
      </Route>
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/change-password" element={<ChangePassword />} />
    </Routes>
  );
}
