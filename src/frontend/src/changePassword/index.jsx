import React, { useState, useEffect } from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import Field from '../utils/Field';
import Button from '../utils/Button';
import makeQuerry from '../helper/querry';
import { codeCheck, passwordCheck } from '../helper/regExCheck';
import timeOut from '../helper/timer';

export default function Index({ changePopup }) {
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [confirmPasswordError, onConfirmPasswordError] = useState(false);
  const [error, onError] = useState('');
  const [minutes, setMinutes] = useState(1);
  const [seconds, setSeconds] = useState(30);
  const [succes, onSucces] = useState(false);
  const [send, onSend] = useState(false);
  const [resend, onResend] = useState(false);
  const [show, onShow] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'change-password' });
  const MY_CONSTANT = 'Пароль змінено!!!';
  useEffect(() => {
    if (minutes > 0 || seconds > 0) {
      timeOut(seconds, minutes).then((time) => {
        setSeconds(time.seconds);
        setMinutes(time.minutes);
      });
    }
  }, [seconds, minutes]);
  function checkResponseForResend(response) {
    if (response.status === 200) {
      setMinutes(2);
    } else if (response.status === 404) {
      onError(t('error-server'));
    } else {
      onError(t('error-server2'));
    }
  }

  function handleResendButton() {
    const body = { email: sessionStorage.getItem('email') };
    makeQuerry('resend-confirm-token', JSON.stringify(body))
      .then((response) => {
        checkResponseForResend(response);
      });
  }

  function checkResponse(response) {
    if (response.status === 200) {
      onSucces(true);
    } else if (response.status === 400) {
      onError(t('error-code'));
    } else {
      onError(t('error-server2'));
    }
  }

  function validation() {
    switch (true) {
      case codeCheck(code):
        onError(t('code-error'));
        onCodeError(true);
        return false;
      case passwordCheck(password):
        onError(t('password-error'));
        onPasswordError(true);
        return false;
      case password !== confirmPassword:
        onError(t('confirm-password-error'));
        onConfirmPasswordError(true);
        return false;
      default:
        return true;
    }
  }

  function resetError() {
    onCodeError(false);
    onPasswordError(false);
    onConfirmPasswordError(false);
  }

  function handleSendButton() {
    resetError();
    if (!validation()) {
      onSend(false);
      return;
    }
    const body = {
      token: code,
      password,
      email: sessionStorage.getItem('email'),
      confirmPassword,
    };
    makeQuerry('new-password', JSON.stringify(body))
      .then((response) => {
        onSend(false);
        checkResponse(response);
      });
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
    <div className="confirm">
      <div className="form-body">
        <h1 className="title">{t('title')}</h1>
        {succes && (
        <p className="confirm__success">
          {MY_CONSTANT}
          {' '}
          <p>
            <Link className="link-success" data-testid="" to="/" onClick={() => changePopup(true)}>Натисніть для того щоб авторизуватися</Link>
          </p>
        </p>
        )}
        <p className="confirm__text">{t('confirm-text1')}</p>
        <p className="confirm__text"><b>{t('confirm-text2')}</b></p>
        {error !== '' && <p data-testid="error" className="error">{error}</p>}
        <Field dataTestId="code-input" error={codeError} name={t('code-input-title')} value={code} type="text" onInputChange={onCodeChange} />
        <Field dataTestId="password-input" error={passwordError} name={t('password-input-title')} value={password} type="password" onInputChange={onPasswordChange} tip={t('password-tip')} show={show} onShow={onShow} />
        <Field dataTestId="confirm-password-input" error={confirmPasswordError} name={t('confirm-password-title')} value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} show={show} onShow={onShow} />
        <Button name={send ? 'Обробка...' : t('button-title')} className="confirm__btn" onButton={onSend} disabled={send} dataTestId="change-password-btn" />
        <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={onResend} type="button">{resend ? 'Обробка...' : t('time', { minutes, seconds })}</button>
      </div>
    </div>
  );
}
