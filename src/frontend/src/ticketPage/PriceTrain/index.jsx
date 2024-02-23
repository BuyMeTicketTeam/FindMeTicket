import React from 'react';
import './style.scss';
import PriceBlock from './PriceBlock';
import proizdIcon from './proizd.webp';
import spinningLoaderIcon from './spinning-loading.svg';

function PriceTrain({ ticketUrls, connection }) {
  return (
    <div className="ticket-price">
      <h2 className="ticket-header">
        <img src={proizdIcon} alt="proizdIcon" className="proizd-icon" />
        Proizd.ua
      </h2>
      {ticketUrls.length > 0
          && ticketUrls.map((ticketUrl) => <PriceBlock ticketUrl={ticketUrl} />)}
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls.length === 0 && !connection) && <p>Error...</p>}
    </div>
  );
}

export default PriceTrain;
