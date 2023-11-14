/* eslint-disable react/jsx-no-bind */
import React, { useState } from 'react';
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
  const navigate = useNavigate();
  function handleNicknameChange(value) {
    onNicknameChange(value);
    onNicknameError(false);
  }
  function sendRequest(body) {
    makeQuerry('register', JSON.stringify(body))
      .then((response) => {
        if (response.status === 201) {
          navigate('/confirm');
          sessionStorage.setItem('email', email);
        } else if (response.status === 409) {
          onError('Користувач з ціїю електронною адресою вже зареєстрований');
        } else if (response.status === 404) {
          onError("З'єднання з сервером відсутнє");
        } else {
          onError('Помилка серверу. Спробуйте ще раз');
        }
      });
  }
  function handleClick() {
    onNicknameError(false);
    onEmailError(false);
    onPasswordError(false);
    onConfirmPasswordError(false);
    if (nicknameCheck(nickname)) {
      onError(t('nickname-error'));
      onNicknameError(true);
      return;
    }
    if (emailCheck(email)) {
      onError(t('email-error'));
      onEmailError(true);
      return;
    }
    if (passwordCheck(password)) {
      onError(t('password-error'));
      onPasswordError(true);
      return;
    }
    if (password !== confirmPassword) {
      onError(t('confirm-password-error'));
      onConfirmPasswordError(true);
      return;
    }
    if (!policy) {
      onError(t('privacy-policy'));
      onErrorPolicy(true);
      return;
    }
    const body = {
      email,
      password,
      nickname,
    };
    sendRequest(body);
  }

  return (
    <div data-testid="register" className="register">
      <h1 className="title">{t('registration')}</h1>
      {error !== '' && <p data-testid="error" className="error">{error}</p>}
      <Field error={nicknameError} dataTestId="nickname-input" tipDataTestId="nickname-tip" name={t('nickname')} value={nickname} type="text" onInputChange={(value) => handleNicknameChange(value)} placeholder="Svillana2012" tip={<ListTip />} />
      <Field error={emailError} dataTestId="email-input" name={t('email')} value={email} type="email" onInputChange={onEmailChange} tip={t('tip-email')} placeholder="mail@mail.com" />
      <Field error={passwordError} dataTestId="password-input" name={t('password')} value={password} type="password" onInputChange={onPasswordChange} tip={t('tip-password')} />
      <Field error={confirmPasswordError} dataTestId="confirm-pass-input" name={t('confirm-password')} value={confirmPassword} type="password" onInputChange={onConfirmPasswordChange} />
      <input data-testid="checkbox" id="policy" type="checkbox" className={errorPolicy ? 'checkbox__field checkbox-error' : 'checkbox__field'} onClick={() => { onPolicy(!policy); onErrorPolicy(false); }} />
      <label htmlFor="policy" className="checkbox">
        {t('agree')}
        <a href="/">{t('privacy policy')}</a>
      </label>
      <Button dataTestId="register-btn" name={t('register')} onButton={() => handleClick()} />
    </div>
  );
}
