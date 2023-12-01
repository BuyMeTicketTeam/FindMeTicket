/* eslint-disable max-len */
/* eslint-disable no-shadow */
/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState } from 'react';
import SearchField from './SearchField';
import Filters from './Filters';
import Ticket from './Ticket';

export default function Index() {
  const [ticketsData, onTicketsdata] = useState(
    [
      {
        dateFrom: '8:40 | 29.11, вт',
        travelTime: '8 год 5 хв',
        dateArrival: '16:30 | 29.11, вт',
        placeFrom: 'Кам’янець-Подільський',
        placeTo: 'Київ',
        placeFromDetails: 'Автовокзал "Центральний"',
        placeToDetails: 'Южный автовокзал',
        priceOrdinary: '4000',
        priceOld: '800',
      },
      {
        dateFrom: '7:40 | 29.11, вт',
        travelTime: '8 год 45 хв',
        dateArrival: '10:30 | 29.11, вт',
        placeFrom: 'Дніпро',
        placeTo: 'Івано-Франківськ',
        placeFromDetails: 'Автовокзал "Центральний"',
        placeToDetails: 'Южный автовокзал',
        priceOrdinary: '500',
        priceOld: '',
      },
      {
        dateFrom: '8:40 | 29.11, вт',
        travelTime: '8 год 5 хв',
        dateArrival: '16:30 | 29.11, вт',
        placeFrom: 'Одеса',
        placeTo: 'Київ',
        placeFromDetails: 'Автовокзал "Центральний"',
        placeToDetails: 'Южный автовокзал',
        priceOrdinary: '1000',
        priceOld: '8000',
      },
    ],
  );
  const [sort, onSort] = useState('price-low');
  function sortFunc(arr, sort) {
    if (sort === 'price-low') {
      return arr.sort((a, b) => a.priceOrdinary - b.priceOrdinary);
    }
    return arr.sort((a, b) => b.priceOrdinary - a.priceOrdinary);
  }
  useEffect(() => {
    onTicketsdata(sortFunc(ticketsData, sort));
  }, [sort]);
  return (
    <div className="main-block">
      <div className="container">
        <SearchField />
        <Filters onSort={onSort} />
        <div className="ticktets">
          {ticketsData.map((item, elementIndex) => <Ticket data={ticketsData[elementIndex]} />)}
        </div>
      </div>
    </div>
  );
}
