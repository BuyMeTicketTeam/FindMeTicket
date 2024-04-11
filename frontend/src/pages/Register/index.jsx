/* eslint-disable import/no-extraneous-dependencies */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import { useRegisterMutation } from '../../services/userApi';

import { NAME_PATTERN, EMAIL_PATTERN, PASSWORD_PATTERN } from '../../constants/regex';

import Input from '../../components/Input';
import Checkbox from '../../components/Checkbox';

import './register.scss';

export default function Register() {
  const [showPassword, setShowPassword] = useState(false);
  const [registerQuery, { isLoading, isError, error }] = useRegisterMutation();
  const { register, handleSubmit, formState: { errors } } = useForm({ mode: 'all' });
  const { t } = useTranslation('translation', { keyPrefix: 'register' });
  const navigate = useNavigate();

  async function onSubmit(data) {
    try {
      await registerQuery(data).unwrap();
      navigate('/');
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div data-testid="register" className="register main">
      <form className="form-body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="title">{t('registration')}</h1>
        {isError && <p data-testid="error" className="error">{error}</p>}
        <Input
          id="nickname"
          error={errors.nickname}
          errorMessage={t('login_error')}
          label={t('nickname')}
          placeholder="Svitlana2012"
          otherProps={{ ...register('nickname', { required: true, pattern: NAME_PATTERN }) }}
        />

        <Input
          id="email"
          error={errors.email}
          errorMessage={t('login_error')}
          label={t('email')}
          otherProps={{ ...register('email', { required: true, pattern: EMAIL_PATTERN }) }}
        />

        <Input
          id="password"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.password}
          errorMessage={t('login_error')}
          label={t('password')}
          type="password"
          otherProps={{ ...register('password', { required: true, pattern: PASSWORD_PATTERN }) }}
        />

        <Input
          id="confirmPassword"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.confirmPassword}
          errorMessage={t('login_error')}
          label={t('confirm-password')}
          type="password"
          otherProps={{ ...register('confirmPassword', { required: true }) }}
        />

        <Checkbox
          id="privacyPolicy"
          error={errors.privacyPolicy}
          errorMessage={t('asd')}
          otherProps={{ ...register('privacyPolicy', { required: true }) }}
        >
          {t('agree')}
          <a href="/">{t('privacy policy')}</a>
        </Checkbox>
        <Checkbox
          id="notifications"
          otherProps={{ ...register('notifications') }}
        >
          {t('ticket-alerts-agreement')}
        </Checkbox>

        <button disabled={isLoading} className="button btn-full" type="submit">{isLoading ? t('processing') : t('register')}</button>
      </form>
    </div>
  );
}
