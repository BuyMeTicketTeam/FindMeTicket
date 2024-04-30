/* eslint-disable import/no-named-as-default-member */
import React from 'react';
import {
  Routes, Route,
} from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import RouteController from './RouteController';
import Reset from '../pages/Reset';
import Register from '../pages/Register';
import Confirm from '../pages/Confirm';
import Index from '../main';
import TicketPage from '../ticketPage';
import Login from '../pages/Login';
import ConfirmReset from '../pages/ConfirmReset';
import ChangePassword from '../changePassword';
import PrivacyPolicyEng from '../privacyPolicy/index-eng';
import PrivacyPolicyUa from '../privacyPolicy/index-ua';
import TouristPlaces from '../TouristPlaces';
import ProfilePage from '../pages/Profile';
import Reviews from '../Reviews';

export default function Routers({
  updateAuthValue, ticketsData,
  setTicketsData, selectedTransport,
  setSelectedTransport,
}) {
  const { language } = useTranslation().i18n;
  const auth = useSelector((state) => state.user.isAuthenticated);
  console.log(auth);
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
            // urlSearch={urlSearch}
            // setUrlSearch={setUrlSearch}
          />
        )}
      />
      <Route
        path="/login"
        element={(
          <RouteController access={!auth}>
            <Login updateAuthValue={updateAuthValue} />
          </RouteController>
      )}
      />
      <Route
        path="/register"
        element={(
          <RouteController access={!auth}>
            <Register />
          </RouteController>
      )}
      />
      <Route
        path="/confirm"
        element={(
          <RouteController access={!auth}>
            <Confirm />
          </RouteController>
      )}
      />
      <Route
        path="/reset"
        element={(
          <RouteController access={!auth}>
            <Reset />
          </RouteController>
      )}
      />
      <Route
        path="/reset-password"
        element={(
          <RouteController access={!auth}>
            <ConfirmReset />
          </RouteController>
      )}
      />
      <Route
        path="/change-password"
        element={(
          <RouteController access={!auth}>
            <ChangePassword />
          </RouteController>
      )}
      />
      <Route path="/ticket-page/:ticketId" element={<TicketPage />} />
      <Route
        path="/change-password"
        element={(
          <RouteController access={!auth}>
            <ChangePassword />
          </RouteController>
      )}
      />
      {language === 'Ua' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyUa />} />
      )}
      {language === 'Eng' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyEng />} />
      )}
      <Route path="/tourist-places/:city?" element={<TouristPlaces auth={auth} />} />
      <Route path="/reviews" element={<Reviews status={auth} />} />
      <Route
        path="/profile-page"
        element={(
          <RouteController access={auth}>
            <ProfilePage status={auth} updateAuthValue={updateAuthValue} />
          </RouteController>
    )}
      />
    </Routes>
  );
}
