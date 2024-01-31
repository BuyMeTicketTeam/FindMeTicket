import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';

function Price({ ticketsUrl, price }) {
  return (
    <div className="ticket-price">
      {ticketsUrl > 0
        ? ticketsUrl.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} price={price} />)
        : <h2>Loading...</h2>}
    </div>
  );
}

export default Price;
