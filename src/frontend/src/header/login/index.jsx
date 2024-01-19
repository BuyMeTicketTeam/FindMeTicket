/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { disableBodyScroll, clearAllBodyScrollLocks } from 'body-scroll-lock';
import { passwordCheck, emailCheck } from '../../helper/regExCheck';
import Field from '../../utils/Field';
import Button from '../../utils/Button';
import makeQuerry from '../../helper/querry';
import Checkbox from '../../utils/Checkbox';
import googleIcon from './google-icon.png';
import './login.scss';

export default function Popup({ changePopup, onAuthorization }) {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const [login, setLoginChange] = useState('');
  const [loginError, setLoginError] = useState(false);
  const [password, setPasswordChange] = useState('');
  const [passwordError, setPasswordError] = useState(false);
  const [error, setError] = useState('');
  const [send, setSend] = useState(false);
  const [remember, rememberMe] = useState(false);
  const [show, setShow] = useState(false);
  const navigate = useNavigate();
  const loginRef = useRef(null);

  useEffect(() => {
    disableBodyScroll(loginRef.current);
    return () => {
      clearAllBodyScrollLocks();
    };
  }, []);

  function statusChecks(response) {
    switch (response.status) {
      case 200:
        changePopup(false);
        onAuthorization(true);
        navigate('/');
        break;
      case 403:
        setError(t('error-lp'));
        break;
      default:
        setError(t('error-server2'));
        break;
    }
  }

  function validation() {
    switch (true) {
      case emailCheck(login):
        setError(t('login-error'));
        setLoginError(true);
        return false;
      case passwordCheck(password):
        setError(t('password-error'));
        setPasswordError(true);
        return false;
      default:
        return true;
    }
  }

  function handleClick() {
    if (!validation()) {
      setSend(false);
      return;
    }
    const body = {
      email: login,
      password,
    };
    makeQuerry('login', JSON.stringify(body))
      .then((response) => {
        setSend(false);
        statusChecks(response);
      });
  }

  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);

  function handleLoginChange(value) {
    setLoginChange(value);
    setLoginError(false);
    setError('');
  }

  function handlePasswordChange(value) {
    setPasswordChange(value);
    setPasswordError(false);
    setError('');
  }

  function handleRememberMeChange() {
    rememberMe(!remember);
  }

  return (
    <div data-testid="login" className="background">
      <div ref={loginRef} className="popup__body">
        <button
          data-testid="close"
          type="button"
          className="close"
          onClick={() => changePopup(false)}
          aria-label="Close"
        />
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
          setShow={setShow}
        />

        <Checkbox onClick={() => handleRememberMeChange()}>{t('remember-me')}</Checkbox>
        <div className="link">
          <Link
            data-testid=""
            to="/reset"
            onClick={() => changePopup(false)}
          >
            {t('forgot-password')}
          </Link>
        </div>

        <Button
          dataTestId="send-request"
          className="btn-full"
          disabled={send}
          name={send ? t('processing') : t('login-buttom')}
          onButton={setSend}
        />

        <div className="login__another">
          <span className="login-another__line" />
          <span className="login-another__content">{t('or')}</span>
          <span className="login-another__line" />
        </div>
        <a href="http://localhost:8080/oauth2/authorize" className="login__google">
          <img src={googleIcon} alt="logo" />
          {t('google')}
        </a>
        <div className="link link-register">
          <Link
            data-testid="to-register-btn"
            to="/register"
            onClick={() => changePopup(false)}
          >
            {t('register')}
          </Link>
        </div>

      </div>
    </div>
  );
}
