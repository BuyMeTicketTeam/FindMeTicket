import React from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';
import busforIcon from './busfor.png';
import proizdIcon from './proizd.webp';
import infobusIcon from './infobus.webp';

function ResourceImage({ resource }) {
  switch (resource) {
    case 'busfor.ua':
      return <img src={busforIcon} alt={resource} />;

    case 'proizd.ua':
      return <img src={proizdIcon} alt={resource} />;

    default:
      return <img src={infobusIcon} alt={resource} />;
  }
}

export default function PriceBlock({
  title, price, url, img,
}) {
  const { t } = useTranslation('translation', { keyPrefix: 'ticket-page' });
  return (
    <div className="price-block" data-testid="price-block">
      {img && <img src={img} alt={title} />}
      <span>
        {title}
        :
      </span>
      <div className="price-container" data-testid="price-container">
        <div className="price">
          {Number(price).toFixed(2)}
          {' '}
          {t('uan')}
        </div>
      </div>
      <a href={url} className="button buy-button" target="_blank" rel="noreferrer">{t('buy')}</a>
    </div>
  );
}
