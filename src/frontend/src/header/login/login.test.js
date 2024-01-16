/* eslint-disable no-undef */
import React from 'react';
import {
  render, screen, fireEvent, act, waitFor,
} from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';
import Login from './index';

test('render login', () => {
  render(
    <Router>
      <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
        <Login />
      </GoogleOAuthProvider>
    </Router>,
  );
  expect(screen.queryByTestId('login')).toBeInTheDocument();
});

test('login input event', () => {
  render(
    <Router>
      <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
        <Login />
      </GoogleOAuthProvider>
    </Router>,
  );
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const loginInput = screen.getByTestId('login-input');
  fireEvent.input(loginInput, {
    target: { value: 'Stepan' },
  });
  expect(loginInput.value).toBe('Stepan');
});

test('password input event', () => {
  render(
    <Router>
      <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
        <Login />
      </GoogleOAuthProvider>
    </Router>,
  );
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const passwordInput = screen.getByTestId('password-login-input');
  fireEvent.input(passwordInput, {
    target: { value: '123456' },
  });
  expect(passwordInput.value).toBe('123456');
});

test('link to register', () => {
  render(
    <Router>
      <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
        <Login />
      </GoogleOAuthProvider>
    </Router>,
  );
  expect(screen.queryByTestId('to-register-btn')).toBeInTheDocument();
});

describe('validation', () => {
  test('login error', () => {
    render(
      <Router>
        <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
          <Login />
        </GoogleOAuthProvider>
      </Router>,
    );
    const buttonLogin = screen.getByTestId('login-btn');
    fireEvent.click(buttonLogin);
    expect(screen.queryByTestId('error').innerHTML).toBe('login-error');
  });
  test('password error', () => {
    render(
      <Router>
        <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
          <Login />
        </GoogleOAuthProvider>
      </Router>,
    );
    const buttonLogin = screen.getByTestId('login-btn');
    const loginInput = screen.getByTestId('login-input');
    fireEvent.input(loginInput, {
      target: { value: 'stepan@gmail.com' },
    });
    fireEvent.click(buttonLogin);
    expect(screen.queryByTestId('error').innerHTML).toBe('password-error');
  });
  test('success validation', () => {
    render(
      <Router>
        <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
          <Login />
        </GoogleOAuthProvider>
      </Router>,
    );
    const buttonLogin = screen.getByTestId('login-btn');
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
      render(
        <Router>
          <GoogleOAuthProvider clientId="827464600699-8u8q3ota4v062r6j6b96l682n2sfapqq.apps.googleusercontent.com">
            <Login />
          </GoogleOAuthProvider>
        </Router>,
      );
    });
    const buttonLogin = screen.getByTestId('login-btn');
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
    await waitFor(() => {
      expect(screen.getByTestId('error').innerHTML).toBe('error-server2');
    });
  });
});
