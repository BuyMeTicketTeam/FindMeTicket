import React from 'react';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { selectIsAuthenticated } from '../../store/user/userSlice';

export default function LoginBtn() {
  const isAuth = useSelector(selectIsAuthenticated);
  const { t } = useTranslation('translation', { keyPrefix: 'header' });

  if (isAuth) {
    return (
      <Link
        data-testid="login-btn"
        className="login-link"
        to="/profile-page"
      >
        {t('profile')}
      </Link>
    );
  }
  return (
    <Link
      data-testid="login-btn"
      className="login-link"
      to="/login"
    >
      {t('login')}
    </Link>
  );
}
