import React from 'react';
import { useTranslation } from 'react-i18next';
import './ad.scss';

export default function Index({ isBig }) {
  const { t } = useTranslation('translation', { keyPrefix: 'ad' });
  return (
    <div className={`ad-container ${isBig ? 'ad_big' : ''}`}>
      <div className="ad-text">{t('title')}</div>
      <div className="contact-email">{t('email')}</div>
    </div>
  );
}
