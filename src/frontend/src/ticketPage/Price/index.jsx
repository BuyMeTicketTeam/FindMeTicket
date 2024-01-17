import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';

function Price({ ticketsUrl }) {
  return (
    <div className="ticket-price">
      {ticketsUrl.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} />)}
    </div>
  );
}

export default Price;
