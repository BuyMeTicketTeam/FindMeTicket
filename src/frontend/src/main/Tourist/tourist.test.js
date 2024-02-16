import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Banner from './index';
import '../../locales/i18n';

test('renders banner component with correct text and button', () => {
  render(<Banner city="TestCity" />);

  // Check if the banner text is rendered correctly
  const bannerText = screen.getByText('У нас є кілька цікавих місць, які можете відвідати у місті TestCity');
  expect(bannerText).toBeInTheDocument();

  // Check if the button is rendered correctly
  const bannerButton = screen.getByText('Детальніше');
  expect(bannerButton).toBeInTheDocument();
});
