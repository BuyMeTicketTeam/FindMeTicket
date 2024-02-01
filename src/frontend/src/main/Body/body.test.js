import React from 'react';
import {
  render, screen,
} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // для использования дополнительных матчеров
import Body from './index';

// Мок функции makeQuerry
jest.mock('../../helper/querry', () => ({
  __esModule: true,
  default: jest.fn(),
}));

describe('Body', () => {
  it('renders tickets and Filters component', () => {
    const ticketsData = [
      {
        id: 1,
        departureTime: '6:40',
        departureDate: '30.11, вт',
        travelTime: '9 год 5 хв',
        arrivalTime: '15:30',
        arrivalDate: '29.11, вт',
        departureCity: 'Одеса',
        arrivalCity: 'Київ',
        placeFrom: 'Автовокзал "Центральний"',
        placeAt: 'Южный автовокзал',
        price: '1000',
        tickerCarrier: 'nikkaBus',
        url: 'https://google.com',
      },
      {
        id: 2,
        departureTime: '6:40',
        departureDate: '30.11, вт',
        travelTime: '9 год 5 хв',
        arrivalTime: '15:30',
        arrivalDate: '29.11, вт',
        departureCity: 'Одеса',
        arrivalCity: 'Київ',
        placeFrom: 'Автовокзал "Центральний"',
        placeAt: 'Южный автовокзал',
        price: '1000',
        tickerCarrier: 'nikkaBus',
        url: 'https://google.com',
      },
    ];

    const requestBody = {
      /* ваш тестовый requestBody */
    };

    render(<Body ticketsData={ticketsData} requestBody={requestBody} setTicketsData={jest.fn()} />);

    // Проверяем, что компонент отрисовался
    expect(screen.getByTestId('filters')).toBeInTheDocument();
    expect(screen.getAllByTestId('ticket')).toHaveLength(ticketsData.length);
  });
});
