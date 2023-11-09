import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Field from './Field';
import Button from './Button';

export default function Popup({ changePopup }) {
  const [login, onLoginChange] = useState('');
  const [password, onPasswordChange] = useState('');
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  useEffect(() => {
    if (button === true) {
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
  }, [button]);
  return (
    <div data-testid="login" className="background">
      <div className="popup__body">
        <button data-testid="close" type="button" className="close" onClick={() => changePopup(false)} aria-label="Close" />
        {error !== '' && <p className="error">{error}</p>}
        <Field dataTestId="login-input" name="Login" value={login} type="text" onInputChange={onLoginChange} />
        <Field dataTestId="password-input" name="Password" value={password} type="password" onInputChange={onPasswordChange} />
        <div className="link"><a href="/reset">Forgot password</a></div>
        <Button className="btn-full" name="Login" onButton={onButton} />
        <div className="link link-register"><Link data-testid="to-register-btn" to="/register" onClick={() => changePopup(false)}>Register</Link></div>
      </div>
    </div>
  );
}
