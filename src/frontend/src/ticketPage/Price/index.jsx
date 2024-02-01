import React from 'react';
import './style.scss';
import PriceBlock from './PriceBlock';
import spinningLoaderIcon from './spinning-loading.svg';

function Price({ ticketUrls, price, connection }) {
  return (
    <div className="ticket-price">
      {ticketUrls.length > 0
      && ticketUrls.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} price={price} />)}
      {connection && <img src={spinningLoaderIcon} alt="loader" />}
    </div>
  );
}

export default Price;
