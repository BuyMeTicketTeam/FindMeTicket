import React from 'react';
import { useTranslation } from 'react-i18next';
import Button from '../../utils/Button';
import './tourist.css';

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
        <Button className="banner__btn" name={t('banner-btn')} />
      </div>
    </div>
  );
}

export default Banner;
