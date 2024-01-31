/* eslint-disable max-len */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-restricted-syntax */
import React, { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import eventSourceQuery2 from '../helper/eventSourceQuery2';
import Price from './Price/index';
import Information from './Information/index ';
// import Maps from './Maps/index';
import './style.css';

function TicketPage() {
  const { ticketId } = useParams();
  const [ticketData, setTicketData] = useState(null);
  const [ticketUrl, setTicketUrl] = useState([]);
  const [ticketError, setTicketError] = useState(false);

  console.log('ticketId', ticketId);

  async function serverRequest() {
    function onMessage(event) {
      if (event.event === 'ticket info') {
        const parsedData = JSON.parse(event.data);
        console.log(parsedData);
        setTicketData(parsedData);
      }
      setTicketUrl(...ticketUrl, { provider: event.event, url: event.data });
    }

    function onError() {
      setTicketError(true);
    }
    eventSourceQuery2(`get/ticket/${ticketId}`, onMessage, onError);
  }

  const handleServerRequest = useCallback(() => serverRequest(), []);

  useEffect(() => {
    handleServerRequest();
  }, []);

  return (
    <div className="ticket-page-container">
      {ticketData
        ? (
          <>
            <div className="ticketPage-header">{`${ticketData.departureDate} - ${ticketData.arrivalDate}`}</div>
            <Information ticketData={ticketData} />
            <div className="ticketPage-text">Ціни</div>
            <Price ticketUrl={ticketUrl} price={ticketData.price} />
            {/* <Maps /> */}
            {/* <Maps /> */}
          </>
        )
        : <h2>{ticketError ? 'Error' : 'Loading...'}</h2>}
    </div>
  );
}

export default TicketPage;
