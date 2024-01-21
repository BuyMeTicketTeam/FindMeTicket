/* eslint-disable no-undef */
import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';

import Footer from './index';

test('Footer renders correctly and toggles detailed info', () => {
  const { getByText, queryByText, getByTestId } = render(<Footer />);

  expect(queryByText('Туристичні місця')).toBeNull();

  const arrowIcon = getByTestId('Toggle Footer');
  fireEvent.click(arrowIcon);

  expect(getByText('Туристичні місця')).toBeInTheDocument();

  fireEvent.click(arrowIcon);

  expect(queryByText('Туристичні місця')).toBeNull();
});
