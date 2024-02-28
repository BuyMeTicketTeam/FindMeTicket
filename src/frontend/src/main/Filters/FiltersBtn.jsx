import React from 'react';
import './filters.scss';

export default function FiltersBtn({
  isDown, isUp, onClick, sortType, children, dataTestId, loading,
}) {
  const btnActive = isDown ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  return (
    <button
      data-testid={dataTestId}
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => onClick(sortType)}
      disabled={loading}
    >
      {children}
    </button>
  );
}
