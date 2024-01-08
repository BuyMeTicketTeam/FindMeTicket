import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import FiltersBtn from './FiltersBtn';
import './filters.css';

export default function Filters({ onSort }) {
  const [sort, setSort] = useState('');
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });

  return (
    <div className="main-filters">
      <FiltersBtn
        onClick={onSort}
        setSort={setSort}
        sortType="price"
        reverse="price-up"
        isDown={sort === 'price'}
        // isUp={prevSort === 'price-up'}
      >
        {t('price')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        setSort={setSort}
        sortType="travelTime"
        reverse="time-travel-up"
        isDown={sort === 'travelTime'}
        // isUp={prevSort === 'time-travel-up'}
      >
        {t('travel-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        setSort={setSort}
        sortType="departureTime"
        reverse="time-departure-up"
        isDown={sort === 'departureTime'}
        // isUp={prevSort === 'time-departure-up'}
      >
        {t('departure-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        setSort={setSort}
        sortType="arrivalTime"
        reverse="time-arrival-up"
        isDown={sort === 'arrivalTime'}
        // isUp={prevSort === 'time-arrival-up'}
      >
        {t('arrival-time')}
      </FiltersBtn>
    </div>
  );
}
