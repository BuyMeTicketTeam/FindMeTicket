/* eslint-disable import/no-extraneous-dependencies */
import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export default function Calendar() {
  const [startDate, setStartDate] = useState(new Date());
  const days = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Нд'];
  const months = ['Січень', 'Лютий', 'Березень', 'Квітень', 'Травень', 'Червень', 'Липень', 'Серпень', 'Вересень', 'Жотень', 'Листопад', 'Грудень'];

  const locale = {
    localize: {
      day: (n) => days[n],
      month: (n) => months[n],
    },
    formatLong: {
      date: () => 'mm/dd/yyyy',
    },
  };
  return (
    <div className="field">
      <div className="field__name">Дата відправки</div>
      <DatePicker
        minDate={new Date()}
        locale={locale}
        dateFormat="dd/MM/yyyy"
        closeOnScroll
        calendarClassName="calendar"
        className="input"
        selected={startDate}
        onChange={(date) => setStartDate(date)}
      />

    </div>
  );
}
