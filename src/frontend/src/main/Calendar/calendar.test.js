import React from 'react';
import { render, screen } from '@testing-library/react';
import Calendar from './index';
import '../../locales/i18n';

test('renders calendar component with correct title', () => {
  render(<Calendar date={new Date()} onDate={() => {}} />);

  // Check if the title is rendered correctly
  expect(screen.getByText('Дата відправки')).toBeInTheDocument();
});
