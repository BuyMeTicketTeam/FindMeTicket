import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
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
        onClick={() => handleButtonClick(t('everything'))}
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
        onClick={() => handleButtonClick(t('plane'))}
        img="../img/plane.svg"
      />
      <TransportButton
        label={t('train')}
        isActive={activeButton === (t('train'))}
        onClick={() => handleButtonClick(t('train'))}
        img="../img/train.svg"
      />
      <TransportButton
        label={t('ferry')}
        isActive={activeButton === (t('ferry'))}
        onClick={() => handleButtonClick(t('ferry'))}
        img="../img/boat.svg"
      />
    </div>
  );
}

export default Transport;
