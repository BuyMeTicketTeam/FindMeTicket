import React from 'react';
import PriceBlock from '../PriceBlock';
import Error from '../../../common/Error';

import spinningLoaderIcon from '../../../images/spinning-loading.svg';
import busforIcon from '../../../images/busfor.png';
import proizdIcon from '../../../images/proizd.webp';
import ticketsIcon from '../../../images/tickets.jpg';

import './style.scss';

function Price({ ticketUrls, connection }) {
  function defineImage(resource) {
    switch (resource) {
      case 'busfor.ua':
        return busforIcon;

      case 'proizd.ua':
        return proizdIcon;

      default:
        return ticketsIcon;
    }
  }

  return (
    <div className="ticket-price">
      {ticketUrls.length > 0
      && ticketUrls.map((ticketUrl) => (
        <PriceBlock
          img={defineImage(ticketUrl.resource)}
          title={ticketUrl.resource}
          price={ticketUrl.price}
          url={ticketUrl.url}
        />
      ))}
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls === 0 && !connection) && <Error />}
    </div>
  );
}

export default Price;
