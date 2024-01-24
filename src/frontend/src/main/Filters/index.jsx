/* eslint-disable react/no-array-index-key */
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import FiltersBtn from './FiltersBtn';
import makeQuerry from '../../helper/querry';
import './filters.scss';

export default function Filters({ requestBody, setTicketsData }) {
  const [sort, setSort] = useState('');
  const [ascending, setAscending] = useState(false);
  const [loading, setLoading] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });

  const filtersBtn = ['price', 'travelTime', 'departureTime', 'arrivalTime'];

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
          loading={loading}
          isDown={sort === filter}
          isUp={sort === filter && ascending}
        >
          {t(filter)}
        </FiltersBtn>
      ))}
    </div>
  );
}
