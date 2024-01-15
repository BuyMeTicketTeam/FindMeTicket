import React from 'react';
import Price from './Price/index';
import Information from './Information/index ';
import Maps from './Maps/index';
import './style.css';

function TicketPage() {
  return (
    <div className="ticket-page-container">
      <div className="ticketPage-header">Ср, 29 грудня</div>
      <Information />
      <div className="ticketPage-text">Ціни</div>
      <Price />
      <Maps />
    </div>
  );
}

export default TicketPage;
