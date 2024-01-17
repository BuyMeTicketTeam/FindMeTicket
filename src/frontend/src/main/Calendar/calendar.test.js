import React from 'react';
import { render, screen } from '@testing-library/react';
import Calendar from './index';
import '../../locales/i18n';

test('renders calendar component with correct title', () => {
  render(<Calendar date={new Date()} onDate={() => {}} />);

  // Check if the title is rendered correctly
  expect(screen.getByText('Дата відправки')).toBeInTheDocument();
});

// test('selects date in the calendar', async () => {
//   const onDateMock = jest.fn();
//   render(<Calendar date={new Date()} onDate={onDateMock} />);

//   // Find the input field
//   const inputField = screen.getByRole('textbox');

//   // Open the calendar
//   // Select a date in the calendar (replace with the actual date you want to select)
//   await waitFor(() => {
//     userEvent.click(inputField);
//   });
//   const selectedDate = screen.getByText('27');
//   // Click on the selected date
//   userEvent.click(selectedDate);
//   screen.debug();
//   // Check if the onDate function is called with the selected date
//   expect(screen.getByRole('textbox').value).toBe('27/12/2023');
// });
