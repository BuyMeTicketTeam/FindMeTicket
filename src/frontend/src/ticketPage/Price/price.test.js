/* eslint-disable no-undef */
import React from 'react';
import { render, screen } from '@testing-library/react';
import PriceBlock from './PriceBlock'; // Make sure the path is correct

test('PriceBlock component renders correctly', () => {
  render(<PriceBlock />);

  const priceBlock = screen.getByTestId('price-block');
  expect(priceBlock).toBeInTheDocument();

  const labelText = screen.getByText('Придбати на Busfor.ua:');
  expect(labelText).toBeInTheDocument();

  const priceContainer = screen.getByTestId('price-container');
  expect(priceContainer).toBeInTheDocument();

  const price = screen.getByText('400 грн');
  expect(price).toBeInTheDocument();

  const discountedPrice = screen.getByText('800 грн');
  expect(discountedPrice).toBeInTheDocument();

  const buyButton = screen.getByText('Купити');
  expect(buyButton).toBeInTheDocument();
});
