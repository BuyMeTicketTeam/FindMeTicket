import React, { useState, useEffect } from 'react';
import Input from '../components/Input';
import Button from '../components/Button';
import './confirm.css';

export default function Confirm() {
  const [code, onCodeChange] = useState('');
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [minutes, setMinutes] = useState(10);
  const [seconds, setSeconds] = useState(0);
  const [sendAg, onSendAg] = useState(false);
  useEffect(() => {
    if (minutes > 0 || seconds > 0) {
      setTimeout(() => {
        if (seconds === 0) {
          setMinutes(minutes - 1);
          setSeconds(59);
        } else {
          setSeconds(seconds - 1);
        }
      }, 1000);
    }
  }, [seconds, minutes]);
  useEffect(() => {
    if (sendAg) {
      setMinutes(10);
      fetch('/userReqCode', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((response) => {
          if (!response.ok) {
            onError('Some error');
          }
        });
      onSendAg(false);
    }
  }, [sendAg]);
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
    <div data-testid="confirm" className="confirm">
      <h1 className="title">Confirm your email</h1>
      <p className="confirm__text">We send you a letter with a confirm code.</p>
      <p className="confirm__text"><b>You have 10 minutes to confirm it.</b></p>
      <Input dataTestId="confirm-input" value={code} onInputChange={onCodeChange} type="text" />
      {error !== '' && <p className="confirm__error">{error}</p>}
      <Button name="Send" className="confirm__btn" onButton={onButton} />
      <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={() => onSendAg(true)} type="button">{`Send a letter again: ${minutes}:${seconds}`}</button>
    </div>
  );
}
