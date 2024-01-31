/* eslint-disable max-len */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-restricted-syntax */
import React, { useCallback, useEffect, useState } from 'react';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { useParams } from 'react-router-dom';
import Price from './Price/index';
import Information from './Information/index ';
// import Maps from './Maps/index';
// import eventSourceQuery from '../helper/eventSourceQuery';
import './style.css';

function TicketPage() {
  const { ticketId } = useParams();
  const [ticketData, setTicketData] = useState(null);
  const [ticketUrl, setTicketUrl] = useState([]);

  // async function serverRequest() {
  //   const dataStream = await eventSourceQuery(`/get/ticket/${ticketId}`, undefined, undefined, 'GET');
  //   for await (const chunk of dataStream) {
  //     console.log(chunk);
  //     // if (Object.prototype.hasOwnProperty.call(chunk, 'ticketname')) {
  //     //   setTicketData(chunk);
  //     // } else {
  //     //   setTicketUrl(((prevLinks) => [...prevLinks, chunk]));
  //     // }
  //   }
  // }

  console.log('ticketId', ticketId);

  async function serverRequest() {
    try {
      await fetchEventSource(`http://localhost:8080/get/ticket/${ticketId}`, {
        method: 'GET',
        headers: {
          Accept: 'text/event-stream',
        },
        onopen(res) {
          if (res.ok && res.status === 200) {
            console.log('Connection made ', res);
          } else if (
            res.status >= 400
          && res.status < 500
          && res.status !== 429
          ) {
            console.log('Client side error ', res);
          }
        },
        onmessage(event) {
          console.log('event', event);
          if (event.event === 'ticket info') {
            const parsedData = JSON.parse(event.data);
            console.log(parsedData);
            setTicketData(parsedData);
          }
          setTicketUrl(...ticketUrl, { provider: event.event, url: event.data });
        },
        onclose() {
          console.log('Connection closed by the server');
          throw new Error('Connection closed');
        },
        onerror(err) {
          throw err;
        },
      });
    } catch (error) {
      console.log('There was an error or connection was closed', error);
    }
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
        : <h2>Loading...</h2>}
    </div>
  );
}

export default TicketPage;
