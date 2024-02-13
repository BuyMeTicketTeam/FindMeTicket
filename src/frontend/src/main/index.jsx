/* eslint-disable max-len */
import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import SearchField from './SearchField';
import Transport from './Transport';
import Ad from '../Ad/index';
import './main.scss';
import TicketsBody from './Body';

export default function Index({ ticketsData, setTicketsData }) {
  const [loading, setLoading] = useState(false);
  const [requestBody, setRequestBody] = useState({});
  const [error, setError] = useState(null);

  return (
    <div className="main-block main">
      <div className="container">
        <div className="search_index">
          <Ad />
          <Transport />
          <SearchField
            loading={loading}
            setLoading={setLoading}
            setTicketsData={setTicketsData}
            setRequestBody={setRequestBody}
            setError={setError}
            ticketsData={ticketsData}
          />
        </div>
        <TicketsBody
          loading={loading}
          error={error}
          requestBody={requestBody}
          setTicketsData={setTicketsData}
          ticketsData={ticketsData}
        />
      </div>
      <Outlet />
    </div>
  );
}
