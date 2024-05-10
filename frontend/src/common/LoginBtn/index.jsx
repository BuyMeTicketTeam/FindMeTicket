import React from 'react';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { selectIsAuthenticated } from '../../store/user/userSlice';

import profileIcon from '../../images/user.svg';
import loginIcon from '../../images/logout.svg';

import './loginBtn.scss';

export default function LoginBtn({ isMediumDevice }) {
  const isAuth = useSelector(selectIsAuthenticated);
  const { t } = useTranslation('translation', { keyPrefix: 'header' });

  if (isAuth) {
    return (
      <Link
        data-testid="login-btn"
        className="login-link"
        to="/profile-page"
      >
        {!isMediumDevice ? t('profile') : <img className="login-link__icon" src={profileIcon} alt="Profile" />}
      </Link>
    );
  }
  return (
    <Link
      data-testid="login-btn"
      className="login-link"
      to="/login"
    >
      {!isMediumDevice ? t('login') : <img className="login-link__icon" src={loginIcon} alt="Login" />}
    </Link>
  );
}
