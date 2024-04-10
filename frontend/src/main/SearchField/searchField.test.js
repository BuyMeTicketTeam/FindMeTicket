import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import SearchField from './index';

test('renders city selection fields', () => {
  const setTicketsData = jest.fn();
  const setError = jest.fn();
  render(
    <Router>
      <SearchField setTicketsData={setTicketsData} setError={setError} />
    </Router>,
  );

  const cityFromInput = screen.getByLabelText('cityFromSelect');
  const cityToInput = screen.getByLabelText('cityToSelect');

  expect(cityFromInput).toBeInTheDocument();
  expect(cityToInput).toBeInTheDocument();
});

test('displays error message when city is not selected', async () => {
  const setTicketsData = jest.fn();
  const setError = jest.fn();
  render(
    <Router>
      <SearchField setTicketsData={setTicketsData} setError={setError} />
    </Router>,
  );

  userEvent.click(screen.getByRole('button', { name: /find/i })); // Simulate clicking the "Find" button

  await waitFor(() => {
    const errorFrom = screen.getByTestId('errorCityFrom');
    const errorTo = screen.getByTestId('errorCityTo');

    expect(errorFrom).toHaveTextContent('error2');
    expect(errorTo).toHaveTextContent('error2');
  });
});

test('renders date picker', () => {
  const setTicketsData = jest.fn();
  const setError = jest.fn();
  render(
    <Router>
      <SearchField setTicketsData={setTicketsData} setError={setError} />
    </Router>,
  );

  const datePicker = screen.getByTestId('datepicker'); // Assuming a role for the datepicker

  expect(datePicker).toBeInTheDocument();
});
