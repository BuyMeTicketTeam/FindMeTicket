import React from 'react';
import './style.css';

export default function PriceBlock({ ticketUrl, price }) {
  return (
    <div className="price-block" data-testid="price-block">
      <span>
        Придбати на
        {ticketUrl.provider}
        :
      </span>
      <div className="price-container" data-testid="price-container">
        <div className="price">{price}</div>
        {/* <div className="discounted-price"></div> */}
      </div>
      <a href={ticketUrl.url} className="buy-button">Купити</a>
    </div>
  );
}
