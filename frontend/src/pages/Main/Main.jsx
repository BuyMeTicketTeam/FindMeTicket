/* eslint-disable no-unused-vars */
/* eslint-disable max-len */
import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { useSelector } from "react-redux";

import {
  useLazySearchTicketsQuery,
  useLazySortByQuery,
} from "../../services/ticketsApi";
// import { paramsToObject } from '../../helper/paramsToObject';
// import { setSearchPayload } from '../../store/tickets/ticketsSlice';

import SearchField from "./SearchField/SearchField";
import Transport from "./Transport/Transport";
import Ad from "../../common/Ad/Ad";
import Partner from "./Partner/Partner";
import Tourist from "./Tourist/Tourist";
import Filters from "./Filters/Filters";
import Ticket from "./Ticket/Ticket";
import Loader from "../../common/Loader/Loader";
import Error from "../../common/Error/Error";

import "./main.scss";

const dateFormat = new Intl.DateTimeFormat("ru", {
  year: "numeric",
  month: "2-digit",
  day: "2-digit",
});

export default function Index() {
  const [tickets, setTickets] = useState([]);
  const [
    searchTickets,
    { isError: searchTicketsError, isLoading: isSearchTicketsLoading },
  ] = useLazySearchTicketsQuery();
  const [sortTickets, { isError: sortTicketsError, isLoading: isSortLoading }] =
    useLazySortByQuery();
  const loading = isSearchTicketsLoading || isSortLoading;
  const error = searchTicketsError || sortTicketsError;
  const [_, setSearchParams] = useSearchParams();
  const {
    departureCity,
    arrivalCity,
    departureDate,
    bus,
    train,
    airplane,
    ferry,
    sortBy,
    ascending,
  } = useSelector((state) => state.tickets);
  // const dispatch = useDispatch();

  // useEffect(() => {
  //   if (searchParams.size < 7) {
  //     return;
  //   }
  //   const searchParamsObj = paramsToObject(searchParams.entries());
  //   dispatch(setSearchPayload(searchParamsObj));
  // }, [searchParams]);

  useEffect(() => {
    if (departureCity === "" && arrivalCity === "") {
      return;
    }

    const payload = {
      departureCity,
      arrivalCity,
      departureDate: dateFormat.format(departureDate),
      bus,
      train,
      airplane,
      ferry,
      sortBy,
      ascending,
    };

    setSearchParams(payload);

    if (tickets.length === 0) {
      searchTickets({
        data: payload,
        onChunk: (value) => setTickets((prevData) => [...prevData, value]),
      });
    } else {
      sortTickets(payload);
    }
  }, [
    departureCity,
    arrivalCity,
    departureDate,
    bus,
    train,
    airplane,
    ferry,
    sortBy,
    ascending,
    searchTickets,
    sortTickets,
  ]);

  return (
    <div className="main-block main">
      <div className="container">
        <Ad />
        <Transport loading={loading} />
        <SearchField loading={loading} />
        {error && <Error />}
        {tickets.length > 0 && (
          <>
            <Tourist />
            <Filters loading={loading} />
            <div className="tickets">
              {tickets.map((item) => (
                <Ticket key={item.data.id} data={item.data} />
              ))}
            </div>
          </>
        )}
        {tickets.length === 0 && loading && <Loader />}
        {tickets.length === 0 && <Ad />}
        <Partner />
      </div>
    </div>
  );
}
