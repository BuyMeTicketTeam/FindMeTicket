import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { codeCheck } from '../helper/regExCheck';
import Input from '../utils/Input';
import Button from '../utils/Button';
import makeQuerry from '../helper/querry';
import timeOut from '../helper/timer';
import './confirm.scss';

export default function Confirm({ changePopup }) {
  const { t } = useTranslation('translation', { keyPrefix: 'confirm' });
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [error, onError] = useState('');
  const [success, onSucces] = useState(false);
  const [minutes, setMinutes] = useState(1);
  const [seconds, setSeconds] = useState(30);
  const [send, onSend] = useState(false);
  const [resend, onResend] = useState(false);
  const sendButtonIsDisabled = send || success;
  const resendButtonIsDisabled = (minutes > 0 || seconds > 0) || success;

  useEffect(() => {
    if (minutes > 0 || seconds > 0) {
      timeOut(seconds, minutes).then((time) => {
        setSeconds(time.seconds);
        setMinutes(time.minutes);
      });
    }
  }, [seconds, minutes]);

  function statusChecks(response) {
    switch (response.status) {
      case 200:
        onSucces(true);
        onError('');
        break;
      case 400:
        onError(t('error-code'));
        break;
      default:
        onError(t('error-server2'));
        break;
    }
  }

  function validation() {
    if (codeCheck(code)) {
      onSend(false);
      onError(t('error-code'));
      onCodeError(true);
      return false;
    }
    return true;
  }

  function handleSendButton() {
    if (!validation()) {
      onSend(false);
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
      setMinutes(1);
      setSeconds(30);
    } else if (response.status === 419) {
      onError(t('error-server1'));
    } else {
      onError(t('error-server2'));
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
      <div className="form-body">
        <h1 className="title">{t('confirm-email')}</h1>
        {success && (
        <div className="confirm__success">
          {t('success-message')}
          {' '}
          <p>
            <Link
              className="link-success"
              data-testid=""
              to="/"
              onClick={() => changePopup(true)}
            >
              {t('auth-link')}
            </Link>

          </p>
        </div>
        )}
        <p>{t('confirm-code')}</p>
        <p className="confirm__text"><b>{t('confirm-ten')}</b></p>
        <Input
          error={codeError}
          dataTestId="confirm-input"
          value={code}
          onInputChange={(value) => handleCodeChange(value)}
          type="text"
        />
        {error !== '' && <p data-testid="error" className="confirm__error">{error}</p>}
        <div className="row">
          <Button
            name={send ? t('processing') : t('send')}
            disabled={sendButtonIsDisabled}
            onButton={onSend}
            dataTestId="confirm-btn"
          />

          <button
            data-testid="send-again-btn"
            className="confirm__send-again"
            disabled={resendButtonIsDisabled}
            onClick={onResend}
            type="button"
          >
            {resend ? t('processing') : t('time', { minutes, seconds })}
          </button>
        </div>
      </div>
    </div>
  );
}
