/* eslint-disable no-undef */
/* eslint-disable import/extensions */
/* eslint-disable import/no-unresolved */
import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import Maps from './index';

test('Maps component renders correctly without testing images', () => {
  render(<Maps />);

  const hotelsCategory = screen.getByText('Готелі');
  const restaurantsCategory = screen.getByText('Ресторани');
  const touristPlacesCategory = screen.getByText('Туристичні місця');

  const horizontalLine = screen.getByTestId('horizontal-line');
  expect(screen.queryByAltText('HotelsMap')).toBeInTheDocument();
  expect(screen.queryByAltText('RestaurantMap')).toBeNull();
  expect(screen.queryByAltText('TouristPlaces')).toBeNull();

  fireEvent.click(hotelsCategory);

  expect(screen.queryByAltText('HotelsMap')).toBeNull();

  expect(horizontalLine).toBeInTheDocument();

  fireEvent.mouseEnter(restaurantsCategory);

  expect(restaurantsCategory).toHaveClass('hovered');

  fireEvent.click(restaurantsCategory);

  expect(screen.queryByAltText('RestaurantMap')).toBeInTheDocument();

  fireEvent.mouseLeave(restaurantsCategory);

  expect(horizontalLine).toBeInTheDocument();

  expect(hotelsCategory).toHaveClass('category');
  expect(restaurantsCategory).toHaveClass('category');
  expect(touristPlacesCategory).toHaveClass('category');
});
