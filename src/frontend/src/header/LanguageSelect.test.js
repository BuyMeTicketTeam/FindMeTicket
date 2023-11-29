/* eslint-disable no-undef */
import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import LanguageSelect from './LanguageSelect';

test('renders LanguageSelect with initial language value', () => {
  const { getByTestId } = render(<LanguageSelect language="Ua" />);
  const selectElement = getByTestId('language-select');

  expect(selectElement).toHaveValue('Ua');
});

test('calls changeLanguage prop when select value changes', () => {
  const mockChangeLanguage = jest.fn();
  const { getByTestId } = render(<LanguageSelect language="Ua" changeLanguage={mockChangeLanguage} />);
  const selectElement = getByTestId('language-select');

  fireEvent.change(selectElement, { target: { value: 'Eng' } });

  expect(mockChangeLanguage).toHaveBeenCalledWith('Eng');
});
