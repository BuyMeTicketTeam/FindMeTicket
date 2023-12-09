import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Ticket from './Ticket';

const mockData = {
  dateFrom: '2023-01-01',
  travelTime: '2h 30m',
  dateArrival: '2023-01-02',
  placeFrom: 'City A',
  placeTo: 'City B',
  placeFromDetails: 'Station A',
  placeToDetails: 'Station B',
  priceOrdinary: 100,
  priceOld: 120,
  tickerCarrier: 'nikkaBus',
};

test('Ticket component renders with correct data', () => {
  render(<Ticket data={mockData} />);

  expect(screen.getByText(mockData.dateFrom)).toBeInTheDocument();
  expect(screen.getByText(mockData.travelTime)).toBeInTheDocument();
  expect(screen.getByText(mockData.dateArrival)).toBeInTheDocument();
  expect(screen.getByText(mockData.placeFrom)).toBeInTheDocument();
  expect(screen.getByText(mockData.placeTo)).toBeInTheDocument();
  expect(screen.getByText(mockData.placeFromDetails)).toBeInTheDocument();
  expect(screen.getByText(mockData.placeToDetails)).toBeInTheDocument();
  expect(screen.getByText(`${mockData.priceOrdinary} грн`)).toBeInTheDocument();
  expect(screen.getByText(`${mockData.priceOld} грн`)).toBeInTheDocument();
  expect(screen.getByText('Купити')).toBeInTheDocument();
  expect(screen.getByText(`Перевізник: ${mockData.tickerCarrier}`)).toBeInTheDocument();
});
