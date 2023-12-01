import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import makeQuerry from '../helper/querry';

export default function LoginBtn({ status, changePopup, onAuthorization }) {
  const { t } = useTranslation('translation', { keyPrefix: 'header' });
  const [logout, onLogout] = useState(false);

  function handleLogoutButton() {
    onLogout(false);
    makeQuerry('logout')
      .then((response) => {
        if (response.status === 200) {
          onAuthorization(!status);
          localStorage.removeItem('JWTtoken');
        }
      });
  }

  useEffect(() => {
    if (logout) {
      handleLogoutButton();
    }
  }, [logout]);

  if (status) {
    return <button className="login" type="button" onClick={() => { onLogout(true); }}>{t('profile')}</button>;
  }
  return (
    <button data-testid="login-btn" className="login" onClick={() => { changePopup(true); }} type="button">{t('login')}</button>
  );
}
