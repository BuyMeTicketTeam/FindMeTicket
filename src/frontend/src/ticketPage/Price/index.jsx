import React from 'react';
import './style.css';
import PriceBlock from './PriceBlock';

function Price() {
  return (
    <div className="ticket-price">
      <PriceBlock />
      <PriceBlock />
      <PriceBlock />
    </div>
  );
}

export default Price;
