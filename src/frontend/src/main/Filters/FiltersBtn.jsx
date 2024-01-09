import React from 'react';
import './filters.scss';

export default function FiltersBtn({
  isDown, isUp, onClick, sortType, children, reverse,
}) {
  const btnActive = isDown || isUp ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  function prevSort(prev) {
    if (prev === sortType) {
      return reverse;
    }
    return sortType;
  }

  return (
    <button
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => onClick((prev) => prevSort(prev))}
    >
      {children}
    </button>
  );
}
