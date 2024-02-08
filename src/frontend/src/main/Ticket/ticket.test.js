import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Ticket from './index';

const mockData = {
  id: 1,
  departureTime: '12:00',
  departureDate: '2023-01-01',
  travelTime: '2h 30m',
  arrivalTime: '14:30',
  arrivalDate: '2023-01-01',
  departureCity: 'City A',
  arrivalCity: 'City B',
  placeFrom: 'Station A',
  placeAt: 'Station B',
  price: 100,
  priceOld: 120,
  url: 'https://example.com',
};

test('renders ticket component with correct data', () => {
  render(<Ticket data={mockData} />);

  // Check if the ticket information is rendered correctly
  expect(screen.getByText('12:00 | 2023-01-01')).toBeInTheDocument();
  expect(screen.getByText('2h 30m')).toBeInTheDocument();
  expect(screen.getByText('14:30 | 2023-01-01')).toBeInTheDocument();
  expect(screen.getByText('City A')).toBeInTheDocument();
  expect(screen.getByText('City B')).toBeInTheDocument();
  expect(screen.getByText('Station A')).toBeInTheDocument();
  expect(screen.getByText('Station B')).toBeInTheDocument();

  // Check if the price information is rendered correctly
  expect(screen.getByText('100 uan')).toBeInTheDocument();
  expect(screen.getByText('120 uan')).toBeInTheDocument(); // Check for discounted price
  expect(screen.getByText('select')).toBeInTheDocument();

  // Check if the link has the correct href
  const link = screen.getByText('select');
  expect(link).toHaveAttribute('href', '/ticket-page/1');
});

test('truncates long station names', () => {
  const longStationData = {
    ...mockData,
    placeFrom: 'This is a very long station name that needs to be truncated',
    placeAt: 'Another very long station name that needs to be truncated',
  };

  render(<Ticket data={longStationData} />);

  // Check if long station names are truncated
  expect(screen.getByText('This is a very long ...')).toBeInTheDocument();
  expect(screen.getByText('Another very long st...')).toBeInTheDocument();
});

test('does not display old price if it is not available', () => {
  const dataWithoutOldPrice = {
    ...mockData,
    priceOld: null,
  };

  render(<Ticket data={dataWithoutOldPrice} />);

  // Check if the old price is not displayed
  expect(screen.queryByText('120 uan')).not.toBeInTheDocument();
});
