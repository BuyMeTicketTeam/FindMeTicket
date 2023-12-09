import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Calendar from './Calendar';

test('Calendar renders with current date', () => {
  render(<Calendar />);

  const currentDate = new Date();
  const formattedDate = `${currentDate.getDate()}/${currentDate.getMonth() + 1}/${currentDate.getFullYear()}`;
  expect(screen.getByDisplayValue(formattedDate)).toBeInTheDocument();
});

test('Selecting a new date updates the state', () => {
  render(<Calendar />);

  const newDate = '15/01/2023';
  fireEvent.change(screen.getByText('Дата відправки'), { target: { value: newDate } });

  expect(screen.getByDisplayValue(newDate)).toBeInTheDocument();
});
