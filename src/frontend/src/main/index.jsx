/* eslint-disable max-len */
import React, { useState } from 'react';
import SearchField from './SearchField';
import Loader from './Loader';
import Body from './Body';
import Error from './Error';
import Transport from './Transport';
import Tourist from './Tourist';

export default function Index() {
  const [ticketsData, onTicketsData] = useState([]);
  const [loading, onLoading] = useState(false);

  function showTickets() {
    if (ticketsData !== null) {
      return (
        <>
          <Body ticketsData={ticketsData} onTicketsData={onTicketsData} />
          {ticketsData.length > 0 ? <Tourist ticketsData={ticketsData} onTicketsData={onTicketsData} /> : null}
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
          <SearchField onLoading={onLoading} onTicketsData={onTicketsData} />
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
