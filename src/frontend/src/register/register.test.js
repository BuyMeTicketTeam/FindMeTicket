/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
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

describe('checkbox event', () => {
  test('have check', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const checkbox = screen.getByTestId('checkbox');
    const buttonReg = screen.getByTestId('register-btn');
    fireEvent.click(checkbox);
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
  test('do not have check', () => {
    render(
      <Router>
        <Index />
      </Router>,
    );
    const buttonReg = screen.getByTestId('register-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeTruthy();
  });
});
