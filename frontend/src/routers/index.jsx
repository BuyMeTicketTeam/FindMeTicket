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
import PrivacyPolicyEng from '../privacyPolicy/index-eng';
import PrivacyPolicyUa from '../privacyPolicy/index-ua';
import TouristPlaces from '../TouristPlaces';
import ProfilePage from '../newProfile';

export default function Routers({
  updateAuthValue, ticketsData,
  setTicketsData, selectedTransport,
  setSelectedTransport, auth, language,
}) {
  return (
    <Routes>
      <Route
        path="/*"
        element={(
          <Index
            ticketsData={ticketsData}
            setTicketsData={setTicketsData}
            selectedTransport={selectedTransport}
            setSelectedTransport={setSelectedTransport}
          />
        )}
      >
        <Route path="login" element={<Login updateAuthValue={updateAuthValue} />} />
      </Route>
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/reset-password" element={<ResetPassword />} />
      <Route path="/change-password" element={<ChangePassword />} />
      <Route path="/ticket-page/:ticketId" element={<TicketPage />} />
      <Route path="/change-password" element={<ChangePassword />} />
      <Route path="/tourist-places/:city?" element={<TouristPlaces auth={auth} />} />
      <Route path="/profile-page" element={<ProfilePage status={auth} updateAuthValue={updateAuthValue} />} />
      {language.value === 'UA' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyUa />} />
      )}
      {language.value === 'ENG' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyEng />} />
      )}
    </Routes>
  );
}
