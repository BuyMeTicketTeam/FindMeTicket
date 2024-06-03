/* eslint-disable no-unused-vars */
/* eslint-disable max-len */
import React, { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { useLazySearchTicketsQuery, useLazySortByQuery } from '../../services/ticketsApi';
import { paramsToObject } from '../../helper/paramsToObject';
import eventSourceQuery from '../../helper/eventSourceQuery';

import SearchField from './SearchField';
import Transport from './Transport';
import Ad from '../../common/Ad';
import Partner from './Partner';
import Tourist from './Tourist';
import Filters from './Filters';
import Ticket from './Ticket';

import './main.scss';

export default function Index() {
  const { tickets } = useSelector((state) => state.tickets);
  const [
    searchTickets, { isError: searchTicketsError, isLoading: isSearchTicketsLoading },
  ] = useLazySearchTicketsQuery();
  const [
    sortTickets, { isError: sortTicketsError, isLoading: isSortLoading },
  ] = useLazySortByQuery();
  const loading = isSearchTicketsLoading || isSortLoading;
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (!searchParams.has('arrivalCity') && !searchParams.has('departureCity')) {
      return;
    }

    const searchParamsObj = paramsToObject(searchParams.entries());

    function handleMessage(params) {
      console.log(params);
    }

    if (tickets.length === 0) {
      console.log(searchParamsObj);
      // searchTickets(searchParamsObj);
      eventSourceQuery({ address: '/tickets/search', body: searchParamsObj, handleMessage });
    } else {
      sortTickets(searchParamsObj);
    }
  }, [searchParams]);

  return (
    <div className="main-block main">
      <div className="container">
        <Ad />
        <Transport
          loading={loading}
        />
        <SearchField
          loading={loading}
        />
        {tickets.length > 0
        && (
        <>
          <Tourist />
          <Filters
            loading={loading}
          />
          <div className="tickets">
            {tickets.map((item) => <Ticket key={item.id} data={item} />)}
          </div>
        </>
        )}
        {tickets.length === 0 && <Ad />}
        <Partner />
      </div>
    </div>
  );
}
