import React from 'react';
import { useTranslation } from 'react-i18next';
import FiltersBtn from './FiltersBtn';
import './filters.css';

export default function Filters({ onSort, prevSort }) {
  const { t } = useTranslation('translation', { keyPrefix: 'filters' });

  return (
    <div className="main-filters">
      <FiltersBtn
        onClick={onSort}
        sortType="price-low"
        reverse="price-up"
        isDown={prevSort === 'price-low'}
        isUp={prevSort === 'price-up'}
      >
        {t('price')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-travel-low"
        reverse="time-travel-up"
        isDown={prevSort === 'time-travel-low'}
        isUp={prevSort === 'time-travel-up'}
      >
        {t('travel-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-departure-low"
        reverse="time-departure-up"
        isDown={prevSort === 'time-departure-low'}
        isUp={prevSort === 'time-departure-up'}
      >
        {t('departure-time')}
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-arrival-low"
        reverse="time-arrival-up"
        isDown={prevSort === 'time-arrival-low'}
        isUp={prevSort === 'time-arrival-up'}
      >
        {t('arrival-time')}
      </FiltersBtn>
    </div>
  );
}
