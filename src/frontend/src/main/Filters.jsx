import React from 'react';

export default function Filters({ onSort, prevSort }) {
  function sortChange() {
    if (prevSort === 'price-low') {
      return 'price-up';
    }
    return 'price-low';
  }
  return (
    <div className="main-filters">
      <button className={`main-filters__btn active ${prevSort === 'price-low' ? 'down' : 'up'}`} type="button" onClick={() => onSort(sortChange())}>Ціна</button>
      <button className="main-filters__btn" type="button">Час поїздки</button>
    </div>
  );
}
