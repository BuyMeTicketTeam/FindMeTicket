/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Index from './index';

test('confirm input event', () => {
  render(<Index />);
  const confirmInput = screen.getByTestId('confirm-input');
  fireEvent.input(confirmInput, {
    target: { value: '12312' },
  });
  expect(confirmInput.value).toBe('12312');
});
describe('validation', () => {
  test('confirm error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('confirm-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('confirm-error');
  });
  test('success validation', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('confirm-btn');
    const confirmInput = screen.getByTestId('confirm-input');
    fireEvent.input(confirmInput, {
      target: { value: '12342' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
});
