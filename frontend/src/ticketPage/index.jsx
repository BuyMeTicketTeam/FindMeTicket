/* eslint-disable max-len */
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
// import eventSourceQuery2 from '../helper/eventSourceQuery';
import { useGetTicketQuery } from '../services/ticketsApi';
import Price from './Price/index';
import Information from './Information/index ';
import Loader from '../Loader/index';
import PriceTrain from './PriceTrain/index';
import Error from '../Error';
import Maps from './Maps';
import './style.scss';

function TicketPage() {
  const { ticketId } = useParams();
  const [ticketUrl, setTicketUrl] = useState(new Set());
  const [ticketData, setTicketData] = useState();

  const onChunk = (value) => {
    if (value.event === 'ticket info') {
      setTicketData(value.data);
      return;
    }
    setTicketUrl((prevTicketUrl) => [...prevTicketUrl, value]);
  };

  const { isError, isLoading } = useGetTicketQuery({ data: ticketId, onChunk });
  const { t } = useTranslation('translation', { keyPrefix: 'ticket-page' });

  const PriceView = ticketData?.type === 'TRAIN' ? <PriceTrain /> : <Price />;
  const mapView = ticketData?.placeAt ? <Maps address={`${ticketData.placeAt},${ticketData.arrivalCity}`} /> : <Error />;

  const ticketDataView = ticketData && (
    <>
      <div className="ticketPage-header">{`${ticketData.departureDate} - ${ticketData.arrivalDate}`}</div>
      <Information ticketData={ticketData} />
      <div className="ticketPage-text">{t('price')}</div>
      {PriceView}
      {mapView}
    </>
  );
  const ticketErrorView = isError && <Error />;
  const ticketLoadingView = (!isError && !ticketData) && <Loader />;

  return (
    <div className="ticket-page-container">
      {ticketDataView}
      {ticketErrorView}
      {ticketLoadingView}
    </div>
  );
}

export default TicketPage;
