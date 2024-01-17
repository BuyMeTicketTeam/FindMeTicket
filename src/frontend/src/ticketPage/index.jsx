/* eslint-disable no-restricted-syntax */
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Price from './Price/index';
import Information from './Information/index ';
import Maps from './Maps/index';
import eventSourceQuery from '../helper/eventSourceQuery';
import './style.css';

function TicketPage() {
  const { userId } = useParams();
  const [ticketData, setTicketData] = useState(null);
  const [ticketUrl, setTicketUrl] = useState([]);

  async function serverRequest() {
    const dataStream = await eventSourceQuery('', undefined, undefined, 'GET');
    for await (const chunk of dataStream) {
      console.log(chunk);
      if (Object.prototype.hasOwnProperty.call(chunk, 'ticketname')) {
        setTicketData(chunk);
      } else {
        setTicketUrl(((prevLinks) => [...prevLinks, chunk]));
      }
    }
  }

  useEffect(() => {
    serverRequest();
  }, []);

  return (
    <div className="ticket-page-container">
      {/* <div className="ticketPage-header">{ticketData.date}</div>
      <Information ticketData={ticketData} />
      <div className="ticketPage-text">Ціни</div>
      <Price ticketUrl={ticketUrl} />
      <Maps /> */}
      <h2>testing</h2>
    </div>
  );
}

export default TicketPage;
