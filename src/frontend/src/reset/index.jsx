import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Input from '../components/Input';
import Button from '../components/Button';
import makeQuerry from '../helper/querry';
import './reset.css';

export default function Index() {
  const { t } = useTranslation('translation', { keyPrefix: 'reset' });
  const [email, onEmailChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
    onCodeError(false);
    onButton(false);
    if (button === true) {
      if (email.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError(t('reset-error'));
        onCodeError(true);
      } else {
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
    }
  }, [button]);
  return (
    <div className="reset">
      <h1 className="title">{t('password-reset')}</h1>
      <p className="reset__text">{t('email')}</p>
      <Input error={codeError} value={email} onInputChange={onEmailChange} type="text" placeholder="mail@mail.com" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name={t('send')} className="reset__btn" onButton={onButton} />
    </div>
  );
}
