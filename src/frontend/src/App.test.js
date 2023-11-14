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

test('link to register', () => {
  render(<App />);
  const loginBtn = screen.getByTestId('login-btn');
  fireEvent.click(loginBtn);
  const toRegisterBtn = screen.getByTestId('to-register-btn');
  fireEvent.click(toRegisterBtn);
  expect(screen.queryByTestId('register')).toBeInTheDocument();
});

// describe('login check', () => {
//   // let container = null;
//   // beforeEach(() => {
//   // // подготавливаем DOM-элемент, куда будем рендерить
//   //   container = document.createElement('div');
//   //   document.body.appendChild(container);
//   // });

//   // afterEach(() => {
//   // // подчищаем после завершения
//   //   unmountComponentAtNode(container);
//   //   container.remove();
//   //   container = null;
//   // });

//   it('renders user data', async () => {
//     // const fakeUser = {
//     //   name: 'Joni Baez',
//     //   age: '32',
//     //   address: '123, Charming Avenue',
//     // };
//     const status = 400;
//     jest.spyOn(global, 'fetch').mockImplementation(() => Promise.resolve({
//       // json: () => Promise.resolve(fakeUser),
//       status: () => Promise.resolve(status),
//     }));

//     // Используем act асинхронно, чтобы передать успешно завершённые промисы
//     await act(async () => {
//       render(<Login />);
//     });
//     const sendBtn = screen.getByTestId('send-request');
//     fireEvent.click(sendBtn);
//     expect(screen.queryByTestId('error')).toBeInTheDocument();

//     // выключаем фиктивный fetch, чтобы убедиться, что тесты полностью изолированы
//     global.fetch.mockRestore();
//   });
// });
