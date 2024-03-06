import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate, useLocation } from 'react-router-dom';
import InProgress from '../../InProgress/index';
import './transport.scss';
import makeQuerry from '../../helper/querry';
import {
  busIcon, trainIcon, planeIcon, boatIcon, everythingIcon,
} from './transport-img/img';
import loaderAnim from '../loader.svg';

function TransportButton({
  label, isActive, onClick, img, disabled, loading,
}) {
  return (
    <button
      type="button"
      className={`transport-btn ${isActive ? 'active' : ''} ${(disabled || loading) ? 'disabled' : ''}`}
      onClick={onClick}
      disabled={disabled || loading}
    >
      <img
        className="transportation"
        src={img}
        alt={label}
      />
      {loading ? <img src={loaderAnim} alt="Loading anim" /> : label}
    </button>
  );
}

function Transport({
  setTicketsData, selectedTransport,
  setSelectedTransport, requestBody,
  ticketsData, loading,
}) {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [isOpen, setIsOpen] = useState(false);
  const [buttonClicked, setButtonClicked] = useState(false);
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'transport' });

  const handleButtonClick = (button) => {
    setSelectedTransport((prevSelectedTransport) => {
      navigate(location.search.replace(/bus|all|train/, button));
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
    if ((ticketsData.length || Object.keys(requestBody).length !== 0) > 0 && buttonClicked) {
      console.log(ticketsData);
      setButtonClicked(false);
      const body = {
        ...requestBody,
        ...selectedTransport,
      };
      makeQuerry('selectedTransport', JSON.stringify(body), { 'Content-Language': i18n.language.toLowerCase() })
        .then((response) => {
          setTicketsData(response.body);
        })
        .catch((error) => {
          console.error('Error fetching data:', error);
        });
    }
  }, [selectedTransport, buttonClicked]);

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={searchParams.get('type') === 'all'}
        onClick={() => { handleButtonClick('all'); setButtonClicked(true); }}
        img={everythingIcon}
        loading={loading}
      />
      <TransportButton
        label={t('bus')}
        isActive={searchParams.get('type') === 'bus'}
        onClick={() => { handleButtonClick('bus'); setButtonClicked(true); }}
        img={busIcon}
        loading={loading}
      />
      <TransportButton
        label={t('train')}
        isActive={searchParams.get('type') === 'train'}
        onClick={() => { handleButtonClick('train'); setButtonClicked(true); }}
        img={trainIcon}
        loading={loading}
      />
      <TransportButton
        label={t('plane')}
        isActive={selectedTransport.airplane}
        onClick={() => setIsOpen(true)}
        img={planeIcon}
        loading={loading}
        disabled
      />
      <TransportButton
        label={t('ferry')}
        isActive={selectedTransport.ferry}
        onClick={() => setIsOpen(true)}
        img={boatIcon}
        loading={loading}
        disabled
      />
      {isOpen && <InProgress title={t('message-title')} text={t('message-text')} setIsOpen={setIsOpen} />}
    </div>
  );
}

export default Transport;
