/* eslint-disable import/no-named-as-default-member */
import React from 'react';
import {
  Routes, Route,
} from 'react-router-dom';
import RouteController from './RouteController';
import Reset from '../pages/Reset';
import Register from '../pages/Register';
import Confirm from '../pages/Confirm';
import Index from '../main';
import TicketPage from '../ticketPage';
import Login from '../pages/Login';
import ResetPassword from '../resetPassword';
import ChangePassword from '../changePassword';
import PrivacyPolicy from '../privacyPolicy';
import TouristPlaces from '../TouristPlaces';
import ProfilePage from '../newProfile';

export default function Routers({
  updateAuthValue, ticketsData,
  setTicketsData, selectedTransport,
  setSelectedTransport, auth,
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
      <Route path="/privacy-policy" element={<PrivacyPolicy />} />
      <Route path="/tourist-places/:city?" element={<TouristPlaces auth={auth} />} />
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
