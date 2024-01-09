import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import FiltersBtn from './FiltersBtn';
import makeQuerry from '../../helper/querry';
import './filters.css';

export default function Filters({ requestBody, setTicketsData }) {
  const [sort, setSort] = useState('');
  const [ascending, setAscending] = useState(false);
  const [loading, setLoading] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });

  async function sendRequest(sortArg, reverse) {
    const body = {
      ...requestBody,
      sortingBy: sortArg,
      ascending: reverse,
    };
    setLoading(true);
    const response = await makeQuerry('sortedby', JSON.stringify(body));
    setLoading(false);

    const responseBody = response.status === 200 ? response.body : null;
    setTicketsData(responseBody);
  }

  function handleSort(sortType) {
    if (sortType === sort) {
      setAscending((prevAsending) => !prevAsending);
      sendRequest(sortType, true);
      return;
    }
    setSort(sortType);
    setAscending(false);
    sendRequest(sortType, false);
  }

  return (
    <div className="main-filters">
      <FiltersBtn
        onClick={(sortType) => handleSort(sortType)}
        sortType="price"
        reverse="price-up"
        loading={loading}
        isDown={sort === 'price'}
        isUp={sort === 'price' && ascending}
      >
        {t('price')}
      </FiltersBtn>
      <FiltersBtn
        onClick={(sortType) => handleSort(sortType)}
        sortType="travelTime"
        reverse="time-travel-up"
        loading={loading}
        isDown={sort === 'travelTime'}
        isUp={sort === 'travelTime' && ascending}
      >
        {t('travel-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={(sortType) => handleSort(sortType)}
        sortType="departureTime"
        reverse="time-departure-up"
        loading={loading}
        isDown={sort === 'departureTime'}
        isUp={sort === 'departureTime' && ascending}
      >
        {t('departure-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={(sortType) => handleSort(sortType)}
        sortType="arrivalTime"
        reverse="time-arrival-up"
        loading={loading}
        isDown={sort === 'arrivalTime'}
        isUp={sort === 'arrivalTime' && ascending}
      >
        {t('arrival-time')}
      </FiltersBtn>
    </div>
  );
}
