/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import App from './App';

test('render header', () => {
  render(<App />);
  const headerElement = screen.getByTestId('header');
  expect(headerElement).toBeInTheDocument();
});

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
  const passwordInput = screen.getByTestId('password-input');
  fireEvent.input(passwordInput, {
    target: { value: '123456' },
  });
  expect(passwordInput.value).toBe('123456');
});

// jest.mock('fetch');
// describe('login check', () => {
//   let response;
//   beforeEach(() => {
//     response = {
//     }
//   });
//   test('success login', () => {
//     render(<App />);
//     const loginBtn = screen.getByTestId('login-btn');
//     fireEvent.click(loginBtn);
//     const passwordInput = screen.getByTestId('password-input');
//     fireEvent.input(passwordInput, {
//       target: { value: '123456' },
//     });
//     expect(passwordInput.value).toBe('123456');
//   });
// });
