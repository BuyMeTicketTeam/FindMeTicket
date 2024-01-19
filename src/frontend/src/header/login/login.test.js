/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import App from '../../App';
import Login from './index';

test('open login', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  expect(screen.queryByTestId('login')).toBeNull();
  fireEvent.click(loginBtn);
  expect(screen.queryByTestId('login')).toBeInTheDocument();
});

test('close login', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const closeBtn = screen.getByTestId('close');
  expect(screen.queryByTestId('login')).toBeInTheDocument();
  fireEvent.click(closeBtn);
  expect(screen.queryByTestId('login')).toBeNull();
});

test('login input event', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const loginInput = screen.getByTestId('login-input');
  fireEvent.input(loginInput, {
    target: { value: 'Stepan' },
  });
  expect(loginInput.value).toBe('Stepan');
});

test('password input event', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const passwordInput = screen.getByTestId('password-login-input');
  fireEvent.input(passwordInput, {
    target: { value: '123456' },
  });
  expect(passwordInput.value).toBe('123456');
});

test('link to register', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const toRegisterBtn = screen.getByTestId('to-register-btn');
  fireEvent.click(toRegisterBtn);
  expect(screen.queryByTestId('register')).toBeInTheDocument();
});

describe('validation', () => {
  test('login error', () => {
    render(
      <Router>
        <Login />
      </Router>,
    );
    const buttonLogin = screen.getByTestId('send-request');
    fireEvent.click(buttonLogin);
    expect(screen.queryByTestId('error').innerHTML).toBe('The email field is not filled in correctly');
  });
  test('password error', () => {
    render(
      <Router>
        <Login />
      </Router>,
    );
    const buttonLogin = screen.getByTestId('send-request');
    const loginInput = screen.getByTestId('login-input');
    fireEvent.input(loginInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.click(buttonLogin);
    expect(screen.queryByTestId('error').innerHTML).toBe('The password field is incorrect');
  });
  test('success validation', () => {
    render(
      <Router>
        <Login />
      </Router>,
    );
    const buttonLogin = screen.getByTestId('send-request');
    const loginInput = screen.getByTestId('login-input');
    const passwordInput = screen.getByTestId('password-login-input');
    fireEvent.input(loginInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'stepan123132123' },
    });
    fireEvent.click(buttonLogin);
    expect(screen.queryByTestId('error')).toBeNull();
  });
});

describe('server tests', () => {
  test('login or password error', async () => {
    function mockFetch() {
      return {
        status: 400,
      };
    }
    const fetchMock = jest.spyOn(global, 'fetch').mockImplementation(mockFetch);
    await act(async () => {
      render(<App />);
    });
    const loginBtn = screen.getByTestId('login-btn');
    fireEvent.click(loginBtn);
    const buttonLogin = screen.getByTestId('send-request');
    const loginInput = screen.getByTestId('login-input');
    const passwordInput = screen.getByTestId('password-login-input');
    fireEvent.input(loginInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.input(passwordInput, {
      target: { value: 'stepan123132123' },
    });
    fireEvent.click(buttonLogin);
    expect(fetchMock).toHaveBeenCalledTimes(1);
    expect(fetchMock).toHaveBeenCalledWith(`http://localhost:${process.env.REACT_APP_PORT}/login`, {
      body: '{"email":"stepan@gmail.com","password":"stepan123132123"}',
      credentials: 'include',
      headers: {
        Authorization: null,
        'Content-Type': 'application/json',
      },
      method: 'POST',
    });
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('Server error. Try again');
    });
  });
});
