/* eslint-disable no-undef */
import React from 'react';
import { render } from '@testing-library/react';
import Information from './index ';

test('Information component renders correctly', () => {
  const { getByText, queryByTestId } = render(<Information />);

  expect(getByText('8:40')).toBeInTheDocument();
  expect(getByText('8 год 5 хв')).toBeInTheDocument();
  expect(getByText('16:45')).toBeInTheDocument();

  expect(getByText('Київ')).toBeInTheDocument();
  expect(getByText('Автовокзал Центральний')).toBeInTheDocument();
  expect(getByText('Кам’янець-Подільський')).toBeInTheDocument();
  expect(getByText('Южный автовокзал')).toBeInTheDocument();

  const locationTextSmall = queryByTestId('location-text-small');
  expect(locationTextSmall).toBeNull();
});
