/* eslint-disable no-useless-concat */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useDispatch, useSelector } from 'react-redux';

import { setTransportType } from '../../../store/tickets/ticketsSlice';

import InProgress from '../../../InProgress/index';
import TransportButton from '../TransportBtn';

import './transport.scss';
import {
  busIcon, trainIcon, everythingIcon, planeIcon, boatIcon,
} from '../../../images/transport-img/img';

function Transport({
  loading,
}) {
  const [isOpen, setIsOpen] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'transport' });
  const dispatch = useDispatch();
  const { bus, train } = useSelector((state) => state.tickets);

  const handleButtonClick = (type) => dispatch(setTransportType(type));

  return (
    <div>
      <TransportButton
        label={t('everything')}
        isActive={bus && train}
        onClick={() => { handleButtonClick('all'); }}
        img={everythingIcon}
        loading={loading}
      />
      <TransportButton
        label={t('bus')}
        isActive={bus && !train}
        onClick={() => { handleButtonClick('bus'); }}
        img={busIcon}
        loading={loading}
      />
      <TransportButton
        label={t('train')}
        isActive={train && !bus}
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
