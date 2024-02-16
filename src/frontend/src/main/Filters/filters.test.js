import React from 'react';
import { render, screen } from '@testing-library/react';
import Filters from './index';
import makeQuerry from '../../helper/querry'; // Adjust the path if needed

jest.mock('../../helper/querry'); // Mock the makeQuerry function

describe('Filters component', () => {
  const requestBody = {};
  const setTicketsData = jest.fn();

  beforeEach(() => {
    makeQuerry.mockResolvedValue({ status: 200, body: {} }); // Mock a successful response
  });

  it('renders the FiltersBtn components', () => {
    render(<Filters requestBody={requestBody} setTicketsData={setTicketsData} />);

    const filtersBtns = screen.getAllByRole('button', { name: /price|travelTime|departureTime|arrivalTime/i });
    expect(filtersBtns).toHaveLength(4);
  });
});
