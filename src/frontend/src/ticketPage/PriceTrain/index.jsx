import React from 'react';
import './style.scss';
import PriceBlock from './PriceBlock';
import spinningLoaderIcon from './spinning-loading.svg';

function PriceTrain({ ticketUrls, connection }) {
  return (
    <div className="ticket-price">
      <button type="button" className="ticket-header">
        Proizd.ua
      </button>
      <div className="horizontal-line-train" />
      {' '}
      {ticketUrls.length > 0 && ticketUrls.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} />)}
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls.length === 0 && !connection) && <p>Error...</p>}
    </div>
  );
}

export default PriceTrain;
