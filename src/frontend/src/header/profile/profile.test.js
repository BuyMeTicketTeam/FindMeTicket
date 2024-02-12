import React from 'react';
import { render, fireEvent } from '@testing-library/react';
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

    const {
      getByText,
      getByAltText,
      getByTestId,
    } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    expect(getByText(/hello TestUser/i)).toBeInTheDocument();
    expect(getByAltText('Avatar')).toBeInTheDocument();
    expect(getByText(/change-password/i)).toBeInTheDocument();
    expect(getByText(/privacy.*policy/i)).toBeInTheDocument();
    expect(getByText(/delete-account/i)).toBeInTheDocument();
    expect(getByText(/exit/i)).toBeInTheDocument();
    expect(getByTestId('avatar')).toBeInTheDocument();
  });

  it('calls handleAvatarClick on clicking the avatar', () => {
    const status = {
      googlePictureUrl: 'https://example.com/avatar.jpg',
      basicPicture: 'somebase64encodedstring',
      username: 'TestUser',
    };
    const updateAuthValue = jest.fn();

    const { getByTestId } = render(
      <Router>
        <Popup status={status} updateAuthValue={updateAuthValue} />
      </Router>,
    );

    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    fileInput.setAttribute('id', 'avatarInput');
    document.body.appendChild(fileInput);

    fireEvent.click(getByTestId('avatar'));
    expect(fileInput).toHaveProperty('click');
  });
});
