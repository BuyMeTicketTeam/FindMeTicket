/* eslint-disable no-restricted-syntax */
/* eslint-disable react/jsx-no-bind */
import React, { useState, useEffect } from 'react';
import AsyncSelect from 'react-select/async';
import { useTranslation } from 'react-i18next';
import Field from '../../utils/Field';
import Button from '../../utils/Button';
import Calendar from '../Calendar';
import Passengers from '../Passangers';
import makeQuerry from '../../helper/querry';
import arrowsImg from './arrows.svg';
import eventSourceQuery2 from '../../helper/eventSourceQuery2';
import './searchField.scss';

export default function SearchField({
  onLoading, setTicketsData, setRequestBody, setError,
}) {
  const { t, i18n } = useTranslation('translation', { keyPrefix: 'search' });
  const [adultsValue, setAdultsValue] = useState(1);
  const [childrenValue, onChildrenValue] = useState(0);
  const [cityFrom, onCityFrom] = useState('');
  const [cityTo, onCityTo] = useState('');
  const [errorCityFrom, onErrorCityFrom] = useState(false);
  const [errorCityTo, onErrorCityTo] = useState(false);
  const [date, onDate] = useState(new Date());
  const [passanger, onPassengers] = useState(`1 ${t('adults')}, 0 ${t('child')}`);
  const [showPassengers, onShowPassengers] = useState(false);
  const fieldRef = React.createRef();
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? (t('error')) : null);

  function showPassengersDrop() {
    onShowPassengers(!showPassengers);
  }

  useEffect(() => {
    const checkIfClickedOutside = (e) => {
      if (showPassengers && fieldRef.current && !fieldRef.current.contains(e.target)) {
        onShowPassengers(false);
      }
    };
    document.addEventListener('mousedown', checkIfClickedOutside);

    return () => {
      document.removeEventListener('mousedown', checkIfClickedOutside);
    };
  }, [showPassengers, fieldRef]);

  function updatePassangerText() {
    let adults = (t('adult'));
    let kids = (t('child'));
    if (adultsValue > 1) {
      adults = (t('adults'));
    }
    if (childrenValue > 1 && childrenValue <= 4) {
      kids = (t('children'));
    } else if (childrenValue > 4) {
      kids = (t('childrens'));
    }
    onPassengers(`${adultsValue} ${adults}, ${childrenValue} ${kids}`);
  }

  useEffect(() => {
    updatePassangerText();
  }, [childrenValue, adultsValue, t]);

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

  async function sendRequest() {
    if (!validation()) {
      return;
    }
    const body = {
      departureCity: cityFrom.value,
      arrivalCity: cityTo.value,
      departureDate: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`,
    };
    setRequestBody(body);
    onLoading(true);
    setTicketsData([]);

    function handleOpen(res) {
      switch (res.status) {
        case 200:
          console.log('open successfully');
          break;
        case 204:
          setError('Нажаль квитків за цим маршрутом не знайдено');
          break;
        default:
          setError(true);
          break;
      }
    }

    function handleMessage(event) {
      console.log(event);
      const parsedData = JSON.parse(event.data);
      setTicketsData((prevTickets) => [...prevTickets, parsedData]);
      onLoading(false);
    }

    function handleError() {
      onLoading(false);
    }

    eventSourceQuery2({
      address: 'searchTickets',
      body: JSON.stringify(body),
      handleOpen,
      handleMessage,
      handleError,
      method: 'POST',
      headers: { 'Content-Language': i18n.language.toLowerCase() },
    });
  }

  function changeCities() {
    const cityToTemp = cityTo;
    onCityTo(cityFrom);
    onCityFrom(cityToTemp);
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
  async function getCities(inputValue) {
    if (inputValue.length > 1) {
      clearInterval(timerId);
      const result = await new Promise((resolve) => {
        timerId = setTimeout(async () => {
          const response = await makeQuerry('typeAhead', JSON.stringify({ startLetters: inputValue }), { 'Content-language': i18n.language.toLowerCase() });
          const responseBody = response.status === 200 ? response.body.map(transformData) : [];
          resolve(responseBody);
        }, 500);
      });
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
          loadOptions={getCities}
          placeholder="Київ"
          onChange={onCityFrom}
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
          loadOptions={getCities}
          placeholder="Одеса"
          onChange={onCityTo}
          onInputChange={() => onErrorCityTo(false)}
        />
        {errorCityTo && <p data-testid="errorCityTo" className="search-field__error">{t('error2')}</p>}
      </div>
      <Calendar date={date} onDate={onDate} />
      <Field
        dataTestId="passengers"
        ref={fieldRef}
        className="search-field__tip-long"
        name={t('passengers')}
        value={passanger}
        type="text"
        tip={(
          <Passengers
            status={showPassengers}
            adultsValue={adultsValue}
            setAdultsValue={setAdultsValue}
            childrenValue={childrenValue}
            onChildrenValue={onChildrenValue}
          />
)}
        onClick={() => showPassengersDrop()}
      />
      <Button name={t('find')} onButton={sendRequest} />
    </div>
  );
}
