/* eslint-disable no-param-reassign */
import React from 'react';
import {
  render, fireEvent, waitFor, screen,
} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import SearchField from './index';
import '../../locales/i18n';

test('renders SearchField component with initial values', () => {
  render(<SearchField onLoading={() => {}} onTicketsData={() => {}} setRequestBody={() => {}} />);

  expect(screen.getByLabelText('cityFromSelect')).toBeInTheDocument();
  expect(screen.getByLabelText('cityToSelect')).toBeInTheDocument();
  expect(screen.getByText('Знайти')).toBeInTheDocument();
});

test('displays error messages when submitting without selecting cities', async () => {
  render(<SearchField onLoading={() => {}} onTicketsData={() => {}} setRequestBody={() => {}} />);

  fireEvent.click(screen.getByText('Знайти'));

  await waitFor(() => {
    expect(screen.getByTestId('errorCityFrom')).toBeInTheDocument();
    expect(screen.getByTestId('errorCityTo')).toBeInTheDocument();
  });
});

// test('swaps departure and arrival cities when arrows button is clicked', async () => {
// render(<SearchField onLoading={() => {}} onTicketsData={() => {}} setRequestBody={() => {}} />);

//   fireEvent.change(screen.getByLabelText('cityFromSelect'), { target: { inputValue: 'Київ' } });
//   await waitFor(() => {
//     expect(screen.getByText('Київ, Україна')).toBeInTheDocument();
//   });
//   fireEvent.click(screen.getByText('Київ, Україна'));

//   fireEvent.change(screen.getByLabelText('cityToSelect'), { target: { inputValue: 'Одеса' } });
//   fireEvent.click(screen.getByText('Одеса, Україна'));

//   fireEvent.click(screen.getByAltText('arrows'));

//   expect(screen.getByText('Київ, Україна')).toBeInTheDocument();
//   expect(screen.getByText('Одеса, Україна')).toBeInTheDocument();
// });
