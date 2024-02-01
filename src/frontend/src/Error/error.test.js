import React from 'react';
import { render, screen } from '@testing-library/react';
import Error from './index';

test('renders error component', () => {
  render(<Error />);

  const errorTitle = screen.getByText('title');
  const errorText = screen.getByText('text');

  expect(errorTitle).toBeInTheDocument();
  expect(errorText).toBeInTheDocument();
});
