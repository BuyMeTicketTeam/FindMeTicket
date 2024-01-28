/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-restricted-syntax */
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
// import Price from './Price/index';
// import Information from './Information/index ';
// import Maps from './Maps/index';
import eventSourceQuery from '../helper/eventSourceQuery';
import './style.css';

function TicketPage() {
  const { ticketId } = useParams();
  // const [ticketData, setTicketData] = useState(null);
  // const [ticketUrl, setTicketUrl] = useState([]);

  async function serverRequest() {
    const dataStream = await eventSourceQuery(`/get/ticket/${ticketId}`, undefined, undefined, 'GET');
    for await (const chunk of dataStream) {
      console.log(chunk);
      // if (Object.prototype.hasOwnProperty.call(chunk, 'ticketname')) {
      //   setTicketData(chunk);
      // } else {
      //   setTicketUrl(((prevLinks) => [...prevLinks, chunk]));
      // }
    }
  }

  console.log('ticketId', ticketId);

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
