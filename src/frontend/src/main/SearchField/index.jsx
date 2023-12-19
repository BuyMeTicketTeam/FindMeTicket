/* eslint-disable max-len */
/* eslint-disable react/jsx-no-bind */
import React, { useState, useEffect } from 'react';
import AsyncSelect from 'react-select/async';
import Field from '../../utils/Field';
import Button from '../../utils/Button';
import Calendar from '../Calendar';
import Passangers from '../Passangers';
import makeQuerry from '../../helper/querry';
import './searchField.css';

export default function SearchField({ onLoading, onTicketsData, setRequestBody }) {
  const [adultsValue, onAdultsValue] = useState(1);
  const [childrenValue, onChildrenValue] = useState(0);
  const [cityFrom, onCityFrom] = useState('');
  const [cityTo, onCityTo] = useState('');
  const [errorCityFrom, onErrorCityFrom] = useState('');
  const [errorCityTo, onErrorCityTo] = useState('');
  const [date, onDate] = useState(new Date());
  const [passanger, onPassangers] = useState('1 Дорослий, 0 Дитина');
  const [send, onSend] = useState(false);
  const [showPassangers, onShowPassangers] = useState(false);
  const fieldRef = React.createRef();
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? 'За вашим запитом нічого не знайдено' : null);

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

  useEffect(() => {
    let adults = 'Дорослий';
    let kids = 'Дитина';
    if (adultsValue > 1) {
      adults = 'Дорослих';
    }
    if (childrenValue > 1 && childrenValue <= 4) {
      kids = 'Дитини';
    } else if (childrenValue > 4) {
      kids = 'Дітей';
    }
    onPassangers(`${adultsValue} ${adults}, ${childrenValue} ${kids}`);
  }, [childrenValue, adultsValue]);

  function validation() {
    if (!cityFrom && !cityTo) {
      onErrorCityFrom('Поле не повинно бути порожнім');
      onErrorCityTo('Поле не повинно бути порожнім');
      return false;
    }
    if (!cityFrom) {
      onErrorCityFrom('Поле не повинно бути порожнім');
      return false;
    }
    if (!cityTo) {
      onErrorCityTo('Поле не повинно бути порожнім');
      return false;
    }
    return true;
  }

  async function handleClick() {
    onSend(false);
    if (!validation()) {
      return;
    }
    const body = {
      departureCity: cityFrom.value,
      arrivalCity: cityTo.value,
      departureDate: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`,
      indexFrom: 0,
    };
    setRequestBody(body);
    onLoading(true);
    const response = await makeQuerry('getfirsttickets', JSON.stringify(body));
    onLoading(false);
    const responseBody = response.status === 200 ? response.body : null;
    onTicketsData(responseBody);
  }

  function changeCities() {
    const cityToTemp = cityTo;
    onCityTo(cityFrom);
    onCityFrom(cityToTemp);
  }

  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);

  let timerId;
  async function getCities(inputValue) {
    if (inputValue.length > 1) {
      clearInterval(timerId);
      const result = await new Promise((resolve) => {
        timerId = setTimeout(async () => {
          const response = await makeQuerry('typeAhead', JSON.stringify({ startLetters: inputValue }));
          const responseBody = response.status === 200 ? response.body.map((item) => ({ value: item.cityUkr, label: `${item.cityUkr}, ${item.country}` })) : [];
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
        <div className="field__name">Звідки</div>
        <AsyncSelect
          isClearable
          value={cityFrom}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => 'Завантаження...'}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={getCities}
          placeholder="Київ"
          onChange={onCityFrom}
          onInputChange={() => onErrorCityFrom('')}
        />
        {errorCityFrom !== '' && <p data-testid="error" className="search-field__error">{errorCityFrom}</p>}
      </div>
      <button
        className="search-field__img"
        type="button"
        onClick={changeCities}
      >
        <img src="../img/arrows.svg" alt="arrows" />
      </button>
      <div className={`field ${errorCityTo ? 'error-select' : ''}`}>
        <div className="field__name">Куди</div>
        <AsyncSelect
          isClearable
          value={cityTo}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => 'Завантаження...'}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={getCities}
          placeholder="Одеса"
          onChange={onCityTo}
          onInputChange={() => onErrorCityTo('')}
        />
        {errorCityTo !== '' && <p data-testid="error" className="search-field__error">{errorCityTo}</p>}
      </div>
      <Calendar date={date} onDate={onDate} />
      <Field
        ref={fieldRef}
        className="search-field__tip-long"
        name="Пасажири"
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
      <Button name="Знайти" onButton={onSend} />
    </div>
  );
}
