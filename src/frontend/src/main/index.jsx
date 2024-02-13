/* eslint-disable max-len */
import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import SearchField from './SearchField';
import Loader from './Loader';
import Error from './Error';
import Transport from './Transport';
import Tourist from './Tourist';
import Ticket from './Ticket';
import Filters from './Filters';
import Ad from '../Ad/index';
import './main.scss';

export default function Index() {
  const [ticketsData, setTicketsData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [requestBody, setRequestBody] = useState({});

  function ticketsBody() {
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
            setTicketsData={setTicketsData}
            city={requestBody.arrivalCity}
          />
          <Filters setTicketsData={setTicketsData} requestBody={requestBody} />
          <div className="tickets">
            {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
          </div>
        </>
      );
    }
    return <Ad isBig />;
  }

  return (
    <div className="main-block main">
      <div className="container">
        <div className="search_index">
          <Ad />
          <Transport />
          <SearchField
            onLoading={setLoading}
            setTicketsData={setTicketsData}
            setRequestBody={setRequestBody}
            loading={loading}
          />
        </div>
        {ticketsBody()}
      </div>
      <Outlet />
    </div>
  );
}
