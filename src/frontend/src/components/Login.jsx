import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Field from './Field';
import Button from './Button';

export default function Popup({ changePopup }) {
  const { t } = useTranslation('translation', { keyPrefix: 'login' });
  const [login, onLoginChange] = useState('');
  const [loginError, onLoginError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  useEffect(() => {
    if (button === true) {
      onLoginError(false);
      onPasswordError(false);
      onButton(false);
      if (login.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError(t('login-error'));
        onLoginError(true);
      } else if (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null) {
        onError(t('password-error'));
        onPasswordError(true);
      } else {
        fetch('/userData', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            login,
            password,
          }),
        })
          .then((response) => {
            if (!response.ok) {
              onError('Some error');
            }
          });
      }
    }
  }, [button]);
  return (
    <div data-testid="login" className="background">
      <div className="popup__body">
        <button data-testid="close" type="button" className="close" onClick={() => changePopup(false)} aria-label="Close" />
        {error !== '' && <p className="error">{error}</p>}
        <Field error={loginError} dataTestId="login-input" name={t('email-name')} tip={t('login-tip')} value={login} type="text" onInputChange={onLoginChange} placeholder="mail@mail.com" />
        <Field error={passwordError} dataTestId="password-input" name={t('password-name')} tip={t('password-tip')} value={password} type="password" onInputChange={onPasswordChange} />
        <div className="link"><Link data-testid="" to="/reset" onClick={() => changePopup(false)}>{t('forgot-password')}</Link></div>
        <Button className="btn-full" name={t('login-buttom')} onButton={onButton} />
        <div className="link link-register"><Link data-testid="to-register-btn" to="/register" onClick={() => changePopup(false)}>{t('register')}</Link></div>
      </div>
    </div>
  );
}
