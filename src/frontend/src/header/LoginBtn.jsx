import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import makeQuerry from '../helper/querry';

export default function LoginBtn({ status, changePopup, onAuthorization }) {
  const { t } = useTranslation('translation', { keyPrefix: 'header' });
  const [logout, setLogout] = useState(false);

  function handleLogoutButton() {
    setLogout(false);

    makeQuerry('logout').then((response) => {
      switch (response.status) {
        case 200:
          onAuthorization(!status);
          localStorage.removeItem('JWTtoken');
          break;
        default:
          break;
      }
    });
  }

  useEffect(() => {
    if (logout) {
      handleLogoutButton();
    }
  }, [logout]);

  if (status) {
    return (
      <button
        data-testid="logout-btn"
        className="login"
        type="button"
        onClick={() => { setLogout(true); }}
      >
        {t('profile')}
      </button>
    );
  }
  return (
    <button
      data-testid="login-btn"
      className="login"
      onClick={() => { changePopup(true); }}
      type="button"
    >
      {t('login')}
    </button>
  );
}
