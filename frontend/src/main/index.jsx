/* eslint-disable max-len */
import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import SearchField from './SearchField';
import Transport from './Transport';
import Ad from '../Ad/index';
import './main.scss';
import TicketsBody from './Body';
import Partner from './Partner';

export default function Index({
  ticketsData, setTicketsData, selectedTransport, setSelectedTransport, urlSearch, setUrlSearch,
}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  return (
    <div className="main-block main">
      <div className="container">
        <div className="search_index">
          <Ad />
          <Transport
            loading={loading}
            selectedTransport={selectedTransport}
            setSelectedTransport={setSelectedTransport}
            ticketsData={ticketsData}
            setTicketsData={setTicketsData}
          />
          <SearchField
            loading={loading}
            setLoading={setLoading}
            setTicketsData={setTicketsData}
            urlSearch={urlSearch}
            setUrlSearch={setUrlSearch}
            setError={setError}
            ticketsData={ticketsData}
            selectedTransport={selectedTransport}
          />
        </div>
        <TicketsBody
          loading={loading}
          error={error}
          setTicketsData={setTicketsData}
          ticketsData={ticketsData}
          selectedTransport={selectedTransport}
        />
        <Partner />
      </div>
      <Outlet />
    </div>
  );
}
