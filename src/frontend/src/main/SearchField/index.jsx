/* eslint-disable no-shadow */
/* eslint-disable no-unused-vars */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-restricted-syntax */
/* eslint-disable react/jsx-no-bind */
import React, { useState } from 'react';
import AsyncSelect from 'react-select/async';
import { useTranslation } from 'react-i18next';
import {
  useSearchParams, Link, useNavigate, useLocation,
} from 'react-router-dom';
import Button from '../../utils/Button';
import Calendar from '../Calendar';
import makeQuerry from '../../helper/querry';
import arrowsImg from './arrows.svg';
import eventSourceQuery2 from '../../helper/eventSourceQuery2';
import './searchField.scss';

export default function SearchField({
  setLoading, setTicketsData, setError, loading,
}) {
  const [searchParams] = useSearchParams();
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'search' });
  const [cityFrom, setCityFrom] = useState('');
  const [cityTo, setCityTo] = useState('');
  const [errorCityFrom, onErrorCityFrom] = useState(false);
  const [errorCityTo, onErrorCityTo] = useState(false);
  const [date, onDate] = useState(new Date());
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? (t('error')) : null);
  const navigate = useNavigate();
  const location = useLocation();

  function validation() {
    if (!cityFrom && !cityTo) {
      onErrorCityFrom(true);
      onErrorCityTo(true);
      return false;
    }
    if (!cityFrom) {
      onErrorCityFrom(true);
      return false;
    }
    if (!cityTo) {
      onErrorCityTo(true);
      return false;
    }
    return true;
  }

  function sendRequestEvents(body) {
    const requestBody = {
      ...body,
      departureDate: `${body.departureDate.getFullYear()}-${body.departureDate.getMonth() + 1}-${body.departureDate.getDate()}`,
    };
    setLoading(true);
    setTicketsData([]);
    function handleOpen(res) {
      switch (res.status) {
        case 200:
          console.log('open successfully');
          break;
        case 404:
          setError(t('ticket-not-found'));
          break;
        default:
          setError(true);
          break;
      }
    }

    function handleMessage(event) {
      const parsedData = JSON.parse(event.data);
      setTicketsData((prevTickets) => [...prevTickets, parsedData]);
    }

    function handleError() {
      setLoading(false);
    }

    function handleClose() {
      setLoading(false);
    }

    eventSourceQuery2({
      address: 'searchTickets',
      body: JSON.stringify(requestBody),
      handleOpen,
      handleMessage,
      handleError,
      handleClose,
      method: 'POST',
      headers: { 'Content-Language': i18n.language.toLowerCase() },
    });
  }

  function sendRequestHTTP(body) {
    const requestBody = {
      ...body,
      departureDate: `${body.departureDate.getFullYear()}-${body.departureDate.getMonth() + 1}-${body.departureDate.getDate()}`,
    };
    makeQuerry('selectedTransport', JSON.stringify(requestBody), { 'Content-Language': i18n.language.toLowerCase() })
      .then((response) => {
        setTicketsData(response.body);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
      });
  }

  function handleRequest() {
    if (!validation()) {
      return;
    }
    setError(null);
    navigate(`?type=${searchParams.get('type')}&from=${cityFrom.value}&to=${cityTo.value}&departureDate=${+date}&endpoint=1`);
  }

  useEffect(() => {
    if (!(/^.*type=(bus|train|all).*$/).test(location.search)) {
      navigate(`?${location.search.replace('?', '')}&type=all`);
    }
    const body = {
      departureCity: searchParams.get('from'),
      arrivalCity: searchParams.get('to'),
      departureDate: searchParams.get('departureDate') ? new Date(+searchParams.get('departureDate')) : date,
      bus: searchParams.get('type') === 'bus' || searchParams.get('type') === 'all',
      train: searchParams.get('type') === 'train' || searchParams.get('type') === 'all',
      ferry: false,
      airplane: false,
    };
    const endpoint = searchParams.get('endpoint');
    onDate(body.departureDate);
    setCityFrom(body.departureCity ? { value: body.departureCity, label: body.departureCity } : '');
    setCityTo(body.arrivalCity ? { value: body.arrivalCity, label: body.arrivalCity } : '');
    if (searchParams.size < 3 || !body.departureCity || !body.arrivalCity || !endpoint) {
      setTicketsData([]);
      return;
    }
    if (endpoint === '2') {
      sendRequestHTTP(body);
      return;
    }
    sendRequestEvents(body);
  }, [searchParams]);

  function changeCities() {
    const cityToTemp = cityTo;
    setCityTo(cityFrom);
    setCityFrom(cityToTemp);
  }

  function transformData(item) {
    switch (true) {
      case item.siteLanguage === 'ua' && item.cityEng !== null:
        return ({ value: item.cityUa, label: `${item.cityUa} (${item.cityEng}), ${item.country}` });
      case item.siteLanguage === 'ua' && item.cityEng === null:
        return ({ value: item.cityUa, label: `${item.cityUa}, ${item.country}` });
      case item.siteLanguage === 'eng' && item.cityUa === null:
        return ({ value: item.cityEng, label: `${item.cityEng}, ${item.country}` });
      default:
        return ({ value: item.cityEng, label: `${item.cityEng} (${item.cityUa}), ${item.country}` });
    }
  }

  let timerId;
  async function getCities(inputValue, updateState) {
    if (inputValue.length > 1) {
      clearInterval(timerId);
      const result = await new Promise((resolve) => {
        timerId = setTimeout(async () => {
          const response = await makeQuerry('typeAhead', JSON.stringify({ startLetters: inputValue }), { 'Content-language': i18n.language.toLowerCase() });
          const responseBody = response.status === 200 ? response.body.map(transformData) : [];
          resolve(responseBody);
        }, 500);
      });
      updateState(result[0]);
      return result;
    }
    return [];
  }

  return (
    <div className="search-field">
      <div className={`field ${errorCityFrom ? 'error-select' : ''}`}>
        <div className="field__name">{t('where')}</div>
        <AsyncSelect
          aria-label="cityFromSelect"
          isClearable
          value={cityFrom}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => t('loading')}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={(inputValue) => getCities(inputValue, setCityFrom)}
          placeholder="Київ"
          onChange={setCityFrom}
          onInputChange={() => onErrorCityFrom(false)}
        />
        {errorCityFrom && <p data-testid="errorCityFrom" className="search-field__error">{t('error2')}</p>}
      </div>
      <button
        className="search-field__img"
        type="button"
        onClick={changeCities}
      >
        <img src={arrowsImg} alt="arrows" />
      </button>
      <div className={`field ${errorCityTo ? 'error-select' : ''}`}>
        <div className="field__name">{t('where-t')}</div>
        <AsyncSelect
          isClearable
          aria-label="cityToSelect"
          value={cityTo}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => t('loading')}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={(inputValue) => getCities(inputValue, setCityTo)}
          placeholder="Одеса"
          onChange={setCityTo}
          onInputChange={() => onErrorCityTo(false)}
        />
        {errorCityTo && <p data-testid="errorCityTo" className="search-field__error">{t('error2')}</p>}
      </div>
      <Calendar date={date} onDate={onDate} />
      <Button name={t('find')} onButton={handleRequest} disabled={loading} />
    </div>
  );
}
