/* eslint-disable max-len */
import React, { useState } from 'react';
import SearchField from './SearchField';
import Loader from './Loader';
import Body from './Body';
import Error from './Error';
import Transport from './Transport';
import Tourist from './Tourist';
import './main.css';

export default function Index() {
  const [ticketsData, onTicketsData] = useState([]);
  const [loading, onLoading] = useState(false);
  const [requestBody, setRequestBody] = useState({});

  function showTickets() {
    if (ticketsData !== null) {
      return (
        <>
          {ticketsData.length > 0 ? <Tourist ticketsData={ticketsData} onTicketsData={onTicketsData} city={requestBody.arrivalCity} /> : null}
          <Body ticketsData={ticketsData} onTicketsData={onTicketsData} onLoading={onLoading} setRequestBody={setRequestBody} requestBody={requestBody} />
        </>
      );
    }
    return <Error />;
  }

  return (
    <div className="main-block">
      <div className="container">
        <div className="search_index">
          <Transport />
          <SearchField onLoading={onLoading} onTicketsData={onTicketsData} setRequestBody={setRequestBody} />
        </div>
        {loading
          ? (
            <Loader />
          )
          : (
            showTickets()
          )}
      </div>
    </div>
  );
}
