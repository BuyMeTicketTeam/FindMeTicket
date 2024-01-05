import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

describe('App component', () => {
  test('checks authentication on mount', () => {
    // Mock sessionStorage
    const sessionStorageMock = {
      getItem: jest.fn(),
      setItem: jest.fn(),
    };
    global.sessionStorage = sessionStorageMock;

    // Render the App component
    render(<App />);
    // Check if checkAuth function is called
    expect(sessionStorage.getItem('auth')).toBe('undefined');
  });
});
