import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

import { useChangePasswordMutation, useLazyLogoutQuery } from '../../services/userApi';
import { PASSWORD_PATTERN } from '../../constants/regex';

import Input from '../../common/Input';
import SuccessPopup from '../../common/SuccessPopup';

export default function ChangePassword() {
  const [showPassword, setShowPassword] = useState(false);
  const {
    register, handleSubmit, setError, formState: { errors },
  } = useForm({ mode: 'onTouched' });
  const [changePassword, {
    isSuccess, isLoading, isError, error,
  }] = useChangePasswordMutation();
  const [logout] = useLazyLogoutQuery();
  const { t } = useTranslation('translation', { keyPrefix: 'changePassword' });

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
      await changePassword(data).unwrap();
      logout().unwrap();
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div className="block-center main">
      <form className="block-center__body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="block-center__title">{t('title')}</h1>
        {isSuccess && <SuccessPopup t={t} />}
        {isError && <p data-testid="error" className="error">{t([`error_${error.status}`, 'error_500'])}</p>}
        <Input
          id="lastPassword"
          error={errors.lastPassword}
          errorMessage={t('password_error')}
          label={t('last_password')}
          otherProps={{ ...register('lastPassword', { required: true, pattern: PASSWORD_PATTERN }) }}
        />

        <Input
          id="password"
          showPassword={showPassword}
          type="password"
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
          type="password"
          error={errors.confirmPassword}
          errorMessage={t('confirm_password_error')}
          label={t('confirm_password')}
          otherProps={{ ...register('confirmPassword', { required: true }) }}
        />

        <button
          disabled={isLoading || isSuccess}
          className="button button_long"
          type="submit"
        >
          {isLoading ? t('processing') : t('send')}
        </button>
      </form>
    </div>
  );
}
