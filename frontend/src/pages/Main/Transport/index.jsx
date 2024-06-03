/* eslint-disable no-useless-concat */
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';

import { paramsToObject } from '../../../helper/paramsToObject';

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
  const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'transport' });

  const searchParamsObj = paramsToObject(searchParams.entries());

  const handleButtonClick = (...types) => {
    const defaultTypes = {
      bus: false,
      train: false,
      airplane: false,
      ferry: false,
    };
    types.forEach((type) => { defaultTypes[type] = true; });
    setSearchParams({ ...searchParamsObj, ...defaultTypes });
  };

  useEffect(() => {
    handleButtonClick('bus', 'train');
  }, []);

  const getIsActive = (transport) => {
    const bus = searchParams.get('bus') === 'true';
    const train = searchParams.get('train') === 'true';

    switch (transport) {
      case 'bus':
        return bus && !train;
      case 'train':
        return !bus && train;
      default:
        return bus && train;
    }
  };

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={getIsActive('all')}
        onClick={() => { handleButtonClick('bus', 'train'); }}
        img={everythingIcon}
        loading={loading}
      />
      <TransportButton
        label={t('bus')}
        isActive={getIsActive('bus')}
        onClick={() => { handleButtonClick('bus'); }}
        img={busIcon}
        loading={loading}
      />
      <TransportButton
        label={t('train')}
        isActive={getIsActive('train')}
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
