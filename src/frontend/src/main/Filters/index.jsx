import React from 'react';
import FiltersBtn from './FiltersBtn';
import './filters.css';

export default function Filters({ onSort, prevSort }) {
  return (
    <div className="main-filters">
      <FiltersBtn
        onClick={onSort}
        sortType="price-low"
        reverse="price-up"
        isDown={prevSort === 'price-low'}
        isUp={prevSort === 'price-up'}
      >
        Ціна
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-travel-low"
        reverse="time-travel-up"
        isDown={prevSort === 'time-travel-low'}
        isUp={prevSort === 'time-travel-up'}
      >
        Час поїздки
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-departure-low"
        reverse="time-departure-up"
        isDown={prevSort === 'time-departure-low'}
        isUp={prevSort === 'time-departure-up'}
      >
        Час відправлення
      </FiltersBtn>
      <FiltersBtn
        onClick={onSort}
        sortType="time-arrival-low"
        reverse="time-arrival-up"
        isDown={prevSort === 'time-arrival-low'}
        isUp={prevSort === 'time-arrival-up'}
      >
        Час прибуття
      </FiltersBtn>
    </div>
  );
}
