import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Input from '../components/utlis/Input';
import Button from '../components/utlis/Button';
import makeQuerry from '../helper/querry';
import './reset.css';
import { emailCheck } from '../helper/regExCheck';

export default function Index() {
  const { t } = useTranslation('translation', { keyPrefix: 'reset' });
  const [email, onEmailChange] = useState('');
  const [emailError, onEmailError] = useState(false);
  const [error, onError] = useState('');
  const [send, onSend] = useState(false);
  const navigate = useNavigate();
  function statusChecks(response) {
    if (response.status === 200) {
      navigate('/change-password');
      sessionStorage.setItem('email', email);
    } else if (response.status === 404) {
      onError("З'єднання з сервером відсутнє");
    } else {
      onError('Помилка серверу. Спробуйте ще раз');
    }
  }
  function validation() {
    if (emailCheck(email)) {
      onSend(false);
      onError(t('reset-error'));
      onEmailError(true);
      return false;
    }
    return true;
  }
  function handleButton() {
    onEmailError(false);
    if (!validation) {
      return;
    }
    makeQuerry('reset', JSON.stringify(email))
      .then((response) => {
        onSend(false);
        statusChecks(response);
      });
  }
  function handleEmailChange(value) {
    onEmailChange(value);
    onEmailError(false);
  }
  useEffect(() => {
    if (send) {
      handleButton();
    }
  }, [send]);
  return (
    <div className="reset">
      <h1 className="title">{t('password-reset')}</h1>
      <p className="reset__text">{t('email')}</p>
      <Input error={emailError} value={email} onInputChange={(value) => handleEmailChange(value)} type="text" placeholder="mail@mail.com" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name={send ? 'Обробка...' : t('send')} className="reset__btn" onButton={onSend} />
    </div>
  );
}
