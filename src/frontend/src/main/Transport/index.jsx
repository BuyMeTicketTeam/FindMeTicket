import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import InProgress from '../../InProgress/index';
import './style.css';

function TransportButton({
  label, isActive, onClick, img,
}) {
  return (
    <button
      type="button"
      className={`transport-btn ${isActive ? 'active' : ''}`}
      onClick={onClick}
    >
      <img
        className="transportion"
        src={img}
        alt={label}
      />
      {label}
    </button>
  );
}

function Transport() {
  const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'transport' });
  const [activeButton, setActiveButton] = useState((t('bus')));

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={activeButton === (t('everything'))}
        onClick={() => setIsOpen(true)}
        img="../img/everyting.svg"
      />
      <TransportButton
        label={t('bus')}
        isActive={activeButton === (t('bus'))}
        onClick={() => handleButtonClick(t('bus'))}
        img="../img/bus.svg"
      />
      <TransportButton
        label={t('plane')}
        isActive={activeButton === (t('plane'))}
        onClick={() => setIsOpen(true)}
        img="../img/plane.svg"
      />
      <TransportButton
        label={t('train')}
        isActive={activeButton === (t('train'))}
        onClick={() => setIsOpen(true)}
        img="../img/train.svg"
      />
      <TransportButton
        label={t('ferry')}
        isActive={activeButton === (t('ferry'))}
        onClick={() => setIsOpen(true)}
        img="../img/boat.svg"
      />
      {isOpen && <InProgress title="Ця функція знаходиться в розробці ):" text="Ми постійно працюємо над покращенням продукту. Як тільки ця функція буде доступна, ми вам повідомимо." setIsOpen={setIsOpen} />}
    </div>
  );
}

export default Transport;
