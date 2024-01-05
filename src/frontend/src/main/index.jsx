/* eslint-disable max-len */
import React, { useState } from 'react';
import SearchField from './SearchField';
import Loader from './Loader';
import Body from './Body';
import Error from './Error';
import Transport from './Transport';
import Tourist from './Tourist';
import Ad from '../Ad/index';
import './main.css';

export default function Index() {
  const [ticketsData, onTicketsData] = useState([]);
  const [loading, onLoading] = useState(true);
  const [requestBody, setRequestBody] = useState({});

  function showTickets() {
    if (loading) {
      return <Loader />;
    }
    if (ticketsData == null) {
      return <Error />;
    }
    if (ticketsData.length > 0) {
      return (
        <>
          <Tourist
            ticketsData={ticketsData}
            onTicketsData={onTicketsData}
            city={requestBody.arrivalCity}
          />
          <Body
            ticketsData={ticketsData}
            onTicketsData={onTicketsData}
            onLoading={onLoading}
            setRequestBody={setRequestBody}
            requestBody={requestBody}
          />
        </>
      );
    }
    return <Ad isBig />;
  }

  return (
    <div className="main-block">
      <div className="container">
        <div className="search_index">
          <Ad />
          <Transport />
          <SearchField
            onLoading={onLoading}
            onTicketsData={onTicketsData}
            setRequestBody={setRequestBody}
          />
        </div>
        {showTickets()}
      </div>
    </div>
  );
}
