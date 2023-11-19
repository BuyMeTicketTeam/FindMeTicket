import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { codeCheck } from '../helper/regExCheck';
import Input from '../components/utlis/Input';
import Button from '../components/utlis/Button';
import makeQuerry from '../helper/querry';
import timeOut from '../helper/timer';
import './confirm.css';

export default function Confirm({ changePopup }) {
  const { t } = useTranslation('translation', { keyPrefix: 'confirm' });
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [error, onError] = useState('');
  const [succes, onSucces] = useState(false);
  const [minutes, setMinutes] = useState(1);
  const [seconds, setSeconds] = useState(30);
  const [send, onSend] = useState(false);
  const [resend, onResend] = useState(false);
  useEffect(() => {
    if (minutes > 0 || seconds > 0) {
      timeOut(seconds, minutes).then((time) => {
        setSeconds(time.seconds);
        setMinutes(time.minutes);
      });
    }
  }, [seconds, minutes]);
  function statusChecks(response) {
    if (response.status === 200) {
      onSucces(true);
      onError('');
    } else if (response.status === 400) {
      onError(t('error-code'));
    } else if (response.status === 404) {
      onError(t('error-server'));
    } else {
      onError(t('error-server2'));
    }
  }
  function validation() {
    if (codeCheck(code)) {
      onSend(false);
      onError(t('confirm-error'));
      onCodeError(true);
      return false;
    }
    return true;
  }
  function handleSendButton() {
    onCodeError(false);
    if (!validation) {
      return;
    }
    const body = {
      email: sessionStorage.getItem('email'),
      token: code,
    };
    makeQuerry('confirm-email', JSON.stringify(body))
      .then((response) => {
        onSend(false);
        statusChecks(response);
      });
  }
  function statusChecksForResend(response) {
    if (response.status === 200) {
      setMinutes(2);
    } else if (response.status === 404) {
      onError('Немає акаунту зареєстрованого на цю електронну пошту');
    } else {
      onError('Помилка серверу. Спробуйте ще раз');
    }
  }
  function handleResendButton() {
    const body = { email: sessionStorage.getItem('email') };
    makeQuerry('resend-confirm-token', JSON.stringify(body))
      .then((response) => {
        onResend(false);
        statusChecksForResend(response);
      });
  }
  function handleCodeChange(value) {
    onCodeChange(value);
    onCodeError(false);
  }
  useEffect(() => {
    if (send) {
      handleSendButton();
    }
  }, [send]);
  useEffect(() => {
    if (resend) {
      handleResendButton();
    }
  }, [resend]);
  return (
    <div data-testid="confirm" className="confirm">
      <h1 className="title">{t('confirm-email')}</h1>
      {succes && (
      <div className="confirm__success">
        Пошту підтвержено!!!
        {' '}
        <p>
          <Link className="link-success" data-testid="" to="/" onClick={() => changePopup(true)}>Натисніть для того щоб авторизуватися</Link>
        </p>
      </div>
      )}
      <p className="confirm__text">{t('confirm-code')}</p>
      <p className="confirm__text"><b>{t('confirm-ten')}</b></p>
      <Input error={codeError} dataTestId="confirm-input" value={code} onInputChange={(value) => handleCodeChange(value)} type="text" />
      {error !== '' && <p className="confirm__error">{error}</p>}
      <Button name={send ? 'Обробка...' : t('send')} disabled={send} className="confirm__btn" onButton={onSend} />
      <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={onResend} type="button">{resend ? t('processing') : t('time', { minutes, seconds })}</button>
    </div>
  );
}
