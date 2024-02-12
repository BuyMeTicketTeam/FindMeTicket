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
          ticketsData={[{
            id: 6,
            departureTime: '6:40',
            departureDate: '30.11, вт',
            travelTime: '9 год 5 хв',
            arrivalTime: '15:30',
            arrivalDate: '29.11, вт',
            departureCity: 'Одеса',
            arrivalCity: 'Київ',
            placeFrom: 'Автовокзал "Центральний"',
            placeAt: 'Южный автовокзал',
            price: '1000',
            carrier: 'nikkaBus',
            url: 'https://google.com',
            type: 'BUS',
          },
          {
            id: 6,
            departureTime: '6:40',
            departureDate: '30.11, вт',
            travelTime: '9 год 5 хв',
            arrivalTime: '15:30',
            arrivalDate: '29.11, вт',
            departureCity: 'Одеса',
            arrivalCity: 'Київ',
            placeFrom: 'Автовокзал "Центральний"',
            placeAt: 'Южный автовокзал',
            price: '1000',
            carrier: 'nikkaBus',
            url: 'https://google.com',
            type: 'TRAIN',
          }]}
        />
      </div>
      <Outlet />
    </div>
  );
}
