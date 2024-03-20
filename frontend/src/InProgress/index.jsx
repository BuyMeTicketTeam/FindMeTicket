import React, { useRef } from 'react';
import { useTranslation } from 'react-i18next';
import Button from '../utils/Button';
import './inProgress.scss';

export default function Index({
  title, text, setIsOpen,
}) {
  const { t } = useTranslation('translation', { keyPrefix: 'in-progress' });
  const inProgressRef = useRef(null);

  return (
    <div className="message background">
      <div ref={inProgressRef} className="message__content">
        <div className="message__header">
          <h2 className="message__title">{title}</h2>
          <button
            type="button"
            className="message__close close"
            aria-label="Close"
            onClick={() => setIsOpen(false)}
          />
        </div>
        <p className="message__text">{text}</p>
        <Button
          className="message__button"
          name={t('ok')}
          onButton={() => setIsOpen(false)}
        />
      </div>
    </div>
  );
}
