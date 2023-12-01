import React from 'react';

export default function Filters({ onSort }) {
  return (
    <div className="main-filters">
      <button className="main-filters__btn" type="button" onClick={() => onSort('price-up')}>Ціна</button>
      <button className="main-filters__btn" type="button">Час поїздки</button>
    </div>
  );
}
