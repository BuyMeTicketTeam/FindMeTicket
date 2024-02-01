import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';

function Price({ ticketUrls, price }) {
  console.log('price section: ', ticketUrls);
  return (
    <div className="ticket-price">
      {ticketUrls.length > 0
        ? ticketUrls.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} price={price} />)
        : <h2>Loading...</h2>}
    </div>
  );
}

export default Price;
