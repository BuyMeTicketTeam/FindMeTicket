/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable import/no-extraneous-dependencies */
import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { GoogleLogin } from '@react-oauth/google';
import { useForm } from 'react-hook-form';
import FacebookLogin from '@greatsumini/react-facebook-login';
import { PASSWORD_PATTERN, EMAIL_PATTERN } from '../../constants/regex';
import facebookIcon from '../../images/facebook.png';

import './login.scss';

export default function Login() {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const { register, handleSubmit, formState: { errors } } = useForm();
  // const navigate = useNavigate();

  console.log(errors);

  function onSubmit(data, error) {
    console.log(data, error);
  }

  return (
    <div data-testid="login" className="background main">
      <form className="popup__body" onSubmit={handleSubmit(onSubmit)}>
        <Link to="/" className="close" aria-label="Close" />

        <input className="input" type="text" {...register('email', { required: true, pattern: EMAIL_PATTERN })} />
        {errors.email && <p data-testid="error" className="confirm__error">Email error</p>}
        <input className="input" type="password" {...register('password', { required: true, pattern: PASSWORD_PATTERN })} />
        {errors.password && <p data-testid="error" className="confirm__error">Password error</p>}

        <button type="submit">Login</button>

        <div className="login__another">
          <span className="login-another__line" />
          <span className="login-another__content">{t('or')}</span>
          <span className="login-another__line" />
        </div>
        <GoogleLogin
          onSuccess={() => {
            // setError('');
            // auth2Request('google', credentialResponse.credential);
          }}
          onError={() => {
            // setError('Помилка. Спробуйте ще раз');
          }}
          shape="circle"
          width={336}
        />
        <FacebookLogin
          appId="927706882244929"
          className="login__google"
          onSuccess={() => {
            // setError('');
            // auth2Request('facebook', response.userID);
          }}
          onFail={() => {
            // setError('Помилка. Спробуйте ще раз');
          }}
          onProfileSuccess={() => {
          }}
        >
          <img src={facebookIcon} alt="logo" />
          {t('facebook')}

        </FacebookLogin>
        <div className="link link-register">
          <Link
            data-testid="to-register-btn"
            to="/register"
          >
            {t('register')}
          </Link>
        </div>

      </form>
    </div>
  );
}
