import React from 'react';
import { render, screen } from '@testing-library/react';
import Filters from './index';

test('renders filters component', () => {
  const onSortMock = jest.fn();

  render(<Filters onSort={onSortMock} prevSort="price-low" />);

  const priceBtn = screen.getByText('price');
  const travelTimeBtn = screen.getByText('travel-time');
  const departureTimeBtn = screen.getByText('departure-time');
  const arrivalTimeBtn = screen.getByText('arrival-time');

  expect(priceBtn).toBeInTheDocument();
  expect(travelTimeBtn).toBeInTheDocument();
  expect(departureTimeBtn).toBeInTheDocument();
  expect(arrivalTimeBtn).toBeInTheDocument();
});
