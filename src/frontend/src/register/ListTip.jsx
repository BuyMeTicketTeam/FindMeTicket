import React from 'react';
import { useTranslation } from 'react-i18next';

export default function ListTip() {
  const { t } = useTranslation('translation', { keyPrefix: 'tip' });
  return (
    <>
      {t('nickname-tip')}
      <ul className="listtip">
        <li>{t('nickname-tip2')}</li>
        <li>{t('nickname-tip3')}</li>
        <li>{t('nickname-tip4')}</li>
        <li>{t('nickname-tip5')}</li>
      </ul>
    </>
  );
}
