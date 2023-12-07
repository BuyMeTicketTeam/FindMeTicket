/* eslint-disable react/jsx-no-bind */
import React, { useState, useEffect } from 'react';
import AsyncSelect from 'react-select/async';
import Field from '../utils/Field';
import Button from '../utils/Button';
// import DropDown from './DropDown';
import Calendar from './Calendar';
import Passangers from './Passangers';
import makeQuerry from '../helper/querry';
import './main.css';

export default function SearchField() {
  const [adultsValue, onAdultsValue] = useState(1);
  const [childrenValue, onChildrenValue] = useState(0);
  const [passanger, onPassangers] = useState('1 Дорослий, 0 Дитина');
  const [send, onSend] = useState(false);
  const [showPassangers, onShowPassangers] = useState(false);
  const fieldRef = React.createRef();
  // const from = [
  //   { value: 'Дніпро', label: 'Дніпро' },
  //   { value: 'Київ', label: 'Київ' },
  //   { value: 'Одеса', label: 'Одеса' },
  // ];
  // const destination = [
  //   { value: 'Дніпро', label: 'Дніпро' },
  //   { value: 'Київ', label: 'Київ' },
  //   { value: 'Одеса', label: 'Одеса' },
  // ];

  function showPassangersDrop() {
    onShowPassangers(!showPassangers);
  }

  useEffect(() => {
    const checkIfClickedOutside = (e) => {
      // If the menu is open and the clicked target is not within the menu,
      // then close the menu
      if (showPassangers && fieldRef.current && !fieldRef.current.contains(e.target)) {
        onShowPassangers(false);
      }
    };
    document.addEventListener('mousedown', checkIfClickedOutside);

    return () => {
      // Cleanup the event listener
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

  function handleClick() {
    onSend(false);
    makeQuerry('get1', undefined, undefined, 'GET')
      .then((response) => {
        if (response.status === 200) {
          console.log(`status: ${200}`);
        } else if (response.status === 401) {
          console.log(`status: ${401}`);
        } else if (response.status === 403) {
          console.log(`status: ${403}`);
        } else {
          console.log('status: other');
        }
      });
  }

  useEffect(() => {
    if (send) {
      handleClick();
    }
  }, [send]);
  let timerId;
  async function getFromCities(inputValue) {
    if (inputValue.length > 1) {
      clearInterval(timerId);
      const result = await new Promise((resolve) => {
        timerId = setTimeout(async () => {
          const response = await makeQuerry('cities', JSON.stringify({ value: inputValue }));
          resolve(response.body);
        }, 500);
      });
      return result;
    }
    return [];
  }

  async function getToCities(inputValue) {
    if (inputValue.length > 1) {
      const result = await new Promise((resolve) => {
        setTimeout(async () => {
          const response = await makeQuerry('cities', JSON.stringify({ value: inputValue }));
          resolve(response.body);
        }, 500);
      });

      return result;
    }
    return [];
  }

  return (
    <div className="search-field">
      <div className="field ">
        <div className="field__name">Звідки</div>
        <AsyncSelect noOptionsMessage={() => null} loadingMessage={() => 'Завантаження...'} cacheOptions classNamePrefix="react-select" loadOptions={getFromCities} placeholder={null} />
      </div>
      <div className="search-field__img"><img src="../img/arrows.svg" alt="arrows" /></div>
      <div className="field">
        <div className="field__name">Куди</div>
        <AsyncSelect noOptionsMessage={() => null} loadingMessage={() => 'Завантаження...'} cacheOptions classNamePrefix="react-select" loadOptions={getToCities} placeholder={null} />
      </div>
      <Calendar />
      <Field ref={fieldRef} className="search-field__tip-long" name="Пасажири" value={passanger} type="text" tip={<Passangers status={showPassangers} adultsValue={adultsValue} onAdultsValue={onAdultsValue} childrenValue={childrenValue} onChildrenValue={onChildrenValue} />} onClick={() => showPassangersDrop()} />
      <Button name="Знайти" onButton={onSend} />
    </div>
  );
}
