import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { BrowserRouter as Router } from 'react-router-dom';
import Popup from './index';

describe('Popup component', () => {
  it('renders correctly', () => {
    const status = {
      googlePictureUrl: 'https://example.com/avatar.jpg',
      basicPicture: 'somebase64encodedstring',
      username: 'TestUser',
    };
    const updateAuthValue = jest.fn();

    const { getByText, getByAltText, getByTestId } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    expect(getByText(/hello TestUser/i)).toBeInTheDocument();
    expect(getByAltText('Avatar')).toBeInTheDocument();
    expect(getByText(/change password/i)).toBeInTheDocument();
    expect(getByText(/privacy policy/i)).toBeInTheDocument();
    expect(getByText(/delete account/i)).toBeInTheDocument();
    expect(getByText(/exit/i)).toBeInTheDocument();
    expect(getByTestId('avatar-input')).toBeInTheDocument();
  });

  it('calls handleAvatarClick on clicking the avatar', () => {
    const status = {
      googlePictureUrl: 'https://example.com/avatar.jpg',
      basicPicture: 'somebase64encodedstring',
      username: 'TestUser',
    };
    const updateAuthValue = jest.fn();
    const handleAvatarClick = jest.fn();

    const { getByTestId } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    fireEvent.click(getByTestId('avatar'));
    expect(handleAvatarClick).toHaveBeenCalled();
  });

  it('calls handleLogoutButton on clicking exit button', () => {
    const status = {
      googlePictureUrl: 'https://example.com/avatar.jpg',
      basicPicture: 'somebase64encodedstring',
      username: 'TestUser',
    };
    const updateAuthValue = jest.fn();
    const handleLogoutButton = jest.fn();

    const { getByText } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    fireEvent.click(getByText(/exit/i));
    expect(handleLogoutButton).toHaveBeenCalled();
  });

  it('calls handleDeleteAccount and shows delete confirmation popup on clicking delete account button', async () => {
    const status = {
      googlePictureUrl: 'https://example.com/avatar.jpg',
      basicPicture: 'somebase64encodedstring',
      username: 'TestUser',
    };
    const updateAuthValue = jest.fn();
    const handleDeleteAccount = jest.fn();

    const { getByText, getByTestId, queryByTestId } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    fireEvent.click(getByText(/delete account/i));
    expect(handleDeleteAccount).toHaveBeenCalled();
    expect(getByTestId('delete-confirmation-popup')).toBeInTheDocument();

    fireEvent.click(getByText(/cancel/i));
    await waitFor(() => {
      expect(queryByTestId('delete-confirmation-popup')).not.toBeInTheDocument();
    });
  });
});
