/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
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
  const [error, setError] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'find-more-btn' });

  function convertTimeDate(time, date) {
    const [day, month] = date.split(',')[0].split('.');
    const [hours, minutes] = time.split(':');
    return new Date(new Date().getFullYear(), month - 1, day, hours, minutes).getTime();
  }

  function convertTravelTime(time) {
    const [hours,, minutes] = time.split(' ');
    return +(hours * 60) + +minutes;
  }

  function compareTimeDate(time1, date1, time2, date2) {
    const timestamp1 = convertTimeDate(time1, date1);
    const timestamp2 = convertTimeDate(time2, date2);
    return timestamp1 - timestamp2;
  }

  function compareTravelTime(time1, time2) {
    const convertedTime1 = convertTravelTime(time1);
    const convertedTime2 = convertTravelTime(time2);
    return convertedTime1 - convertedTime2;
  }

  function sortFunc(arr, sortType) {
    const compareFunctions = {
      'price-up': (a, b) => b.price - a.price,
      'time-departure-low': (a, b) => compareTimeDate(a.departureTime, a.departureDate, b.departureTime, b.departureDate),
      'time-departure-up': (a, b) => compareTimeDate(b.departureTime, b.departureDate, a.departureTime, a.departureDate),
      'time-arrival-low': (a, b) => compareTimeDate(a.arrivalTime, a.arrivalDate, b.arrivalTime, b.arrivalDate),
      'time-arrival-up': (a, b) => compareTimeDate(b.arrivalTime, b.arrivalDate, a.arrivalTime, a.arrivalDate),
      'time-travel-low': (a, b) => compareTravelTime(a.travelTime, b.travelTime),
      'time-travel-up': (a, b) => compareTravelTime(b.travelTime, a.travelTime),
    };

    const compareFunction = compareFunctions[sortType] || ((a, b) => a.price - b.price);

    return arr.sort(compareFunction);
  }

  async function handleClick() {
    handleSend(false);
    onLoading(true);
    const response = await makeQuerry('getnexttickets', JSON.stringify(requestBody));
    const responseBody = response.status === 200 ? response.body : [];
    if (response.status !== 200) {
      setError(true);
    }
    onLoading(false);
    onTicketsData((prevValue) => sortFunc([...prevValue, ...responseBody], sort));
  }

  function checkResponse() {
    return error ? t('error') : t('find-more');
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
      <Filters onSort={onSort} prevSort={sort} />
      <div className="tickets">
        {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
        {ticketsData.length !== 0 ? <Button className="tickets__more" name={loading ? <img className="tickets__loading-img" src="../img/loading.svg" alt="Підвантажуємо..." /> : checkResponse()} onButton={handleSend} /> : null}
      </div>
    </>
  );
}
