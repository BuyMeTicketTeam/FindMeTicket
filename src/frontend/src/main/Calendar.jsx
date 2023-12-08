/* eslint-disable import/no-extraneous-dependencies */
import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import { useTranslation } from 'react-i18next';
import 'react-datepicker/dist/react-datepicker.css';

export default function Calendar() {
  const [startDate, setStartDate] = useState(new Date());
  const { t } = useTranslation('translation', { keyPrefix: 'main-calendar' });
  const days = t('days', { returnObjects: true });
  const months = t('months', { returnObjects: true });

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
      <div className="field__name">{t('title')}</div>
      <DatePicker minDate={new Date()} locale={locale} dateFormat="dd/MM/yyyy" closeOnScroll calendarClassName="calendar" className="input" selected={startDate} onChange={(date) => setStartDate(date)} />
    </div>
  );
}
