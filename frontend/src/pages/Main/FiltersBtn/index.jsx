import React from 'react';
import { useTranslation } from 'react-i18next';
import loaderAnim from '../../../images/loader.svg';

export default function FiltersBtn({
  active, isUp, onClick, children, loading,
}) {
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });
  const btnActive = active ? 'active' : '';
  const btnUp = isUp ? 'up' : '';

  return (
    <button
      className={`main-filters__btn ${btnActive} ${btnUp}`}
      type="button"
      onClick={() => onClick()}
      disabled={loading}
    >
      {loading ? <img src={loaderAnim} alt="Loader anim" /> : t(children)}
    </button>
  );
}
