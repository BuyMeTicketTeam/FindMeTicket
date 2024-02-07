/* eslint-disable max-len */
import React, { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import eventSourceQuery2 from '../helper/eventSourceQuery2';
import Price from './Price/index';
import Information from './Information/index ';
import Loader from '../Loader/index';
import Error from '../Error';
import './style.scss';

function TicketPage() {
  const { ticketId } = useParams();
  const [ticketData, setTicketData] = useState(null);
  const [ticketUrl, setTicketUrl] = useState([]);
  const [ticketError, setTicketError] = useState(false);
  const [connection, setConnection] = useState(true);
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'ticket-page' });

  async function serverRequest() {
    function onOpen(res) {
      switch (res.status) {
        case 200:
          console.log('open successfully');
          break;
        default:
          setTicketError(true);
          break;
      }
    }

    function onMessage(event) {
      const parsedData = JSON.parse(event.data);
      if (event.event === 'ticket info') {
        setTicketData(parsedData);
        return;
      }
      setTicketUrl((prevTicketUrl) => [...prevTicketUrl, { resource: event.event, url: parsedData.url, price: parsedData.price }]);
    }

    function onError() {
      if (!ticketData) {
        console.log('ticket data in error message:', ticketData);
      }
    }

    function onClose() {
      setConnection(false);
    }
    eventSourceQuery2({
      address: `get/ticket/${ticketId}`,
      onMessage,
      onError,
      onOpen,
      onClose,
      headers: { 'Content-Language': i18n.language.toLowerCase() },
    });
  }

  const handleServerRequest = useCallback(() => serverRequest(), []);

  useEffect(() => {
    handleServerRequest();
  }, []);

  const ticketDataView = ticketData && (
    <>
      <div className="ticketPage-header">{`${ticketData.departureDate} - ${ticketData.arrivalDate}`}</div>
      <Information ticketData={ticketData} />
      <div className="ticketPage-text">{t('price')}</div>
      <Price ticketUrls={ticketUrl} connection={connection} />
    </>
  );
  const ticketErrorView = ticketError && <Error />;
  const ticketLoadingView = (!ticketError && !ticketData) && <Loader />;

  return (
    <div className="ticket-page-container">
      {ticketDataView}
      {ticketErrorView}
      {ticketLoadingView}
    </div>
  );
}

export default TicketPage;
