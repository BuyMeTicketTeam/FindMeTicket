/* eslint-disable import/no-extraneous-dependencies */
import React from 'react';
import SearchField from './SearchField';
import Filters from './Filters';
import Ticket from './Ticket';

export default function index() {
  return (
    <div className="main-block">
      <div className="container">
        <SearchField />
        <Filters />
        <div className="ticktets">
          <Ticket />
          <Ticket />
          <Ticket />
        </div>
      </div>
    </div>
  );
}
