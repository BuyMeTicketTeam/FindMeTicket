import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Input from '../components/Input';
import Button from '../components/Button';
import './reset.css';

export default function Index() {
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
    onCodeError(false);
    onButton(false);
    if (button === true) {
      if (code.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError('Поле nickname заповнено не вірно');
        onCodeError(true);
      } else {
        fetch('/userCode', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            code,
          }),
        })
          .then((response) => {
            if (response.ok) {
              onError('Some error');
            } else {
              navigate('/change-password');
            }
          });
      }
    }
  }, [button]);
  return (
    <div className="reset">
      <h1 className="title">Password reset</h1>
      <p className="reset__text">Введіть свою електронну пошту, на яку вам прийде код зміни пароля.</p>
      <Input error={codeError} value={code} onInputChange={onCodeChange} type="text" placeholder="mail@mail.com" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name="Send" className="reset__btn" onButton={onButton} />
    </div>
  );
}
