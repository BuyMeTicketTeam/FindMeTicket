import React from 'react';
import { useTranslation } from 'react-i18next';
import './error.css';

export default function Error() {
  const { t } = useTranslation('translation', { keyPrefix: 'main-error' });

  return (
    <div className="main-error">
      <img className="main-error__img" src="../img/error.svg" alt="Error" />
      <h2 className="main-error__title">{t('title')}</h2>
      <p className="main-error__text">{t('text')}</p>
    </div>
  );
}
