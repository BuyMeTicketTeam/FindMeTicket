import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Input from '../components/Input';
import Button from '../components/Button';
import './reset.css';

export default function Index() {
  const { t } = useTranslation('translation', { keyPrefix: 'reset' });
  const [code, onCodeChange] = useState('');
  const [codeError, onCodeError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
    onCodeError(false);
    onButton(false);
    if (button === true) {
      if (code.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError(t('reset-error'));
        onCodeError(true);
      } else {
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
            if (response.ok) {
              onError('Some error');
            } else {
              navigate('/change-password');
            }
          });
      }
    }
  }, [button]);
  return (
    <div className="reset">
      <h1 className="title">{t('password-reset')}</h1>
      <p className="reset__text">{t('email')}</p>
      <Input error={codeError} value={code} onInputChange={onCodeChange} type="text" placeholder="mail@mail.com" />
      {error !== '' && <p className="reset__error">{error}</p>}
      <Button name={t('send')} className="reset__btn" onButton={onButton} />
    </div>
  );
}
