/* eslint-disable no-undef */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Index from './index';

test('nickname input event', () => {
  render (<Index />);
  const nicknameInput = screen.getByTestId('nickname-input');
  fireEvent.input(nicknameInput, {
    target: { value: 'Stepan' },
  });
  expect(nicknameInput.value).toBe('Stepan');
});

test('email input event', () => {
  render(<Index />);
  const nicknameInput = screen.getByTestId('email-input');
  fireEvent.input(nicknameInput, {
    target: { value: 'Stepan@mail.com' },
  });
  expect(nicknameInput.value).toBe('Stepan@mail.com');
});

test('password input event', () => {
  render(<Index />);
  const nicknameInput = screen.getByTestId('password-input');
  fireEvent.input(nicknameInput, {
    target: { value: '1234' },
  });
  expect(nicknameInput.value).toBe('1234');
});

test('confirm password input event', () => {
  render(<Index />);
  const nicknameInput = screen.getByTestId('confirm-pass-input');
  fireEvent.input(nicknameInput, {
    target: { value: '1234' },
  });
  expect(nicknameInput.value).toBe('1234');
});

describe('checkbox event', () => {
  test('have check', () => {
    render(<Index />);
    const checkbox = screen.getByTestId('checkbox');
    const buttonReg = screen.getByTestId('register-btn');
    fireEvent.click(checkbox);
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeNull();
  });
  test('do not have check', () => {
    render(<Index />);
    const buttonReg = screen.getByTestId('register-btn');
    fireEvent.click(buttonReg);
    expect(screen.queryByTestId('error')).toBeTruthy();
  });
});
