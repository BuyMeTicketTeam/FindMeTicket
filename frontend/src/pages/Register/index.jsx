/* eslint-disable import/no-extraneous-dependencies */
import React, { useState } from 'react';
import { useTranslation, Trans } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import { useRegisterMutation } from '../../services/userApi';

import { NAME_PATTERN, EMAIL_PATTERN, PASSWORD_PATTERN } from '../../constants/regex';

import Input from '../../common/Input';
import Checkbox from '../../common/Checkbox';

import './register.scss';

export default function Register() {
  const [showPassword, setShowPassword] = useState(false);
  const [registerQuery, { isLoading, isError, error }] = useRegisterMutation();
  const {
    register, handleSubmit, setError, formState: { errors },
  } = useForm({ mode: 'onTouched' });
  const { t } = useTranslation('translation', { keyPrefix: 'registration' });
  const navigate = useNavigate();

  function passwordValidation(data) {
    if (data.password !== data.confirmPassword) {
      setError('confirmPassword', {
        type: 'manual',
      });
      return false;
    }
    return true;
  }

  async function onSubmit(data) {
    if (!passwordValidation(data)) {
      return;
    }
    try {
      const payload = {
        email: data.email,
        password: data.password,
        username: data.username,
        confirmPassword: data.confirmPassword,
        notification: data.notifications,
      };
      await registerQuery(payload).unwrap();
      navigate('/confirm', { state: { email: data.email } });
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div data-testid="register" className="register block-center main">
      <form className="block-center__body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="block-center__title">{t('registration')}</h1>
        {isError && <p data-testid="error" className="error">{t([`error_${error.status}`, 'error_500'])}</p>}
        <Input
          id="username"
          error={errors.username}
          errorMessage={t('username_error')}
          label={t('username')}
          placeholder="Svitlana2012"
          otherProps={{ ...register('username', { required: true, pattern: NAME_PATTERN }) }}
        />

        <Input
          id="email"
          error={errors.email}
          errorMessage={t('email_error')}
          label={t('email')}
          otherProps={{ ...register('email', { required: true, pattern: EMAIL_PATTERN }) }}
        />

        <Input
          id="password"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.password}
          errorMessage={t('password_error')}
          label={t('password')}
          type="password"
          otherProps={{ ...register('password', { required: true, pattern: PASSWORD_PATTERN }) }}
        />

        <Input
          id="confirmPassword"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.confirmPassword}
          errorMessage={t('confirm_password_error')}
          label={t('confirm_password')}
          type="password"
          otherProps={{ ...register('confirmPassword', { required: true }) }}
        />

        <Checkbox
          id="privacyPolicy"
          error={errors.privacyPolicy}
          errorMessage={t('privacy_policy')}
          otherProps={{ ...register('privacyPolicy', { required: true }) }}
        >
          <Trans i18nKey="registration.privacy_policy_title"><a href="/">privacy_policy_title</a></Trans>
        </Checkbox>
        <Checkbox
          id="notifications"
          otherProps={{ ...register('notifications') }}
        >
          {t('ticket_alerts_agreement')}
        </Checkbox>

        <button
          disabled={isLoading}
          className="button button_long"
          type="submit"
        >
          {isLoading ? t('processing') : t('register')}
        </button>
      </form>
    </div>
  );
}
