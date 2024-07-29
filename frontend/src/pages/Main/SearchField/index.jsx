import React, { useState } from "react";
import AsyncSelect from "react-select/async";
import { useTranslation } from "react-i18next";
import { useSelector, useDispatch } from "react-redux";

import useGetCities from "../../../hook/useGetCities";
import {
  setCities,
  setDepartureDate,
} from "../../../redux/store/tickets/ticketsSlice";

import Calendar from "../Calendar";

import arrowsImg from "./arrows.svg";

import "./searchField.scss";

export default function SearchField({ loading }) {
  const { t } = useTranslation("translation", { keyPrefix: "search" });
  const [cityFrom, setCityFrom] = useState("");
  const [cityTo, setCityTo] = useState("");
  const [errorCityFrom, setErrorCityFrom] = useState(false);
  const [errorCityTo, setErrorCityTo] = useState(false);
  const { departureDate } = useSelector((state) => state.tickets);
  const dispatch = useDispatch();
  const setDate = (date) => dispatch(setDepartureDate(date));
  const getCities = useGetCities();
  const noOptionsMessage = (target) =>
    target.inputValue.length > 1 ? t("error") : null;

  function validation() {
    if (!cityFrom && !cityTo) {
      setErrorCityFrom(true);
      setErrorCityTo(true);
      return false;
    }
    if (!cityFrom) {
      setErrorCityFrom(true);
      return false;
    }
    if (!cityTo) {
      setErrorCityTo(true);
      return false;
    }
    return true;
  }

  function handleRequest() {
    if (!validation()) {
      return;
    }

    dispatch(
      setCities({
        arrivalCity: cityTo.value,
        departureCity: cityFrom.value,
      })
    );
  }

  function changeCities() {
    const cityToTemp = cityTo;
    setCityTo(cityFrom);
    setCityFrom(cityToTemp);
  }

  return (
    <form className="search-field">
      <div className={`field ${errorCityFrom ? "error-select" : ""}`}>
        <label htmlFor="cityFrom" className="field__name">
          {t("where")}
        </label>
        <AsyncSelect
          id="cityFrom"
          aria-label="cityFromSelect"
          isClearable
          value={cityFrom}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => t("loading")}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={(inputValue) => getCities(inputValue, setCityFrom)}
          placeholder={t("from-placeholder")}
          onChange={setCityFrom}
          onInputChange={() => setErrorCityFrom(false)}
        />
        {errorCityFrom && (
          <p data-testid="errorCityFrom" className="search-field__error">
            {t("error2")}
          </p>
        )}
      </div>
      <button
        className="search-field__img"
        type="button"
        onClick={changeCities}
      >
        <img src={arrowsImg} alt="arrows" />
      </button>
      <div className={`field ${errorCityTo ? "error-select" : ""}`}>
        <label htmlFor="cityTo" className="field__name">
          {t("where-t")}
        </label>
        <AsyncSelect
          id="cityTo"
          isClearable
          aria-label="cityToSelect"
          value={cityTo}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => t("loading")}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={(inputValue) => getCities(inputValue, setCityTo)}
          placeholder={t("to-placeholder")}
          onChange={setCityTo}
          onInputChange={() => setErrorCityTo(false)}
        />
        {errorCityTo && (
          <p data-testid="errorCityTo" className="search-field__error">
            {t("error2")}
          </p>
        )}
      </div>
      <Calendar
        date={departureDate}
        onDate={(date) => setDate(Date.parse(date))}
      />
      <button
        type="button"
        className="button"
        onClick={handleRequest}
        disabled={loading}
      >
        {t("find")}
      </button>
    </form>
  );
}
