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
import ChangePassword from '../changePassword';

export default function Routers({ updateAuthValue, ticketsData, setTicketsData }) {
  return (
    <Routes>
      <Route path="/*" element={<Index ticketsData={ticketsData} setTicketsData={setTicketsData} />}>
        <Route path="login" element={<Login updateAuthValue={updateAuthValue} />} />
      </Route>
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/change-password" element={<ChangePassword />} />
      <Route path="/ticket-page/:ticketId" element={<TicketPage />} />
      <Route path="/change-password" element={<ChangePassword />} />
    </Routes>
  );
}
