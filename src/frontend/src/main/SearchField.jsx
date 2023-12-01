import React, { useState, useEffect } from 'react';
import Select from 'react-select';
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
  const from = [
    { value: 'Дніпро', label: 'Дніпро' },
    { value: 'Київ', label: 'Київ' },
    { value: 'Одеса', label: 'Одеса' },
  ];
  const destination = [
    { value: 'Дніпро', label: 'Дніпро' },
    { value: 'Київ', label: 'Київ' },
    { value: 'Одеса', label: 'Одеса' },
  ];
  function showPassangersDrop() {
    onShowPassangers(!showPassangers);
  }
  useEffect(() => {
    onPassangers(`${adultsValue} Дорослий, ${childrenValue} Дитина`);
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
  return (
    <div className="search-field">
      <div className="field ">
        <div className="field__name">Звідки</div>
        <Select classNamePrefix="react-select" options={from} placeholder={null} />
      </div>
      <div className="search-field__img"><img src="../img/arrows.svg" alt="arrows" /></div>
      <div className="field ">
        <div className="field__name">Куди</div>
        <Select classNamePrefix="react-select" options={destination} placeholder={null} />
      </div>
      <Calendar />
      <Field className="search-field__tip-long" name="Пасажири" value={passanger} type="text" tip={<Passangers status={showPassangers} adultsValue={adultsValue} onAdultsValue={onAdultsValue} childrenValue={childrenValue} onChildrenValue={onChildrenValue} />} onClick={() => showPassangersDrop()} />
      <Button name="Знайти" onButton={onSend} />
    </div>
  );
}
