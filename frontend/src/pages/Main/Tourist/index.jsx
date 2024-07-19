import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useSearchParams } from 'react-router-dom';
import './tourist.scss';

function Banner() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation('translation', { keyPrefix: 'banner' });
  const arrivalCity = searchParams.get('arrivalCity');

  return (
    <div className="banner-container">
      <div className="centered-content">
        <p className="banner-text">
          {t('banner-text')}
          {' '}
          {arrivalCity}
        </p>
        <Link to={`/tourist-places/${arrivalCity}`} className="button banner__btn">{t('banner-btn')}</Link>
      </div>
    </div>
  );
}

export default Banner;
