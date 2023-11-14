import React, { useState } from 'react';
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
  const [codeError, onCodeError] = useState(false);
  const [error, onError] = useState('');
  const navigate = useNavigate();
  function sendRequest() {
    makeQuerry('reset', JSON.stringify(email))
      .then((response) => {
        if (response.status === 202) {
          navigate('/change-password');
          sessionStorage.setItem('email', email);
        } else if (response.status === 404) {
          onError("З'єднання з сервером відсутнє");
        } else {
          onError('Помилка серверу. Спробуйте ще раз');
        }
      });
  }
  function handleButton() {
    onCodeError(false);
    if (emailCheck(email)) {
      onError(t('reset-error'));
      onCodeError(true);
      return;
    }
    sendRequest();
  }
  return (
    <div className="reset">
      <h1 className="title">{t('password-reset')}</h1>
      <p className="reset__text">{t('email')}</p>
      <Input error={codeError} value={email} onInputChange={onEmailChange} type="text" placeholder="mail@mail.com" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name={t('send')} className="reset__btn" onButton={() => handleButton()} />
    </div>
  );
}
