import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';

function Price({ ticketsUrl, price }) {
  return (
    <div className="ticket-price">
      {ticketsUrl.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} price={price} />)}
    </div>
  );
}

export default Price;
