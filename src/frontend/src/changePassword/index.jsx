import React, { useState, useEffect } from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import { useTranslation } from 'react-i18next';
import Field from '../components/Field';
import Button from '../components/Button';

export default function Index() {
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [confirmPasswordError, onConfirmPasswordError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [minutes, setMinutes] = useState(10);
  const [seconds, setSeconds] = useState(0);
  const [sendAg, onSendAg] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'change-password' });
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
      setMinutes(10);
      fetch('/sendEmail', {
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
      onCodeError(false);
      onPasswordError(false);
      onConfirmPasswordError(false);
      onButton(false);
      if
      (code.match(/^[a-zA-Z0-9\s]{5}$/) === null) {
        onError(t('code-error'));
        onCodeError(true);
      } else if (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null) {
        onError(t('password-error'));
        onPasswordError(true);
      } else if (password !== confirmPassword) {
        onError(t('confirm-password-error'));
        onConfirmPasswordError(true);
      } else {
        fetch('/newPassword', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            code,
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
    <div className="confirm">
      <h1 className="title">{t('title')}</h1>
      <p className="confirm__text">{t('confirm-text1')}</p>
      <p className="confirm__text"><b>{t('confirm-text2')}</b></p>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field dataTestId="" error={codeError} name={t('code-input-title')} value={code} type="text" onInputChange={onCodeChange} />
      <Field dataTestId="" error={passwordError} name={t('password-input-title')} value={password} type="password" onInputChange={onPasswordChange} tip={t('password-tip')} />
      <Field dataTestId="" error={confirmPasswordError} name={t('confirm-password-title')} value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <Button name={t('button-title')} className="confirm__btn" onButton={onButton} />
      <button className="confirm__send-again" disabled={minutes > 0 || seconds > 0} onClick={() => sendAg(true)} type="button">{t('time', { minutes, seconds })}</button>
    </div>
  );
}
