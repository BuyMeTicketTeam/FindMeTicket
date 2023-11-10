import React, { useState, useEffect } from 'react';
import Input from '../components/Input';
import Button from '../components/Button';
import './reset.css';

export default function Index() {
  const [code, onCodeChange] = useState('');
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');

  useEffect(() => {
    if (button === true) {
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
          if (!response.ok) {
            onError('Some error');
          }
        });
    }
  }, [button]);
  return (
    <div className="reset">
      <h1 className="title">Password reset</h1>
      <Input dataTestId="reset-email-input" value={code} onInputChange={onCodeChange} type="text" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name="Send" className="reset__btn" onButton={onButton} />
    </div>
  );
}
