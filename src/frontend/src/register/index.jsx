import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Field from '../components/Field';
import Button from '../components/Button';
import ListTip from './ListTip';
import './register.css';

export default function Register() {
  const { t } = useTranslation('translation', { keyPrefix: 'register' });
  const [nickname, onNicknameChange] = useState('');
  const [nicknameError, onNicknameError] = useState(false);
  const [email, onEmailChange] = useState('');
  const [emailError, onEmailError] = useState(false);
  const [password, onPasswordChange] = useState('');
  const [passwordError, onPasswordError] = useState(false);
  const [confirmPassword, onConfirmPasswordChange] = useState('');
  const [confirmPasswordError, onConfirmPasswordError] = useState(false);
  const [button, onButton] = useState(false);
  const [error, onError] = useState('');
  const [policy, onPolicy] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    if (button && policy) {
      fetch('/userRegister', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname,
          email,
          password,
          confirmPassword,
        }),
      })
        .then((response) => {
          if (response.ok) {
            onError('Some error');
            onButton(false);
          } else {
            navigate('/confirm');
          }
        });
    } else if (button && !policy) {
      onButton(false);
      onError(t('privacy-policy'));
    }
  }, [button]);
  useEffect(() => {
    if (button === true) {
      onNicknameError(false);
      onEmailError(false);
      onPasswordError(false);
      onConfirmPasswordError(false);
      onButton(false);
      if
      (nickname.match(/^[a-zA-Z0-9\s]{5,20}$/) === null) {
        onError(t('nickname-error'));
        onNicknameError(true);
      } else if
      (email.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/) === null) {
        onError(t('email-error'));
        onEmailError(true);
      } else if (password.match(/^(?=.*[A-Za-z])(?=.*\d).{8,30}$/) === null) {
        onError(t('password-error'));
        onPasswordError(true);
      } else if (password !== confirmPassword) {
        onError(t('confirm-password-error'));
        onConfirmPasswordError(true);
      } else {
        fetch('/userRegister', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            nickname,
            email,
            password,
            confirmPassword,
          }),
        });
      }
    }
  }, [button]);

  return (
    <div data-testid="register" className="register">
      <h1 className="title">{t('registration')}</h1>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field error={nicknameError} dataTestId="nickname-input" tipDataTestId="nickname-tip" name={t('nickname')} value={nickname} type="text" onInputChange={onNicknameChange} placeholder="Svillana2012" tip={<ListTip />} />
      <Field error={emailError} dataTestId="email-input" name={t('email')} value={email} type="email" onInputChange={onEmailChange} tip={t('tip-email')} />
      <Field error={passwordError} dataTestId="password-input" name={t('password')} value={password} type="password" onInputChange={onPasswordChange} tip={t('tip-password')}  />
      <Field error={confirmPasswordError} dataTestId="confirm-pass-input" name={t('confirm-password')} value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <input data-testid="checkbox" id="policy" type="checkbox" className="checkbox__field" onClick={() => onPolicy(!policy)} />
      <label htmlFor="policy" className="checkbox">
      {t('agree')}
        <a href="/">{t('privacy policy')}</a>
      </label>
      <Button dataTestId="register-btn" name="Register" onButton={onButton} />
    </div>
  );
}
