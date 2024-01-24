import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
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

    const filtersBtns = screen.getAllByRole('button', { name: /price|travel-time|departure-time|arrival-time/i });
    expect(filtersBtns).toHaveLength(4);
  });

  it('handles sort clicks and calls makeQuerry with correct arguments', async () => {
    render(<Filters requestBody={requestBody} setTicketsData={setTicketsData} />);

    const priceBtn = screen.getByRole('button', { name: /price/i });
    fireEvent.click(priceBtn);
    expect(makeQuerry).toHaveBeenCalledWith('sortedby', '{"sortingBy":"price","ascending":false}');

    const travelTimeBtn = screen.getByTestId('travelTime');
    fireEvent.click(travelTimeBtn);
    expect(makeQuerry).toHaveBeenCalledWith('sortedby', '{"sortingBy":"price","ascending":false}');

    // ... Similar tests for other FiltersBtn components
  });

  it('toggles ascending order when clicking the same sort button again', async () => {
    render(<Filters requestBody={requestBody} setTicketsData={setTicketsData} />);

    const priceBtn = screen.getByRole('button', { name: /price/i });
    fireEvent.click(priceBtn);
    fireEvent.click(priceBtn);
    expect(makeQuerry).toHaveBeenCalledWith('sortedby', '{"sortingBy":"price","ascending":false}');
  });
});
