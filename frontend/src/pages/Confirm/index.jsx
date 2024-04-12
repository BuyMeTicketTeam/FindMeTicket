import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { useConfirmMutation } from '../../services/userApi';

export default function Confirm() {
  const [confirm, { isSuccess, isLoading, isError, error }] = useConfirmMutation();
  const { t } = useTranslation('translation', { keyPrefix: 'confirm' });

  return (
    <div data-testid="confirm" className="confirm main">
      <div className="form-body">
        <h1 className="title">{t('confirm-email')}</h1>
        {isSuccess && (
        <div className="confirm__success">
          {t('success-message')}
          {' '}
          <p>
            <Link
              className="link-success"
              data-testid=""
              to="/login"
            >
              {t('auth-link')}
            </Link>
          </p>
        </div>
        )}
        <p>{t('confirm-code')}</p>
        <p className="confirm__text"><b>{t('confirm-ten')}</b></p>
        <Input
          error={codeError}
          dataTestId="confirm-input"
          value={code}
          onInputChange={(value) => handleCodeChange(value)}
          type="text"
        />
        {error !== '' && <p data-testid="error" className="confirm__error">{error}</p>}
        <div className="row">
          <Button
            name={send ? t('processing') : t('send')}
            disabled={sendButtonIsDisabled}
            onButton={setSend}
            dataTestId="confirm-btn"
          />

          <button
            data-testid="send-again-btn"
            className="confirm__send-again"
            disabled={resendButtonIsDisabled}
            onClick={setResend}
            type="button"
          >
            {resend ? t('processing') : t('time', { minutes, seconds })}
          </button>
        </div>
      </div>
    </div>
  )
}
