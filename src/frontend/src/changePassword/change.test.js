/* eslint-disable no-undef */
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
    target: { value: '12345' },
  });
  expect(codeInput.value).toBe('12345');
});

test('code input event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const passwordInput = screen.getByTestId('password-input');
  fireEvent.input(passwordInput, {
    target: { value: 'b12345678' },
  });
  expect(passwordInput.value).toBe('b12345678');
});

test('confirm-password input event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const confirmPasswordInput = screen.getByTestId('confirm-password-input');
  fireEvent.input(confirmPasswordInput, {
    target: { value: '12345' },
  });
  expect(confirmPasswordInput.value).toBe('12345');
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
  test('password error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('change-password-btn');
    const codeInput = screen.getByTestId('code-input');
    fireEvent.input(codeInput, {
      target: { value: '12345' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('password-error');
  });
  test('confirm password error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('change-password-btn');
    const codeInput = screen.getByTestId('code-input');
    const passwordInput = screen.getByTestId('password-input');
    fireEvent.input(codeInput, {
      target: { value: '12345' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('confirm-password-error');
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
      target: { value: '12345' },
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
    const buttonReg = screen.getByTestId('change-password-btn');
    const codeInput = screen.getByTestId('code-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-password-input');
    fireEvent.input(codeInput, {
      target: { value: '12345' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'b12345678' },
    });
    fireEvent.click(buttonReg);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith('http://localhost:3000/new-password', {
      body: '{"token":"12345","password":"b12345678","email":null,"confirmPassword":"b12345678"}',
      headers: {
        'Content-Type': 'application/json',
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-code');
    });
    jest.clearAllMocks();
  });
});
