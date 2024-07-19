import React, { useState } from 'react';

import PriceBlock from '../PriceBlock';
import Error from '../../../common/Error';

import proizdIcon from '../../../images/proizd.webp';
import spinningLoaderIcon from '../../../images/spinning-loading.svg';
import ticketsuaIcon from '../../../images/tickets.jpg';

import './style.scss';

function PriceTrain({ ticketUrls, connection }) {
  const [selctedResource, setSelectedResource] = useState(0);

  return (
    <div className="train-price ticket-price">
      <div className="ticket-header">
        {ticketUrls.some((url) => url.event === 'proizdua') && (
        <button className={`ticket-header__btn ${selctedResource === 0 ? 'active' : ''}`} type="button" onClick={() => setSelectedResource(0)}>
          <img src={proizdIcon} alt="proizdIcon" className="proizd-icon" />
          Proizd.ua
        </button>
        )}
        {ticketUrls.some((url) => url.event === 'ticketsua') && (
        <button className={`ticket-header__btn ${selctedResource === 1 ? 'active' : ''}`} type="button" onClick={() => setSelectedResource(1)}>
          <img src={ticketsuaIcon} alt="proizdIcon" className="proizd-icon" />
          Tickets.ua
        </button>
        )}
      </div>
      <div className="ticket-price__body">
        {ticketUrls.filter((url) => (selctedResource === 0 ? (url.event === 'proizdua') : (url.event === 'ticketsua'))).map(
          (ticketUrl) => (
            <PriceBlock
              title={ticketUrl.data.comfort}
              price={ticketUrl.data.price}
              url={ticketUrl.data.url}
            />
          ),
        )}
      </div>
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls.length === 0 && !connection) && <Error />}
    </div>
  );
}

export default PriceTrain;
