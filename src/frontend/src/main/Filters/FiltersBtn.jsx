import React from 'react';
import './filters.css';

export default function FiltersBtn({
  isDown, isUp, onClick, sortType, children, reverse,
}) {
  function prevSort(prev) {
    if (prev === sortType) {
      return reverse;
    }
    return sortType;
  }
  return (
    <button
      className={`main-filters__btn ${isDown || isUp ? 'active' : ''} ${isUp ? 'up' : ''}`}
      type="button"
      onClick={() => onClick((prev) => prevSort(prev))}
    >
      {children}
    </button>
  );
}
