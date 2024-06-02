import React from 'react';
import { useSearchParams } from 'react-router-dom';
import FiltersBtn from './FiltersBtn';
import './filters.scss';

export default function Filters({ loading }) {
  const [searchParams, setSearchParams] = useSearchParams();
  const sortBy = searchParams.get('sortBy');
  const ascending = searchParams.get('ascending');

  const filtersBtn = ['Price', 'TravelTime', 'DepartureTime', 'ArrivalTime'];

  function handleSort(sortType) {
    if (sortBy === sortType) {
      setSearchParams({ ...searchParams, ascending: true });
      return;
    }
    setSearchParams({ ...searchParams, sortBy: sortType, ascending: false });
  }

  return (
    <div data-testid="filters" className="main-filters">
      {filtersBtn.map((filter) => (
        <FiltersBtn
          key={filter}
          onClick={() => handleSort(filter)}
          isActive={sortBy === filter}
          isUp={sortBy === filter && ascending}
          loading={loading}
        >
          {filter}
        </FiltersBtn>
      ))}
    </div>
  );
}
