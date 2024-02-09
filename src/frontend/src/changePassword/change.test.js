import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Index from './index';

test('code-event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const codeInput = screen.getByTestId('code-input');
  fireEvent.input(codeInput, {
  });
  expect(codeInput.value).toBe('b123456789');
});

describe('validation', () => {
  test('code error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('change-password-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('code-error');
  });

  test('success validation', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('change-password-btn');
    const codeInput = screen.getByTestId('code-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-password-input');
    fireEvent.input(codeInput, {
      target: { value: 'b123456789' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
});

describe('server tests', () => {
  test('wrong data from user', async () => {
    function mockFetch() {
      return {
        status: 403,
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
    const buttonReg = screen.getByTestId('change-password-btn');
    const codeInput = screen.getByTestId('code-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-password-input');
    fireEvent.input(codeInput, {
      target: { value: 'b123456789' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.click(buttonReg);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith(`http://localhost:${process.env.REACT_APP_PORT}/new-password`, {
      body: '{"token":"b123456789","password":"b12345678","email":null,"confirmPassword":"b12345678"}',
      credentials: 'include',
      headers: {
        Authorization: null,
        'Content-Type': 'application/json',
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-server2');
    });
    jest.clearAllMocks();
  });
});
