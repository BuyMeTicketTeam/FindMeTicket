/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Index from './index';

test('resetinput event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const resetInput = screen.getByTestId('reset-input');
  fireEvent.input(resetInput, {
    target: { value: '12312' },
  });
  expect(resetInput.value).toBe('12312');
});
describe('validation', () => {
  test('reset error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('reset-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('reset-error');
  });
  test('success validation', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('reset-btn');
    const resetInput = screen.getByTestId('reset-input');
    fireEvent.input(resetInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
});
