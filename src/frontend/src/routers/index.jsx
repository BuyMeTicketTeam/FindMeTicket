import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Reset from '../reset';
import Register from '../register';
import Confirm from '../confirm';
import Index from '../main';
import Login from '../header/login/index';
import ChangePassword from '../changePassword';
import OAuth2RedirectHandler from '../oauth2/OAuth2RedirectHandler';

export default function index({ changePopup, updateAuthValue }) {
  return (
    <Routes>
      <Route path="/*" element={<Index />}>
        <Route path="login" element={<Login changePopup={changePopup} isMain />} />
      </Route>
      <Route path="/register/*" element={<Register />} />
      <Route path="/confirm/*" element={<Confirm changePopup={changePopup} />} />
      <Route path="/reset/*" element={<Reset />} />
      <Route path="/change-password/*" element={<ChangePassword changePopup={changePopup} />} />
      <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler updateAuthValue={updateAuthValue} />} />
    </Routes>
  );
}
