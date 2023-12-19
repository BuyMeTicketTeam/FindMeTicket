/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import Filters from '../Filters';
import Ticket from '../Ticket';
import Button from '../../utils/Button';
import makeQuerry from '../../helper/querry';

export default function Body({
  ticketsData, onTicketsData, setRequestBody, requestBody,
}) {
  const [sort, onSort] = useState('price-low');
  const [send, handleSend] = useState(false);
  const [loading, onLoading] = useState(false);

  function convertTimeDate(time, date) {
    const [day, month] = date.split(',')[0].split('.');
    const [hours, minutes] = time.split(':');
    return new Date(new Date().getFullYear(), month - 1, day, hours, minutes).getTime();
  }

  function convertTravelTime(time) {
    const [hours,, minutes] = time.split(' ');
    return +(hours * 60) + +minutes;
  }

  function sortFunc(arr, sortType) {
    if (sortType === 'price-up') {
      return arr.sort((a, b) => b.price - a.price);
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
    if (sortType === 'time-travel-low') {
      return arr.sort((a, b) => convertTravelTime(a.travelTime) - convertTravelTime(b.travelTime));
    }
    if (sortType === 'time-travel-up') {
      return arr.sort((a, b) => convertTravelTime(b.travelTime) - convertTravelTime(a.travelTime));
    }
    return arr.sort((a, b) => a.price - b.price);
  }

  async function handleClick() {
    handleSend(false);
    onLoading(true);
    const response = await makeQuerry('getnexttickets', JSON.stringify(requestBody));
    const responseBody = response.status === 200 ? response.body : null;
    onLoading(false);
    onTicketsData((prevValue) => [...prevValue, ...responseBody]);
  }

  useEffect(() => {
    onTicketsData((prevTicketsData) => sortFunc([...prevTicketsData], sort));
  }, [sort]);

  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);

  useEffect(() => {
    setRequestBody((prevValue) => ({ ...prevValue, indexFrom: ticketsData.length }));
  }, [ticketsData]);

  return (
    <>
      {ticketsData.length !== 0 ? <Filters onSort={onSort} prevSort={sort} /> : null}
      <div className="tickets">
        {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
        {ticketsData.length !== 0 ? <Button className="tickets__more" name={loading ? <img className="tickets__loading-img" src="../img/loading.svg" alt="Підвантажуємо..." /> : 'Знайти більше'} onButton={handleSend} /> : null}
      </div>
    </>
  );
}
