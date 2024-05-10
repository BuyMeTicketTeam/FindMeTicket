/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-nested-ternary */
/* eslint-disable no-param-reassign */
/* eslint-disable no-return-assign */
/* eslint-disable import/named */
import React from 'react';
import { useLocation, Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';

import useTimeout from '../../hook/useTimeout';
import { useConfirmMutation, useResendConfirmTokenMutation } from '../../services/userApi';
import { CODE_PATTERN } from '../../constants/regex';

import Input from '../../common/Input';
import SuccessPopup from '../../common/SuccessPopup';

import './confirm.scss';

export default function Confirm() {
  const [confirm, {
    isSuccess, isLoading, isError, error,
  }] = useConfirmMutation();
  const [resendConfirmToken, { isLoading: isResendLoading }] = useResendConfirmTokenMutation();
  const {
    register, handleSubmit, formState: { errors },
  } = useForm({ mode: 'onTouched' });
  const { state } = useLocation();
  const { t } = useTranslation('translation', { keyPrefix: 'confirm' });
  const { minutes, seconds, restart } = useTimeout(1, 30);

  const resendButtonIsDisabled = (minutes > 0 || seconds > 0) || isSuccess || isResendLoading;

  async function onSubmit(data) {
    try {
      const payload = {
        token: data.token,
        email: state.email,
      };
      await confirm(payload).unwrap();
    } catch (err) {
      console.error({ error: err });
    }
  }

  async function handleResendToken() {
    try {
      const payload = {
        email: state.email,
      };
      await resendConfirmToken(payload).unwrap();
      restart();
    } catch (err) {
      console.error({ error: err });
    }
  }

  if (!state || !state.email) {
    return <Navigate to="/" />;
  }

  return (
    <div data-testid="confirm" className="confirm block-center main">
      <form className="block-center__body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="block-center__title">{t('confirm_email')}</h1>
        {isSuccess && <SuccessPopup t={t} />}
        <p className="block-center__text">{t('confirm_code')}</p>
        <p className="block-center__text margin_bottom"><b>{t('confirm_ten')}</b></p>
        <Input
          id="code"
          error={errors.token}
          errorMessage={t('error_400')}
          label={t('code')}
          otherProps={{
            ...register('token', {
              required: true,
              pattern: CODE_PATTERN,
            }),
            onChange: (event) => event.target.value = event.target.value.toUpperCase(),
          }}
        />
        {isError && <p data-testid="error" className="confirm__error">{t([`error_${error.status}`, 'error_500'])}</p>}
        <div className="block-center__row">
          <button
            disabled={isLoading || isSuccess}
            className="button"
            type="submit"
          >
            {isLoading ? t('processing') : t('send')}
          </button>
          <button
            data-testid="send-again-btn"
            className="button type_link"
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
        </div>
      </form>
    </div>
  );
}
