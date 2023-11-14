import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import Input from '../components/Input';
import Button from '../components/Button';
import makeQuerry from '../helper/querry';
import './confirm.css';

export default function Confirm() {
  const { t } = useTranslation('translation', { keyPrefix: 'confirm' });
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [succes, onSucces] = useState(false);
  const [minutes, setMinutes] = useState(10);
  const [seconds, setSeconds] = useState(0);
  const [sendAg, onSendAg] = useState(false);
  function handleCodeChange(value) {
    onCodeChange(value);
    onCodeError(false);
  }
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
      onSendAg(false);
    }
  }, [seconds, minutes]);
  useEffect(() => {
    if (sendAg) {
      const body = {
        email: sessionStorage.getItem('email'),
        code,
      };
      makeQuerry('resend-confirm-token', JSON.stringify(body))
        .then((response) => {
          if (response.status === 200) {
            setMinutes(10);
            onSendAg(false);
          } else if (response.status === 404) {
            onError("З'єднання з сервером відсутнє");
          } else {
            onError('Помилка серверу. Спробуйте ще раз');
          }
        });
    }
  }, [sendAg]);
  useEffect(() => {
    if (button === true) {
      onCodeError(false);
      onButton(false);
      if
      (code.match(/^[a-zA-Z0-9\s]{5}$/) === null) {
        onError(t('confirm-error'));
        onCodeError(true);
      } else {
        const body = {
          email: sessionStorage.getItem('email'),
          code,
        };
        makeQuerry('confirm-email', JSON.stringify(body))
          .then((response) => {
            if (response.status === 200) {
              onSucces(true);
              onError('');
            } else if (response.status === 400) {
              onError('Код не правильний');
            } else if (response.status === 404) {
              onError("З'єднання з сервером відсутнє");
            } else {
              onError('Помилка серверу. Спробуйте ще раз');
            }
          });
      }
    }
  }, [button]);
  return (
    <div data-testid="confirm" className="confirm">
      <h1 className="title">{t('confirm-email')}</h1>
      {succes && <p className="confirm__success">Пошту підтвержено!!!</p>}
      <p className="confirm__text">{t('confirm-code')}</p>
      <p className="confirm__text"><b>{t('confirm-ten')}</b></p>
      <Input error={codeError} dataTestId="confirm-input" value={code} onInputChange={(value) => handleCodeChange(value)} type="text" />
      {error !== '' && <p className="confirm__error">{error}</p>}
      <Button name={t('send')} className="confirm__btn" onButton={onButton} />
      <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={() => onSendAg(true)} type="button">{t('time', { minutes, seconds })}</button>
    </div>
  );
}
