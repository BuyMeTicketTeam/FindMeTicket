/* eslint-disable max-len */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-restricted-syntax */
import React, { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import eventSourceQuery2 from '../helper/eventSourceQuery2';
import Price from './Price/index';
import Information from './Information/index ';
import Loader from '../Loader/index';
import Error from '../Error';
// import Maps from './Maps/index';
import './style.scss';

function TicketPage() {
  const { ticketId } = useParams();
  const [ticketData, setTicketData] = useState(null);
  const [ticketUrl, setTicketUrl] = useState([]);
  const [ticketError, setTicketError] = useState(false);
  const [connection, setConnection] = useState(true);

  async function serverRequest() {
    function onMessage(event) {
      if (event.event === 'ticket info') {
        const parsedData = JSON.parse(event.data);
        setTicketData(parsedData);
      }
      setTicketUrl(...ticketUrl, { provider: event.event, url: event.data });
    }

    function onError() {
      setTicketError(true);
    }

    function onClose() {
      setConnection(false);
    }
    eventSourceQuery2(`get/ticket/${ticketId}`, onMessage, onError, onClose);
  }

  const handleServerRequest = useCallback(() => serverRequest(), []);

  useEffect(() => {
    handleServerRequest();
  }, []);

  const ticketDataView = ticketData && (
    <>
      <div className="ticketPage-header">{`${ticketData.departureDate} - ${ticketData.arrivalDate}`}</div>
      <Information ticketData={ticketData} />
      <div className="ticketPage-text">Ціни</div>
      <Price ticketUrls={ticketUrl} price={ticketData.price} connection={connection} />
      {/* <Maps /> */}
      {/* <Maps /> */}
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
