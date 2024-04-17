/* eslint-disable import/no-named-as-default-member */
import React from 'react';
import {
  Routes, Route,
} from 'react-router-dom';
import RouteController from './RouteController';
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
import Reviews from '../Reviews';

export default function Routers({
  updateAuthValue, ticketsData,
  setTicketsData, selectedTransport,
  setSelectedTransport, auth, urlSearch, setUrlSearch, language,
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
            urlSearch={urlSearch}
            setUrlSearch={setUrlSearch}
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
            <ResetPassword />
          </RouteController>
      )}
      />
      <Route
        path="/change-password"
        element={(
          <RouteController access={auth}>
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
      {language.value === 'UA' && (
        <Route path="/privacy-policy" element={<PrivacyPolicyUa />} />
      )}
      {language.value === 'ENG' && (
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
