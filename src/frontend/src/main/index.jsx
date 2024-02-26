/* eslint-disable max-len */
import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import SearchField from './SearchField';
import Transport from './Transport';
import Ad from '../Ad/index';
import './main.scss';
import TicketsBody from './Body';

export default function Index({
  ticketsData, setTicketsData, selectedTransport, setSelectedTransport, requestBody, setRequestBody,
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
            setLoading={setLoading}
            selectedTransport={selectedTransport}
            setSelectedTransport={setSelectedTransport}
            ticketsData={ticketsData}
            setTicketsData={setTicketsData}
            requestBody={requestBody}
          />
          <SearchField
            loading={loading}
            setLoading={setLoading}
            setTicketsData={setTicketsData}
            setRequestBody={setRequestBody}
            setError={setError}
            ticketsData={ticketsData}
            selectedTransport={selectedTransport}
          />
        </div>
        <TicketsBody
          loading={loading}
          error={error}
          requestBody={requestBody}
          setTicketsData={setTicketsData}
          ticketsData={ticketsData}
          selectedTransport={selectedTransport}
        />
      </div>
      <Outlet />
    </div>
  );
}
