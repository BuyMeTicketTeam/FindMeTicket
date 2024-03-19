/* eslint-disable no-undef */
import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App';

describe('Header component', () => {
  it('renders correctly', () => {
    render(<App />);
    expect(screen.getByTestId('header')).toBeInTheDocument();
  });

  it('restores language from sessionStorage', () => {
    sessionStorage.setItem('lang', JSON.stringify({ value: 'ENG', label: 'ENG' }));
    render(<App />);
    expect(screen.getByText('ENG')).toBeInTheDocument();
  });
});
