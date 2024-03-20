import React from 'react';
import { render, screen } from '@testing-library/react';
import FiltersBtn from './FiltersBtn';

test('renders FiltersBtn component', () => {
  const onClickMock = jest.fn();

  render(
    <FiltersBtn
      isDown={false}
      isUp={false}
      onClick={onClickMock}
      sortType="price-low"
      reverse="price-up"
    >
      Price
    </FiltersBtn>,
  );

  const filterBtn = screen.getByText(/price/i);
  expect(filterBtn).toBeInTheDocument();
});

test('applies correct styles based on isDown and isUp props', () => {
  render(
    <FiltersBtn
      isDown
      isUp
      onClick={() => {}}
      sortType="price-low"
      reverse="price-up"
    >
      Price
    </FiltersBtn>,
  );

  const filterBtn = screen.getByText(/price/i);
  expect(filterBtn).toHaveClass('active');
  expect(filterBtn).toHaveClass('up');
});
