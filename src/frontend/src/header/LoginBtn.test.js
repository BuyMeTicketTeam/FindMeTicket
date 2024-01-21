import React from 'react';
import {
  render, screen, fireEvent,
} from '@testing-library/react';
import LoginBtn from './LoginBtn';

jest.mock('../helper/querry', () => ({
  __esModule: true,
  default: jest.fn(() => Promise.resolve({ status: 200 })),
}));

describe('LoginBtn component', () => {
  it('renders correctly when logged in', () => {
    render(<LoginBtn status />);
    expect(screen.getByText('profile')).toBeInTheDocument();
  });

  it('renders correctly when logged out', () => {
    render(<LoginBtn status={false} />);
    expect(screen.getByTestId('login-btn')).toBeInTheDocument();
  });

  it('calls changePopup when login button is clicked', () => {
    const changePopupMock = jest.fn();

    render(<LoginBtn status={false} changePopup={changePopupMock} />);

    fireEvent.click(screen.getByTestId('login-btn'));

    expect(changePopupMock).toHaveBeenCalledWith(true);
  });
});
