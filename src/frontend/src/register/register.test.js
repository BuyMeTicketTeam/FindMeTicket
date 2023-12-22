/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Index from './index';

test('nickname-v2-event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const nicknameInput = screen.getByTestId('nickname-input');
  fireEvent.input(nicknameInput, {
    target: { value: 'Stepan' },
  });
  expect(nicknameInput.value).toBe('Stepan');
});

test('email input event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const nicknameInput = screen.getByTestId('email-input');
  fireEvent.input(nicknameInput, {
    target: { value: 'Stepan@mail.com' },
  });
  expect(nicknameInput.value).toBe('Stepan@mail.com');
});

test('password input event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const nicknameInput = screen.getByTestId('password-input');
  fireEvent.input(nicknameInput, {
    target: { value: '1234' },
  });
  expect(nicknameInput.value).toBe('1234');
});

test('confirm password input event', () => {
  render(
    <Router>
      <Index />
    </Router>,
  );
  const nicknameInput = screen.getByTestId('confirm-pass-input');
  fireEvent.input(nicknameInput, {
    target: { value: '1234' },
  });
  expect(nicknameInput.value).toBe('1234');
});

describe('validation', () => {
  test('nickname error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('nickname-error');
  });

  test('mail error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('email-error');
  });

  test('password error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    const emailInput = screen.getByTestId('email-input');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.input(emailInput, {
      target: { value: 'stepan@gmail.com' },
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
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.input(emailInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('confirm-password-error');
  });

  test('checkbox error', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-pass-input');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.input(emailInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error').innerHTML).toBe('privacy-policy');
  });

  test('success validation', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-pass-input');
    const checkbox = screen.getByTestId('checkbox');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.input(emailInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.click(checkbox);
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
});
describe('server tests', () => {
  test('login or password error', async () => {
    function mockFetch() {
      return {
        status: 409,
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
    const buttonReg = screen.getByTestId('register-btn');
    const nicknameInput = screen.getByTestId('nickname-input');
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const confirmPasswordInput = screen.getByTestId('confirm-pass-input');
    const checkbox = screen.getByTestId('checkbox');
    fireEvent.input(nicknameInput, {
      target: { value: 'Stepan' },
    });
    fireEvent.input(emailInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.input(confirmPasswordInput, {
      target: { value: 'asdadfdaf213123123' },
    });
    fireEvent.click(checkbox);
    fireEvent.click(buttonReg);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith(`http://localhost:${process.env.REACT_APP_PORT}/register`, {
      body: '{"email":"stepan@gmail.com","password":"asdadfdaf213123123","username":"Stepan","confirmPassword":"asdadfdaf213123123"}',
      credentials: 'include',
      headers: {
        Authorization: null,
        'Content-Type': 'application/json',
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-email');
    });
  });
});
