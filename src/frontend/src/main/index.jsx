import React, { useState } from 'react';
import SearchField from './SearchField';
import Loader from './Loader';
import Body from './Body';

export default function Index() {
  const [ticketsData, onTicketsData] = useState([]);
  const [loading, onLoading] = useState(false);

  return (
    <div className="main-block">
      <div className="container">
        <SearchField onLoading={onLoading} onTicketsData={onTicketsData} />
        {loading
          ? (
            <Loader />
          )
          : (
            <Body ticketsData={ticketsData} onTicketsData={onTicketsData} />
          )}
      </div>
    </div>
  );
}
