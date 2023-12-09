/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
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
    expect(screen.queryByTestId('error').innerHTML).toBe('error-code');
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
describe('server tests', () => {
  test('wrong code from email', async () => {
    function mockFetch() {
      return {
        ok: true,
        status: 400,
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
    const buttonReg = screen.getByTestId('confirm-btn');
    const confirmInput = screen.getByTestId('confirm-input');
    fireEvent.input(confirmInput, {
      target: { value: '12342' },
    });
    fireEvent.click(buttonReg);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith(`http://localhost:${process.env.REACT_APP_PORT}/confirm-email`, {
      body: '{"email":null,"token":"12342"}',
      headers: {
        Authorization: null,
        'Content-Type': 'application/json',
        'Refresh-Token': null,
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-code');
    });
  });
});
