/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import Filters from '../Filters';
import Ticket from '../Ticket';

export default function Body({ ticketsData, onTicketsData }) {
  const [sort, onSort] = useState('price-low');

  function convertTimeDate(time, date) {
    const [day, month] = date.split(',')[0].split('.');
    const [hours, minutes] = time.split(':');
    return new Date(new Date().getFullYear(), month - 1, day, hours, minutes).getTime();
  }

  function sortFunc(arr, sortType) {
    if (sortType === 'price-low') {
      return arr.sort((a, b) => a.priceOrdinary - b.priceOrdinary);
    }
    if (sortType === 'price-up') {
      return arr.sort((a, b) => b.priceOrdinary - a.priceOrdinary);
    }
    if (sortType === 'time-departure-low') {
      return arr.sort((a, b) => convertTimeDate(a.departureTime, a.departureDate) - convertTimeDate(b.departureTime, b.departureDate));
    }
    if (sortType === 'time-departure-up') {
      return arr.sort((a, b) => convertTimeDate(b.departureTime, b.departureDate) - convertTimeDate(a.departureTime, a.departureDate));
    }
    if (sortType === 'time-arrival-low') {
      return arr.sort((a, b) => convertTimeDate(a.arrivalTime, a.arrivalDate) - convertTimeDate(b.arrivalTime, b.arrivalDate));
    }
    if (sortType === 'time-arrival-up') {
      return arr.sort((a, b) => convertTimeDate(b.arrivalTime, b.arrivalDate) - convertTimeDate(a.arrivalTime, a.arrivalDate));
    }
    return arr.sort((a, b) => convertTimeDate(a.departureTime, a.departureDate) - convertTimeDate(b.departureTime, b.departureTime));
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
