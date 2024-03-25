import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import './UnauthorizedPopup.scss';

export default function UnauthorizedPopup() {
  const { t } = useTranslation('translation', { keyPrefix: 'tourist-places' });
  return (
    <div className="unauthorized">
      <div className="tourist-places-background" />
      <div className="unauthorized-content">
        <h2 className="unauthorized-content__title">{t('unauthorized-attention')}</h2>
        <p className="unauthorized-content__text">{t('unauthorized-text')}</p>
        <Link to="/login" className="unauthorized-content__button button">{t('unauthorized-button')}</Link>
      </div>
    </div>
  );
}
