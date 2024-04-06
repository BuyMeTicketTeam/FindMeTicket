import React from 'react';
import {
  Routes, Route,
} from 'react-router-dom';
import Reset from '../reset';
import Register from '../register';
import Confirm from '../confirm';
import Index from '../main';
import TicketPage from '../ticketPage';
import Login from '../header/login/index';
import ResetPassword from '../resetPassword';
import ChangePassword from '../changePassword';
import PrivacyPolicyUa from '../privacyPolicy/index-ua';
import PrivacyPolicyEng from '../privacyPolicy/index-eng';

export default function Routers({
  updateAuthValue, ticketsData, setTicketsData, language,
}) {
  console.log(language);
  return (
    <Routes>
      <Route path="/*" element={<Index ticketsData={ticketsData} setTicketsData={setTicketsData} />}>
        <Route path="login" element={<Login updateAuthValue={updateAuthValue} />} />
      </Route>
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/reset-password" element={<ResetPassword />} />
      <Route path="/change-password" element={<ChangePassword />} />
      <Route path="/ticket-page/:ticketId" element={<TicketPage />} />
      <Route path="/change-password" element={<ChangePassword />} />
      {language.value === 'UA' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyUa />} />
      )}
      {language.value === 'ENG' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyEng />} />
      )}
    </Routes>
  );
}
