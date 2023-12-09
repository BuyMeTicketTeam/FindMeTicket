import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Filters from './Filters';

test('Filters component toggles sort order on button click', () => {
  const initialSort = 'price-low';

  const mockOnSort = jest.fn();

  render(<Filters onSort={mockOnSort} prevSort={initialSort} />);

  const activeButton = screen.getByText('Ціна');
  expect(activeButton).toHaveClass('active');

  fireEvent.click(activeButton);

  expect(mockOnSort).toHaveBeenCalledWith('price-up');
});
