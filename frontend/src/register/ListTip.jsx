import React from 'react';
import { useTranslation } from 'react-i18next';

export default function ListTip() {
  const { t } = useTranslation('translation', { keyPrefix: 'tip' });
  return (
    <>
      {t('nickname-tip-title')}
      <ul className="listtip">
        <li>{t('nickname-tip-chars-numbers')}</li>
        <li>{t('nickname-tip-letters')}</li>
        <li>{t('nickname-tip-sumbols')}</li>
        <li>{t('nickname-tip-spaces')}</li>
      </ul>
    </>
  );
}
