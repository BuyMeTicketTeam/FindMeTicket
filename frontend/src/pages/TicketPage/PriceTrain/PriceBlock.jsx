import React from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';

export default function PriceBlock({ ticketUrl }) {
  const { t } = useTranslation('translation', { keyPrefix: 'ticket-page' });
  return (
    <div className="price-block" data-testid="price-block">
      <span>
        {ticketUrl.comfort}
        :
      </span>
      <div className="price-container" data-testid="price-container">
        <div className="price">
          {Number(ticketUrl.price).toFixed(2)}
          {' '}
          {t('uan')}
        </div>
      </div>
      <a href={ticketUrl.url} className="button buy-button" target="_blank" rel="noreferrer">{t('buy')}</a>
    </div>
  );
}
