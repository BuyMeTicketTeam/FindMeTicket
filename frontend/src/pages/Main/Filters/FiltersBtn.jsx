import React from 'react';
import './filters.scss';
import loaderAnim from '../../../images/loader.svg';

export default function FiltersBtn({
  isActive, isUp, onClick, sortType, children, dataTestId, loading,
}) {
  const btnActive = isActive ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  return (
    <button
      data-testid={dataTestId}
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => onClick(sortType)}
      disabled={loading}
    >
      {loading ? <img src={loaderAnim} alt="Loader anim" /> : children}
    </button>
  );
}