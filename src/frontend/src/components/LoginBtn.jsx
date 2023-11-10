import React from 'react';
import { useTranslation } from 'react-i18next';

export default function LoginBtn({ status, changePopup }) {
  const { t } = useTranslation('translation', { keyPrefix: 'header' });
  if (status) {
    return <button className="login" type="button">{t('profile')}</button>;
  }
  return (
    <button data-testid="login-btn" className="login" onClick={() => { changePopup(true); }} type="button">{t('login')}</button>
  );
}
