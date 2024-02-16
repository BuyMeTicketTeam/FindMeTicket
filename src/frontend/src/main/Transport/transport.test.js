import React from 'react';
import {
  render, fireEvent, waitFor, screen,
} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Transport from './index';
import '../../locales/i18n';

test('renders transport component with buttons', () => {
  const { getByText } = render(<Transport />);

  // Check if all transport buttons are rendered
  expect(getByText('Усі')).toBeInTheDocument();
  expect(getByText('Автобус')).toBeInTheDocument();
  expect(getByText('Літак')).toBeInTheDocument();
  expect(getByText('Потяг')).toBeInTheDocument();
  expect(getByText('Пором')).toBeInTheDocument();
});

test('handles button click and opens in-progress component', async () => {
  const { getByText } = render(<Transport />);

  // Click on the 'Bus' button
  fireEvent.click(getByText('Автобус'));

  // Check if the 'Bus' button is active
  expect(getByText('Автобус')).toHaveClass('active');

  fireEvent.click(getByText('Потяг'));
  await waitFor(() => {
    // Assuming InProgress component is rendered when any button is clicked
    expect(screen.getByText('Ця функція знаходиться в розробці ):')).toBeInTheDocument();
    expect(screen.getByText('Ми постійно працюємо над покращенням продукту. Як тільки ця функція буде доступна, ми вам повідомимо.')).toBeInTheDocument();
  });
});

// Add more tests as needed for specific interactions and functionalities
