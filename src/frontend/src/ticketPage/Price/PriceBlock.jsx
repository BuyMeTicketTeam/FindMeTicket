import React from 'react';
import './style.css';
import busforIcon from './busfor.png';
import proizdIcon from './proizd.webp';
import infobusIcon from './infobus.webp';

function ResourceImage({ resource }) {
  switch (resource) {
    case 'busfor.ua':
      return <img src={busforIcon} alt={resource} />;

    case 'proizd.ua':
      return <img src={proizdIcon} alt={resource} />;

    default:
      return <img src={infobusIcon} alt={resource} />;
  }
}

export default function PriceBlock({ ticketUrl, price }) {
  return (
    <div className="price-block" data-testid="price-block">
      <ResourceImage resource={ticketUrl.resource} />
      <span>
        {ticketUrl.resource}
        :
      </span>
      <div className="price-container" data-testid="price-container">
        <div className="price">{price}</div>
      </div>
      <a href={ticketUrl.url} className="button buy-button">Купити</a>
    </div>
  );
}
