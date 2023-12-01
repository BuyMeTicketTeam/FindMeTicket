import React, { useState, useEffect } from 'react';
import Field from '../utils/Field';
import Button from '../utils/Button';
import DropDown from './DropDown';
import Calendar from './Calendar';
import Passangers from './Passangers';
import makeQuerry from '../helper/querry';
import './main.css';

export default function SearchField() {
  const [from, onFrom] = useState('');
  const [destination, onDestination] = useState('');
  const [passanger, onPassanger] = useState('1 Дорослий, 1 Дитина');
  const [send, onSend] = useState(false);
  function handleClick() {
    onSend(false);
    makeQuerry('get1')
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
      <Field className="search-field__input-long" name="Звідки" value={from} type="text" onInputChange={onFrom} tip={<DropDown />} />
      <div className="search-field__img"><img src="../img/arrows.svg" alt="arrows" /></div>
      <Field className="search-field__input-long" name="Куди" value={destination} type="text" onInputChange={onDestination} tip={<DropDown />} />
      <Calendar />
      <Field className="search-field__tip-long" name="Пасажири" value={passanger} type="text" onInputChange={onPassanger} tip={<Passangers />} />
      <Button name="Знайти" onButton={onSend} />
    </div>
  );
}
