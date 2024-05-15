/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/named */
import React from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import { useResetMutation } from '../../services/userApi';
import { EMAIL_PATTERN } from '../../constants/regex';

import Input from '../../common/Input';

import './reset.scss';

export default function Reset() {
  const [reset, {
    isSuccess, isLoading, isError, error,
  }] = useResetMutation();
  const {
    register, handleSubmit, formState: { errors },
  } = useForm({ mode: 'onTouched' });
  const { t } = useTranslation('translation', { keyPrefix: 'reset' });
  const navigate = useNavigate();

  async function onSubmit(data) {
    try {
      await reset(data).unwrap();
      navigate('/reset-password', { state: { email: data.email } });
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div className="reset block-center main">
      <form className="block-center__body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="block-center__title">{t('password_reset')}</h1>
        <p className="block-center__text margin_bottom">{t('email')}</p>

        {isError && <p data-testid="error" className="error">{t([`error_${error.status}`, 'error_500'])}</p>}

        <Input
          id="email"
          error={errors.email}
          errorMessage={t('email_error')}
          otherProps={{ ...register('email', { required: true, pattern: EMAIL_PATTERN }) }}
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
