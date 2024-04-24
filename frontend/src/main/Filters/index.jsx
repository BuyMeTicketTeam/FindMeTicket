/* eslint-disable max-len */
/* eslint-disable no-unused-vars */
/* eslint-disable react/no-array-index-key */
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';
import FiltersBtn from './FiltersBtn';
import './filters.scss';

export default function Filters({ loading }) {
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });
  const [searchParams] = useSearchParams();
  const [sort, setSort] = useState(searchParams.get('sort'));
  const [ascending, setAscending] = useState(false);
  const navigate = useNavigate();

  const filtersBtn = ['Price', 'TravelTime', 'DepartureTime', 'ArrivalTime'];

  function handleSort(sortType) {
    const date = searchParams.get('departureDate') ? new Date(+searchParams.get('departureDate')) : new Date();
    if (sortType === sort) {
      const ascendingBoolean = searchParams.get('ascending') === 'true';
      navigate(`?type=${searchParams.get('type')}&from=${searchParams.get('from')}&to=${searchParams.get('to')}&departureDate=${+date}&sort=${sortType}&ascending=${!ascendingBoolean}&endpoint=3`);
      return;
    }
    navigate(`?type=${searchParams.get('type')}&from=${searchParams.get('from')}&to=${searchParams.get('to')}&departureDate=${+date}&sort=${sortType}&ascending=false&endpoint=3`);
  }

  useEffect(() => {
    setSort(searchParams.get('sort'));
    setAscending(searchParams.get('ascending') === 'true');
  }, [searchParams]);

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
