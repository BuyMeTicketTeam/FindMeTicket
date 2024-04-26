/* eslint-disable no-nested-ternary */
import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';

import { useNewPasswordMutation, useResendConfirmResetTokenMutation } from '../../services/userApi';
import useTimeout from '../../hook/useTimeout';
import { CODE_PATTERN, PASSWORD_PATTERN } from '../../constants/regex';

import Input from '../../components/Input';

export default function ConfirmReset() {
  const [showPassword, setShowPassword] = useState();
  const { state } = useLocation();
  const { minutes, seconds, restart } = useTimeout(1, 30);
  const [newPassword, {
    isSuccess, isLoading, isError, error,
  }] = useNewPasswordMutation();
  const [resendResetToken, { isLoading: isResendLoading }] = useResendConfirmResetTokenMutation();
  const { t } = useTranslation('translation', { keyPrefix: 'confirmReset' });
  const {
    register, handleSubmit, setError, formState: { errors },
  } = useForm({ mode: 'onBlur' });

  const resendButtonIsDisabled = (minutes > 0 || seconds > 0) || isSuccess || isResendLoading;

  function passwordValidation(data) {
    if (data.password !== data.confirmPassword) {
      setError('confirmPassword', {
        type: 'manual',
      });
      return false;
    }
    return true;
  }

  async function handleResendToken() {
    try {
      const payload = {
        email: state.email,
      };
      await resendResetToken(payload).unwrap();
      restart();
    } catch (err) {
      console.error({ error: err });
    }
  }

  async function onSubmit(data) {
    if (!passwordValidation(data)) {
      return;
    }
    try {
      const payload = {
        token: data.code,
        email: state.email,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };
      await newPassword(payload).unwrap();
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div className="confirm main">
      <form className="form-body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="title">{t('title')}</h1>
        {isSuccess && (
        <p className="confirm__success">
          {t('success_message')}
          {' '}
          <Link
            className="link-success"
            data-testid=""
            to="/login"
            state={{ successNavigate: '/', closeNavigate: '/' }}
          >
            {t('auth_link')}
          </Link>
        </p>
        )}
        <p className="confirm__text">{t('confirm_text1')}</p>
        <p className="confirm__text"><b>{t('confirm_text2')}</b></p>
        {isError && <p data-testid="error" className="error">{t([`error_${error.originalStatus}`, 'error_500'])}</p>}

        <Input
          id="code"
          error={errors.code}
          errorMessage={t('error_400')}
          label={t('code')}
          otherProps={{ ...register('code', { required: true, pattern: CODE_PATTERN }) }}
        />

        <Input
          id="password"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.password}
          errorMessage={t('password_error')}
          label={t('password')}
          otherProps={{ ...register('password', { required: true, pattern: PASSWORD_PATTERN }) }}
        />

        <Input
          id="confirmPassword"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.confirmPassword}
          errorMessage={t('confirm_password_error')}
          label={t('confirm_password')}
          otherProps={{ ...register('confirmPassword', { required: true }) }}
        />

        <button
          disabled={isLoading || isSuccess}
          className="button"
          type="submit"
        >
          {isLoading ? t('processing') : t('send')}
        </button>

        <button
          data-testid="confirm-send-btn"
          className="confirm__send-again"
          disabled={resendButtonIsDisabled}
          onClick={handleResendToken}
          type="button"
        >
          {isResendLoading
            ? t('processing')
            : ((minutes === 0 && seconds === 0)
              ? t('send_letter')
              : t('time', { minutes, seconds }))}
        </button>
      </form>
    </div>
  );
}
