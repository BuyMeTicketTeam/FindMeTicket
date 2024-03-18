import React from 'react';
import {
  render, screen,
} from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import LoginBtn from './LoginBtn';

jest.mock('../helper/querry', () => ({
  __esModule: true,
  default: jest.fn(() => Promise.resolve({ status: 200 })),
}));

describe('LoginBtn component', () => {
  it('renders correctly when logged in', () => {
    render(
      <BrowserRouter>
        <LoginBtn status />
      </BrowserRouter>,
    );
    expect(screen.getByText('profile')).toBeInTheDocument();
  });

  it('renders correctly when logged out', () => {
    render(
      <BrowserRouter>
        <LoginBtn status={false} />
      </BrowserRouter>,
    );
    expect(screen.getByTestId('login-btn')).toBeInTheDocument();
  });
});
