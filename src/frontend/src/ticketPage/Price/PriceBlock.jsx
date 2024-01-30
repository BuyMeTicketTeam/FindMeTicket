import React from 'react';
import './style.css';

export default function PriceBlock() {
  return (
    <div className="price-block" data-testid="price-block">
      <span>Придбати на Busfor.ua:</span>
      <div className="price-container" data-testid="price-container">
        <div className="price">400 грн</div>
        <div className="discounted-price">800 грн</div>
      </div>
      <button type="button" className="buy-button">Купити</button>
    </div>
  );
}
