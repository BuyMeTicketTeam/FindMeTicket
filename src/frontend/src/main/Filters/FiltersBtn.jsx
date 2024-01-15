import React from 'react';
import './filters.css';
import loaderIcon from './loader.svg';

export default function FiltersBtn({
  isDown, isUp, onClick, sortType, children, loading,
}) {
  const btnActive = isDown ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  return (
    <button
      disabled={loading}
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => onClick(sortType)}
    >
      {loading ? <img className="main-filters__img" src={loaderIcon} alt="loaderIcon" /> : children}
    </button>
  );
}
