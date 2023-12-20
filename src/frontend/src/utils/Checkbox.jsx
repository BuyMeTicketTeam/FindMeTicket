import React from 'react';
import { useTranslation } from 'react-i18next';

export default function Checkbox({ onClick }) {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  return (
    <>
      <input
        id="remember"
        type="checkbox"
        className="checkbox__field"
        onChange={onClick}
      />
      <label htmlFor="remember" className="checkbox">{t('remember-me')}</label>
    </>
  );
}
