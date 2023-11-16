/* eslint-disable react/jsx-no-bind */
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { nicknameCheck, emailCheck, passwordCheck } from '../helper/regExCheck';
import Field from '../components/utlis/Field';
import Button from '../components/utlis/Button';
import ListTip from './ListTip';
import makeQuerry from '../helper/querry';
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
  const [error, onError] = useState('');
  const [policy, onPolicy] = useState(false);
  const [errorPolicy, onErrorPolicy] = useState(false);
  const [send, onSend] = useState(false);
  const navigate = useNavigate();
  function validation() {
    if (nicknameCheck(nickname)) {
      onSend(false);
      onError(t('nickname-error'));
      onNicknameError(true);
      return false;
    }
    if (emailCheck(email)) {
      onSend(false);
      onError(t('email-error'));
      onEmailError(true);
      return false;
    }
    if (passwordCheck(password)) {
      onSend(false);
      onError(t('password-error'));
      onPasswordError(true);
      return false;
    }
    if (password !== confirmPassword) {
      onSend(false);
      onError(t('confirm-password-error'));
      onConfirmPasswordError(true);
      return false;
    }
    if (!policy) {
      onSend(false);
      onError(t('privacy-policy'));
      onErrorPolicy(true);
      return false;
    }
    return true;
  }
  function resetErrors() {
    onNicknameError(false);
    onEmailError(false);
    onPasswordError(false);
    onConfirmPasswordError(false);
  }
  function responseStatus(response) {
    if (response.status === 200) {
      navigate('/confirm');
      sessionStorage.setItem('email', email);
    } else if (response.status === 409) {
      onError('Користувач з ціїю електронною адресою вже зареєстрований');
    } else if (response.status === 404) {
      onError("З'єднання з сервером відсутнє");
    } else {
      onError('Помилка серверу. Спробуйте ще раз');
    }
  }
  function handleClick() {
    resetErrors();
    if (!validation()) {
      return;
    }
    const body = {
      email,
      password,
      username: nickname,
      confirmPassword,
    };
    makeQuerry('register', JSON.stringify(body))
      .then((response) => {
        onSend(false);
        responseStatus(response);
      });
  }
  function handleNicknameChange(value) {
    onNicknameChange(value);
    onNicknameError(false);
  }
  function handleEmailChange(value) {
    onEmailChange(value);
    onEmailError(false);
  }
  function handlePasswordChange(value) {
    onPasswordChange(value);
    onPasswordError(false);
  }
  function handleConfirmPasswordChange(value) {
    onConfirmPasswordChange(value);
    onConfirmPasswordError(false);
  }
  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);
  return (
    <div data-testid="register" className="register">
      <h1 className="title">{t('registration')}</h1>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field error={nicknameError} dataTestId="nickname-input" tipDataTestId="nickname-tip" name={t('nickname')} value={nickname} type="text" onInputChange={(value) => handleNicknameChange(value)} placeholder="Svillana2012" tip={<ListTip />} />
      <Field error={emailError} dataTestId="email-input" name={t('email')} value={email} type="email" onInputChange={(value) => handleEmailChange(value)} tip={t('tip-email')} placeholder="mail@mail.com" />
      <Field error={passwordError} dataTestId="password-input" name={t('password')} value={password} type="password" onInputChange={(value) => handlePasswordChange(value)} tip={t('tip-password')} />
      <Field error={confirmPasswordError} dataTestId="confirm-pass-input" name={t('confirm-password')} value={confirmPassword} type="password" onInputChange={(value) => handleConfirmPasswordChange(value)} />
      <input data-testid="checkbox" id="policy" type="checkbox" className={errorPolicy ? 'checkbox__field checkbox-error' : 'checkbox__field'} onClick={() => { onPolicy(!policy); onErrorPolicy(false); }} />
      <label htmlFor="policy" className="checkbox">
        {t('agree')}
        <a href="/">{t('privacy policy')}</a>
      </label>
      <Button dataTestId="register-btn" name={send ? 'Обробка...' : t('register')} onButton={onSend} />
    </div>
  );
}
