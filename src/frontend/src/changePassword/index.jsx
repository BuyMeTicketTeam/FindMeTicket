import React, { useState, useEffect } from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import { useTranslation } from 'react-i18next';
import Field from '../components/Field';
import Button from '../components/Button';
import makeQuerry from '../helper/querry';

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
  const [succes, onSucces] = useState(false);
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
        const body = {
          code,
          password,
          email: sessionStorage.getItem('email'),
        };
        makeQuerry('new-password', JSON.stringify(body))
          .then((response) => {
            if (response.status === 200) {
              onSucces(true);
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
    <div className="confirm">
      <h1 className="title">{t('title')}</h1>
      {succes && <p className="confirm__success">Пароль змінено!!!</p>}
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
