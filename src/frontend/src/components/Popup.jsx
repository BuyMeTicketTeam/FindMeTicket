import React, { useEffect, useState } from 'react';
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
    <div className="background">
      <div className="popup__body">
        <button type="button" className="close" onClick={() => changePopup(false)} aria-label="Close" />
        {error !== '' && <p className="error">{error}</p>}
        <Field name="Login" value={login} type="text" onInputChange={onLoginChange} />
        <Field name="Password" value={password} type="password" onInputChange={onPasswordChange} />
        <Button className="btn-full" name="Login" onButton={onButton} />
        <div className="link"><a href="/register">Register</a></div>
      </div>
    </div>
  );
}
