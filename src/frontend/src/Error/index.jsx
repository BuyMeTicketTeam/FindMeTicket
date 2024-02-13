import React from 'react';
import { useTranslation } from 'react-i18next';
import './error.scss';
import errorIcon from './error.svg';

export default function Error({ error }) {
  const { t } = useTranslation('translation', { keyPrefix: 'main-error' });

  return (
    <div className="main-error">
      <img className="main-error__img" src={errorIcon} alt="Error" />
      <h2 className="main-error__title">{typeof error === 'string' ? error : t('title')}</h2>
      <p className="main-error__text">{typeof error === 'boolean' ? t('text') : null}</p>
    </div>
  );
}
