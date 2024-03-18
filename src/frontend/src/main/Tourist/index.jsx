import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import './tourist.scss';

function Banner({ city }) {
  const { t } = useTranslation('translation', { keyPrefix: 'banner' });

  return (
    <div className="banner-container">
      <div className="centered-content">
        <p className="banner-text">
          {t('banner-text')}
          {' '}
          {city}
        </p>
        <Link to={`/tourist-places/${city}`} className="button banner__btn">{t('banner-btn')}</Link>
      </div>
    </div>
  );
}

export default Banner;
