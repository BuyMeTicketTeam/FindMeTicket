/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Cookies from 'universal-cookie';
import { GoogleLogin } from '@react-oauth/google';
import FacebookLogin from '@greatsumini/react-facebook-login';
import { passwordCheck, emailCheck } from '../../helper/regExCheck';
import Field from '../../utils/Field';
import Button from '../../utils/Button';
import makeQuerry from '../../helper/querry';
import Checkbox from '../../utils/Checkbox';
// import facebookIcon from './facebook.png';
import './login.css';

export default function Popup({ updateAuthValue }) {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const cookies = new Cookies(null, { path: '/' });
  const navigate = useNavigate();
  const [login, onLoginChange] = useState('');
  const [loginError, onLoginError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [error, onError] = useState('');
  const [send, onSend] = useState(false);
  const [remember, rememberMe] = useState(false);
  const [show, onShow] = useState(false);

  function statusChecks(response) {
    switch (response.status) {
      case 200:
        navigate('/');
        updateAuthValue(true);
        console.log(response.headers);
        response.headers.forEach((value, key) => {
          if (key === 'rememberme') {
            console.log('key');
            cookies.set('rememberMe', 'true', { maxAge: 260000 * 60 * 1000 });
          }

          if (key === 'userid') {
            console.log('userid');
            cookies.set('USER_ID', value);
          }
        });
        break;
      case 401:
        onError(t('error-lp'));
        break;
      default:
        onError(t('error-server2'));
        break;
    }
  }

  function validation() {
    switch (true) {
      case emailCheck(login):
        onError(t('login-error'));
        onLoginError(true);
        return false;
      case passwordCheck(password):
        onError(t('password-error'));
        onPasswordError(true);
        return false;
      default:
        return true;
    }
  }

  function handleClick() {
    if (!validation()) {
      onSend(false);
      return;
    }
    const body = {
      email: login,
      password,
      rememberMe: remember,
    };
    makeQuerry('login', JSON.stringify(body))
      .then((response) => {
        onSend(false);
        statusChecks(response);
      });
  }

  async function auth2Request(provider, credential) {
    const bodyJSON = JSON.stringify({ idToken: credential });
    const response = await makeQuerry(`oauth2/authorize/${provider}`, bodyJSON);
    switch (response.status) {
      case 200:
        navigate('/');
        updateAuthValue(true);
        break;
      case 401:
        onError('Помилка. Спробуйте ще раз');
        break;
      default:
        console.log(response.error);
        onError(t('error-server2'));
        break;
    }
  }

  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);

  function handleLoginChange(value) {
    onLoginChange(value);
    onLoginError(false);
    onError('');
  }

  function handlePasswordChange(value) {
    onPasswordChange(value);
    onPasswordError(false);
    onError('');
  }

  function handleRememberMeChange() {
    rememberMe(!remember);
  }

  return (
    <div data-testid="login" className="background">
      <div className="popup__body">
        <Link to="/" className="close" aria-label="Close" />
        {error !== '' && <p data-testid="error" className="error">{error}</p>}
        <Field
          error={loginError}
          dataTestId="login-input"
          name={t('email-name')}
          tip={t('login-tip')}
          value={login}
          type="text"
          onInputChange={(value) => handleLoginChange(value)}
          placeholder="mail@mail.com"
        />

        <Field
          error={passwordError}
          dataTestId="password-login-input"
          name={t('password-name')}
          tip={t('password-tip')}
          value={password}
          type="password"
          onInputChange={(value) => handlePasswordChange(value)}
          show={show}
          onShow={onShow}
        />

        <Checkbox onClick={() => handleRememberMeChange()} />
        <div className="link">
          <Link
            to="/reset"
          >
            {t('forgot-password')}
          </Link>
        </div>

        <Button
          dataTestId="send-request"
          className="btn-full"
          disabled={send}
          name={send ? t('processing') : t('login-buttom')}
          onButton={onSend}
        />

        <div className="login__another">
          <span className="login-another__line" />
          <span className="login-another__content">{t('or')}</span>
          <span className="login-another__line" />
        </div>
        <GoogleLogin
          onSuccess={(credentialResponse) => {
            onError('');
            console.log(credentialResponse);
            auth2Request('google', credentialResponse.credential);
          }}
          onError={() => {
            console.log('Login Failed');
            onError('Помилка. Спробуйте ще раз');
          }}
        />
        <FacebookLogin
          appId="927706882244929"
          onSuccess={(response) => {
            onError('');
            console.log('Login Success!', response);
            auth2Request('facebook', response.userID);
          }}
          onFail={(errorFacebook) => {
            console.log('Login Failed!', errorFacebook);
            onError('Помилка. Спробуйте ще раз');
          }}
          onProfileSuccess={(response) => {
            console.log('Get Profile Success!', response);
          }}
        />
        <div className="link link-register">
          <Link
            data-testid="to-register-btn"
            to="/register"
          >
            {t('register')}
          </Link>
        </div>

      </div>
    </div>
  );
}
