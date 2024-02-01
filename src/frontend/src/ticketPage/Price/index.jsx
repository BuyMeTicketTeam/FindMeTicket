import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';
import spinningLoaderIcon from './spinning-loading.svg';

function Price({ ticketUrls, price, connection }) {
  console.log('price section: ', ticketUrls);
  return (
    <div className="ticket-price">
      {ticketUrls.length > 0
      && ticketUrls.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} price={price} />)}
      {connection && <img src={spinningLoaderIcon} alt="loader" />}
    </div>
  );
}

export default Price;
