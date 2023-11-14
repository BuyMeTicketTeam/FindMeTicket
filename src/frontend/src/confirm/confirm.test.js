/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Index from './index';

test('confirm input event', () => {
  render(<Index />);
  const confirmInput = screen.getByTestId('confirm-input');
  fireEvent.input(confirmInput, {
    target: { value: '12312' },
  });
  expect(confirmInput.value).toBe('12312');
});
