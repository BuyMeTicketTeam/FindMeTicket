import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import DropDown from './DropDown';

test('DropDown renders with correct items', () => {
  render(<DropDown />);

  const items = screen.getAllByRole('listitem');
  expect(items).toHaveLength(3);

  const cities = screen.getAllByTestId('dropDownCity');
  const countries = screen.getAllByTestId('dropDownCountry');

  expect(cities).toHaveLength(3);
  expect(countries).toHaveLength(3);

  expect(cities[0]).toHaveTextContent('Дніпро');
  expect(countries[0]).toHaveTextContent('Україна');
});
