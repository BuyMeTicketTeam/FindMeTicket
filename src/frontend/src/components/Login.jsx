import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { passwordCheck, emailCheck } from '../helper/regExCheck';
import Field from './utlis/Field';
import Button from './utlis/Button';
import makeQuerry from '../helper/querry';

export default function Popup({ changePopup, onAuthorization }) {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const [login, onLoginChange] = useState('');
  const [loginError, onLoginError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [error, onError] = useState('');
  const [send, onSend] = useState(false);
  function resetErrors() {
    onLoginError(false);
    onPasswordError(false);
  }
  function statusChecks(response) {
    if (response.status === 200) {
      changePopup(false);
      onAuthorization(true);
    } else if (response.status === 400) {
      onError('Логін або пароль хибні');
    } else if (response.status === 404) {
      onError("З'єднання з сервером відсутнє");
    } else {
      onError('Помилка серверу. Спробуйте ще раз');
    }
  }
  function validation() {
    if (emailCheck(login)) {
      onSend(false);
      onError(t('login-error'));
      onLoginError(true);
      return false;
    }
    if (passwordCheck(password)) {
      onSend(false);
      onError(t('password-error'));
      onPasswordError(true);
      return false;
    }
    return true;
  }
  function handleClick() {
    resetErrors();
    if (!validation()) {
      return;
    }
    const body = {
      login,
      password,
    };
    makeQuerry('login', JSON.stringify(body))
      .then((response) => {
        onSend(false);
        statusChecks(response);
      });
  }
  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);
  function handleLoginChange(value) {
    onLoginChange(value);
    onLoginError(false);
  }
  function handlePasswordChange(value) {
    onPasswordChange(value);
    onPasswordError(false);
  }
  return (
    <div data-testid="login" className="background">
      <div className="popup__body">
        <button data-testid="close" type="button" className="close" onClick={() => changePopup(false)} aria-label="Close" />
        {error !== '' && <p data-testid="error" className="error">{error}</p>}
        <Field error={loginError} dataTestId="login-input" name={t('email-name')} tip={t('login-tip')} value={login} type="text" onInputChange={(value) => handleLoginChange(value)} placeholder="mail@mail.com" />
        <Field error={passwordError} dataTestId="password-input" name={t('password-name')} tip={t('password-tip')} value={password} type="password" onInputChange={(value) => handlePasswordChange(value)} />
        <div className="link"><Link data-testid="" to="/reset" onClick={() => changePopup(false)}>{t('forgot-password')}</Link></div>
        <Button data-testid="send-request" className="btn-full" disabled={send} name={send ? 'Обробка...' : t('login-buttom')} onButton={onSend} />
        <div className="link link-register"><Link data-testid="to-register-btn" to="/register" onClick={() => changePopup(false)}>{t('register')}</Link></div>
      </div>
    </div>
  );
}
