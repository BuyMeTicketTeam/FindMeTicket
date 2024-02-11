import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import InProgress from '../../InProgress/index';
import './transport.scss';
import makeQuerry from '../../helper/querry';
import {
  busIcon, trainIcon, planeIcon, boatIcon, everythingIcon,
} from './transport-img/img';

function TransportButton({
  label, isActive, onClick, img, disabled,
}) {
  return (
    <button
      type="button"
      className={`transport-btn ${isActive ? 'active' : ''} ${disabled ? 'disabled' : ''}`}
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

function Transport({
  setTicketsData, selectedTransport, setSelectedTransport, requestBody, ticketsData,
}) {
  const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'transport' });

  const handleButtonClick = (button) => {
    setSelectedTransport((prevSelectedTransport) => {
      if (button === 'all') {
        return (
          {
            ...prevSelectedTransport,
            bus: true,
            train: true,
          }
        );
      }
      return (
        {
          ...Object.keys(prevSelectedTransport).reduce((acc, key) => {
            acc[key] = false;
            return acc;
          }, {}),
          [button]: true,
        }
      );
    });
  };

  useEffect(() => {
    const selectedCount = Object.values(selectedTransport).filter((value) => value).length;
    if (selectedCount > 0 && ticketsData > 0) {
      const body = {
        ...requestBody,
        ...selectedTransport,
      };
      makeQuerry('selectedTransport', body)
        .then((response) => {
          setTicketsData(response.body);
        })
        .catch((error) => {
          console.error('Error fetching data:', error);
        });
    } else if (selectedCount === 0) {
      handleButtonClick('all');
    }
  }, [selectedTransport]);

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={selectedTransport.bus && selectedTransport.train}
        onClick={() => handleButtonClick('all')}
        img={everythingIcon}
      />
      <TransportButton
        label={t('bus')}
        isActive={selectedTransport.bus}
        onClick={() => handleButtonClick('bus')}
        img={busIcon}
      />
      <TransportButton
        label={t('train')}
        isActive={selectedTransport.train}
        onClick={() => handleButtonClick('train')}
        img={trainIcon}
      />
      <TransportButton
        label={t('plane')}
        isActive={selectedTransport.airplane}
        onClick={() => setIsOpen(true)}
        img={planeIcon}
        disabled
      />
      <TransportButton
        label={t('ferry')}
        isActive={selectedTransport.ferry}
        onClick={() => setIsOpen(true)}
        img={boatIcon}
        disabled
      />
      {isOpen && <InProgress title={t('message-title')} text={t('message-text')} setIsOpen={setIsOpen} />}
    </div>
  );
}

export default Transport;
