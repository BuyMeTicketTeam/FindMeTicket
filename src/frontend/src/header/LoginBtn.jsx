/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import Cookies from 'universal-cookie';
import makeQuerry from '../helper/querry';

export default function LoginBtn({ status, updateAuthValue }) {
  const { t } = useTranslation('translation', { keyPrefix: 'header' });
  const cookies = new Cookies(null, { path: '/' });
  const [logout, onLogout] = useState(false);

  function handleLogoutButton() {
    onLogout(false);

    makeQuerry('logout').then((response) => {
      switch (response.status) {
        case 200:
          updateAuthValue(false);
          localStorage.removeItem('JWTtoken');
          cookies.remove('rememberMe');
          cookies.remove('USER_ID');
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
        className="login"
        type="button"
        onClick={() => { onLogout(true); }}
      >
        {t('profile')}
      </button>
    );
  }
  return (
    <Link
      className="login-link"
      to="/login"
    >
      {t('login')}
    </Link>
  );
}
