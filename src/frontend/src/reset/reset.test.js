/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
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
describe('server tests', () => {
  test('wrong data from user', async () => {
    function mockFetch() {
      return {
        status: 404,
      };
    }
    const fetchMock = jest.spyOn(global, 'fetch').mockImplementation(mockFetch);
    await act(async () => {
      render(
        <Router>
          <Index />
        </Router>,
      );
    });
    const buttonReg = screen.getByTestId('reset-btn');
    const resetInput = screen.getByTestId('reset-input');
    fireEvent.input(resetInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.click(buttonReg);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith('http://localhost:3000/reset', {
      body: '"stepan@gmail.com"',
      headers: {
        'Content-Type': 'application/json',
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-server');
    });
  });
});
