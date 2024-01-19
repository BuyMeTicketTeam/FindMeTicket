import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Field from '../utils/Field';
import Button from '../utils/Button';
import ListTip from './ListTip';
import makeQuerry from '../helper/querry';
import Checkbox from '../utils/Checkbox';
import { nicknameCheck, emailCheck, passwordCheck } from '../helper/regExCheck';
import './register.scss';

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
  const [policyError, onErrorPolicy] = useState(false);
  const [send, onSend] = useState(false);
  const [show, onShow] = useState(false);
  const navigate = useNavigate();

  function validation() {
    switch (true) {
      case nicknameCheck(nickname):
        onError(t('nickname-error'));
        onNicknameError(true);
        return false;

      case emailCheck(email):
        onError(t('email-error'));
        onEmailError(true);
        return false;

      case passwordCheck(password):
        onError(t('password-error'));
        onPasswordError(true);
        return false;

      case password !== confirmPassword:
        onError(t('confirm-password-error'));
        onConfirmPasswordError(true);
        return false;

      case !policy:
        onError(t('privacy-policy'));
        onErrorPolicy(true);
        return false;

      default:
        return true;
    }
  }

  function responseStatus(response) {
    switch (response.status) {
      case 200:
        navigate('/confirm');
        sessionStorage.setItem('email', email);
        break;
      case 409:
        onError(t('error-email'));
        break;
      case 418:
        onError(t('error-nickname-exist'));
        break;
      default:
        onError(t('error-again'));
        break;
    }
  }

  function handleClick() {
    onError('');
    if (!validation()) {
      onSend(false);
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
      <div className="form-body">
        <h1 className="title">{t('registration')}</h1>
        {error !== '' && <p data-testid="error" className="error">{error}</p>}
        <Field
          error={nicknameError}
          dataTestId="nickname-input"
          tipDataTestId="nickname-tip"
          name={t('nickname')}
          value={nickname}
          type="text"
          onInputChange={(value) => handleNicknameChange(value)}
          placeholder="Svillana2012"
          tip={<ListTip />}
        />

        <Field
          error={emailError}
          dataTestId="email-input"
          name={t('email')}
          value={email}
          type="email"
          onInputChange={(value) => handleEmailChange(value)}
          tip={t('tip-email')}
          placeholder="mail@mail.com"
        />

        <Field
          error={passwordError}
          dataTestId="password-input"
          name={t('password')}
          value={password}
          type="password"
          onInputChange={(value) => handlePasswordChange(value)}
          tip={t('tip-password')}
          show={show}
          onShow={onShow}
        />

        <Field
          error={confirmPasswordError}
          dataTestId="confirm-pass-input"
          name={t('confirm-password')}
          value={confirmPassword}
          type="password"
          onInputChange={(value) => handleConfirmPasswordChange(value)}
          show={show}
          onShow={onShow}
        />
        <Checkbox
          dataTestid="checkbox"
          policyError={policyError}
          onChange={() => { onPolicy(!policy); onErrorPolicy(false); }}
        >
          {t('agree')}
          <a href="/">{t('privacy policy')}</a>
        </Checkbox>
        <Button
          dataTestId="register-btn"
          className="btn-full"
          disabled={send}
          name={send ? t('processing') : t('register')}
          onButton={onSend}
        />
      </div>
    </div>
  );
}
