/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import Filters from './Filters';
import Ticket from './Ticket';

export default function Body({ ticketsData, onTicketsData }) {
  const [sort, onSort] = useState('price-low');

  function sortFunc(arr, sortType) {
    if (sortType === 'price-low') {
      return arr.sort((a, b) => a.priceOrdinary - b.priceOrdinary);
    }
    return arr.sort((a, b) => b.priceOrdinary - a.priceOrdinary);
  }

  useEffect(() => {
    onTicketsData((prevTicketsData) => sortFunc([...prevTicketsData], sort));
  }, [sort]);

  return (
    <>
      {ticketsData.length !== 0 ? <Filters onSort={onSort} prevSort={sort} /> : null}
      <div className="ticktets">
        {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
      </div>
    </>
  );
}
