import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Field from './Field';
import Button from './Button';

export default function Popup({ changePopup }) {
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
        onError('Поле email заповнено не вірно');
        onLoginError(true);
      } else if (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null) {
        onError('Поле паролю заповнено не вірно');
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
        <Field error={loginError} dataTestId="login-input" name="Email" tip="Будь ласка введіть свою електронну адресу" value={login} type="text" onInputChange={onLoginChange} placeholder="mail@mail.com" />
        <Field error={passwordError} dataTestId="password-input" name="Password" tip="Будь ласка введіть свій пароль" value={password} type="password" onInputChange={onPasswordChange} />
        <div className="link"><a href="/reset">Forgot password</a></div>
        <Button className="btn-full" name="Login" onButton={onButton} />
        <div className="link link-register"><Link data-testid="to-register-btn" to="/register" onClick={() => changePopup(false)}>Register</Link></div>
      </div>
    </div>
  );
}
