/* eslint-disable max-len */
/* eslint-disable no-unused-vars */
/* eslint-disable react/no-array-index-key */
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import FiltersBtn from './FiltersBtn';
import makeQuerry from '../../helper/querry';
import './filters.scss';

export default function Filters({
  setTicketsData, loading,
}) {
  const [sort, setSort] = useState('');
  const [ascending, setAscending] = useState(false);
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'filters' });
  const [searchParams] = useSearchParams();

  const filtersBtn = ['Price', 'TravelTime', 'DepartureTime', 'ArrivalTime'];

  async function sendRequest(sortArg, reverse) {
    const date = searchParams.get('departureDate');
    const body = {
      departureCity: searchParams.get('from'),
      arrivalCity: searchParams.get('to'),
      departureDate: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`,
      bus: searchParams.get('type') === 'bus' || searchParams.get('type') === 'all',
      train: searchParams.get('type') === 'train' || searchParams.get('type') === 'all',
      sortingBy: sortArg,
      ascending: reverse,
    };
    const response = await makeQuerry('sortedBy', JSON.stringify(body), { 'Content-Language': i18n.language.toLowerCase() });

    const responseBody = response.status === 200 ? response.body : null;
    setTicketsData(responseBody);
  }

  function handleSort(sortType) {
    if (sortType === sort) {
      setAscending((prevAscending) => !prevAscending);
      return;
    }
    setSort(sortType);
    setAscending(false);
  }

  useEffect(() => {
    if (sort !== '') {
      sendRequest(sort, ascending);
    }
  }, [sort, ascending]);

  return (
    <div data-testid="filters" className="main-filters">
      {filtersBtn.map((filter, index) => (
        <FiltersBtn
          key={index}
          dataTestId={filter}
          onClick={() => handleSort(filter)}
          isDown={sort === filter}
          isUp={sort === filter && ascending}
          loading={loading}
        >
          {t(filter)}
        </FiltersBtn>
      ))}
    </div>
  );
}
