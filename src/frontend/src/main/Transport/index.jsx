import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import InProgress from '../../InProgress/index';
import './style.css';
import {
  busIcon, trainIcon, planeIcon, boatIcon, everythingIcon,
} from './transport-img/img';

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
  const [activeButton, setActiveButton] = useState('bus');

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={activeButton === (t('everything'))}
        onClick={() => setIsOpen(true)}
        img={everythingIcon}
      />
      <TransportButton
        label={t('bus')}
        isActive={activeButton === 'bus'}
        onClick={() => handleButtonClick('bus')}
        img={busIcon}
      />
      <TransportButton
        label={t('plane')}
        isActive={activeButton === 'plane'}
        onClick={() => setIsOpen(true)}
        img={planeIcon}
      />
      <TransportButton
        label={t('train')}
        isActive={activeButton === 'train'}
        onClick={() => setIsOpen(true)}
        img={trainIcon}
      />
      <TransportButton
        label={t('ferry')}
        isActive={activeButton === 'ferry'}
        onClick={() => setIsOpen(true)}
        img={boatIcon}
      />
      {isOpen && <InProgress title={t('message-title')} text={t('message-text')} setIsOpen={setIsOpen} />}
    </div>
  );
}

export default Transport;
