/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable import/no-extraneous-dependencies */
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { GoogleLogin } from '@react-oauth/google';
import { useForm } from 'react-hook-form';
import FacebookLogin from '@greatsumini/react-facebook-login';
import { useLoginMutation, useLoginGoogleMutation, useLoginFacebookMutation } from '../../services/userApi';

import { PASSWORD_PATTERN, EMAIL_PATTERN } from '../../constants/regex';

import Input from '../../components/Input';
import Checkbox from '../../components/Checkbox';

import facebookIcon from '../../images/facebook.png';

import './login.scss';

export default function Login() {
  const [error, setError] = useState(null);
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const { register, handleSubmit, formState: { errors } } = useForm({ mode: 'all' });
  const [login, { isLoading }] = useLoginMutation();
  const [loginGoogle] = useLoginGoogleMutation();
  const [loginFacebook] = useLoginFacebookMutation();
  const navigate = useNavigate();

  async function handleQuery(data, queryFunc) {
    try {
      setError(null);
      await queryFunc(data).unwrap();
      navigate('/');
    } catch (err) {
      console.error({ error: err });
      setError(err.status ?? 500);
    }
  }

  async function onSubmit(data) {
    handleQuery(data, login);
  }

  return (
    <div data-testid="login" className="background main">
      <form className="popup__body" onSubmit={handleSubmit(onSubmit)}>
        <Link to="/" className="close" aria-label="Close" />
        {error && <p data-testid="error" className="error">{t([`error_${error}`, 'error_500'])}</p>}
        <Input
          id="email"
          error={errors.email}
          errorMessage={t('login_error')}
          label={t('email_name')}
          otherProps={{ ...register('email', { required: true, pattern: EMAIL_PATTERN }) }}
        />
        <Input
          id="password"
          error={errors.password}
          errorMessage={t('password_error')}
          label={t('password_name')}
          type="password"
          otherProps={{ ...register('password', { required: true, pattern: PASSWORD_PATTERN }) }}
        />

        <Checkbox
          id="rememberMe"
          otherProps={{ ...register('rememberMe') }}
        >
          {t('remember_me')}
        </Checkbox>
        <div className="link">
          <Link
            to="/reset"
          >
            {t('forgot_password')}
          </Link>
        </div>

        <button disabled={isLoading} className="button btn-full" type="submit">{isLoading ? t('processing') : t('login_button')}</button>

        <div className="login__another">
          <span className="login-another__line" />
          <span className="login-another__content">{t('or')}</span>
          <span className="login-another__line" />
        </div>
        <GoogleLogin
          onSuccess={(credentialResponse) => {
            handleQuery({ idToken: credentialResponse.credential }, loginGoogle);
          }}
          onError={() => {
            setError('google');
          }}
          shape="circle"
          width={336}
        />
        <FacebookLogin
          appId="927706882244929"
          className="login__google"
          onSuccess={(response) => handleQuery({ idToken: response.userID }, loginFacebook)}
          onFail={() => {
            setError('facebook');
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
