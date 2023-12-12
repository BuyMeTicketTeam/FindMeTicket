import React, { useState } from 'react';
import SearchField from './SearchField';
import Loader from './Loader';
import Body from './Body';
import Error from './Error';

export default function Index() {
  const [ticketsData, onTicketsData] = useState([]);
  const [loading, onLoading] = useState(false);

  function showTickets() {
    if (ticketsData !== null) {
      return <Body ticketsData={ticketsData} onTicketsData={onTicketsData} />;
    }
    return <Error />;
  }

  return (
    <div className="main-block">
      <div className="container">
        <SearchField onLoading={onLoading} onTicketsData={onTicketsData} />
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
