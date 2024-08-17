import React from "react";
import {useDispatch, useSelector} from "react-redux";

import {setSorting} from "../../../redux/store/tickets/ticketsSlice";

import FiltersBtn from "./FiltersBtn";

import "./Filters.scss";

export default function Filters({ loading }) {
  const { sortBy, ascending } = useSelector((state) => state.tickets);
  const dispatch = useDispatch();

  const filtersBtn = ["Price", "TravelTime", "DepartureTime", "ArrivalTime"];

  function handleSort(sortType) {
    if (sortBy === sortType) {
      dispatch(setSorting({ ascending: !ascending, sortBy }));
      return;
    }
    dispatch(setSorting({ ascending: false, sortBy: sortType }));
  }

  // console.log(sortBy === filtersBtn[0]);

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
