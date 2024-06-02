/* eslint-disable no-useless-concat */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';

import InProgress from '../../../InProgress/index';
import TransportButton from '../TransportBtn';

import './transport.scss';
import {
  busIcon, trainIcon, everythingIcon, planeIcon, boatIcon,
} from '../../../images/transport-img/img';

function Transport({
  loading,
}) {
  const [searchParams, setSearchParams] = useSearchParams();
  const activeButton = searchParams.get('type') ?? 'all';
  const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'transport' });

  const handleButtonClick = (type) => {
    setSearchParams({ ...searchParams, type });
  };

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={activeButton === 'all'}
        onClick={() => { handleButtonClick('all'); }}
        img={everythingIcon}
        loading={loading}
      />
      <TransportButton
        label={t('bus')}
        isActive={activeButton === 'bus'}
        onClick={() => { handleButtonClick('bus'); }}
        img={busIcon}
        loading={loading}
      />
      <TransportButton
        label={t('train')}
        isActive={activeButton === 'train'}
        onClick={() => { handleButtonClick('train'); }}
        img={trainIcon}
        loading={loading}
      />
      <TransportButton
        label={t('plane')}
        onClick={() => setIsOpen(true)}
        img={planeIcon}
        loading={loading}
        disabled
      />
      <TransportButton
        label={t('ferry')}
        onClick={() => setIsOpen(true)}
        img={boatIcon}
        loading={loading}
        disabled
      />
      {isOpen && <InProgress title={t('message-title')} text={t('message-text')} open={isOpen} closeModal={() => setIsOpen(false)} />}
    </div>
  );
}

export default Transport;
