import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Reset from '../reset';
import Register from '../register';
import Confirm from '../confirm';
import Index from '../main';
import TicketPage from '../ticketPage';
import ChangePassword from '../changePassword';

export default function index({ changePopup }) {
  return (
    <Routes>
      <Route exact path="/" element={<Index />} />
      <Route path="/register" element={<Register />} />
      <Route path="/confirm" element={<Confirm changePopup={changePopup} />} />
      <Route path="/reset" element={<Reset />} />
      <Route path="/change-password" element={<ChangePassword changePopup={changePopup} />} />
      <Route path="/ticket-page/:id" element={<TicketPage />} />
    </Routes>
  );
}
