import React from 'react';

export default function Filters({ onSort, prevSort }) {
  function sortChange() {
    if (prevSort === 'price-low') {
      return 'price-up';
    }
    return 'price-low';
  }

  const priceSort = () => (prevSort === 'price-low' ? 'down' : 'up');

  return (
    <div className="main-filters">
      <button
        className={`main-filters__btn active ${priceSort}`}
        type="button"
        onClick={() => onSort(sortChange())}
      >
        Ціна
      </button>
      <button className="main-filters__btn" type="button">Час поїздки</button>
      <button className="main-filters__btn" type="button">Час відправлення</button>
      <button className="main-filters__btn" type="button">Час прибуття</button>
    </div>
  );
}
