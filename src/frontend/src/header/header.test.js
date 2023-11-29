/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen,
} from '@testing-library/react';
import App from '../App';

test('render header', () => {
  render(<App />);
  const headerElement = screen.getByTestId('header');
  expect(headerElement).toBeInTheDocument();
});
