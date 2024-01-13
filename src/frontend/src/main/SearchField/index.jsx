/* eslint-disable no-restricted-syntax */
/* eslint-disable react/jsx-no-bind */
import React, { useState, useEffect } from 'react';
import AsyncSelect from 'react-select/async';
import { useTranslation } from 'react-i18next';
import Field from '../../utils/Field';
import Button from '../../utils/Button';
import Calendar from '../Calendar';
import Passangers from '../Passangers';
import makeQuerry from '../../helper/querry';
import arrowsImg from './arrows.svg';
import eventSourceQuery from '../../helper/eventSourceQuery';
import './searchField.css';

export default function SearchField({ onLoading, setRequestBody, onTicketsData }) {
  const { t } = useTranslation('translation', { keyPrefix: 'search' });
  const [adultsValue, onAdultsValue] = useState(1);
  const [childrenValue, onChildrenValue] = useState(0);
  const [cityFrom, onCityFrom] = useState('');
  const [cityTo, onCityTo] = useState('');
  const [errorCityFrom, onErrorCityFrom] = useState('');
  const [errorCityTo, onErrorCityTo] = useState('');
  const [date, onDate] = useState(new Date());
  const [passanger, onPassangers] = useState(`1 ${t('adults')}, 0 ${t('child')}`);
  const [showPassangers, onShowPassangers] = useState(false);
  const fieldRef = React.createRef();
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? (t('error')) : null);

  function showPassangersDrop() {
    onShowPassangers(!showPassangers);
  }

  useEffect(() => {
    const checkIfClickedOutside = (e) => {
      if (showPassangers && fieldRef.current && !fieldRef.current.contains(e.target)) {
        onShowPassangers(false);
      }
    };
    document.addEventListener('mousedown', checkIfClickedOutside);

    return () => {
      document.removeEventListener('mousedown', checkIfClickedOutside);
    };
  }, [showPassangers, fieldRef]);

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
    onPassangers(`${adultsValue} ${adults}, ${childrenValue} ${kids}`);
  }

  useEffect(() => {
    updatePassangerText();
  }, [childrenValue, adultsValue, t]);

  function validation() {
    if (!cityFrom && !cityTo) {
      onErrorCityFrom((t('error2')));
      onErrorCityTo((t('error2')));
      return false;
    }
    if (!cityFrom) {
      onErrorCityFrom((t('error2')));
      return false;
    }
    if (!cityTo) {
      onErrorCityTo((t('error2')));
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
    const dataStream = await eventSourceQuery('searchtickets', JSON.stringify(body));
    onLoading(false);
    for await (const chunk of dataStream) {
      if (chunk) {
        onTicketsData((prevTickets) => [...prevTickets, chunk]);
      }
    }
  }

  function changeCities() {
    const cityToTemp = cityTo;
    onCityTo(cityFrom);
    onCityFrom(cityToTemp);
  }

  function transformData(item) {
    return { value: item.cityUkr, label: `${item.cityUkr}, ${item.country}` };
  }

  let timerId;
  async function getCities(inputValue) {
    if (inputValue.length > 1) {
      clearInterval(timerId);
      const result = await new Promise((resolve) => {
        timerId = setTimeout(async () => {
          const response = await makeQuerry('typeAhead', JSON.stringify({ startLetters: inputValue }));
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
          onInputChange={() => onErrorCityFrom('')}
        />
        {errorCityFrom !== '' && <p data-testid="errorCityFrom" className="search-field__error">{errorCityFrom}</p>}
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
          onInputChange={() => onErrorCityTo('')}
        />
        {errorCityTo !== '' && <p data-testid="errorCityTo" className="search-field__error">{errorCityTo}</p>}
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
          <Passangers
            status={showPassangers}
            adultsValue={adultsValue}
            onAdultsValue={onAdultsValue}
            childrenValue={childrenValue}
            onChildrenValue={onChildrenValue}
          />
)}
        onClick={() => showPassangersDrop()}
      />
      <Button name={t('find')} onButton={sendRequest} />
    </div>
  );
}
