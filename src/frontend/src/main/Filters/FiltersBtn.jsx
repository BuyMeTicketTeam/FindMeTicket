import React from 'react';
import './filters.css';

export default function FiltersBtn({
  isDown, isUp, onClick, sortType, children, setSort,
}) {
  const btnActive = isDown || isUp ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  // function prevSort(prev) {
  //   if (prev === sortType) {
  //     return reverse;
  //   }
  //   return sortType;
  // }

  function handleClick() {
    onClick(sortType);
    setSort(sortType);
  }

  return (
    <button
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => handleClick()}
    >
      {children}
    </button>
  );
}
