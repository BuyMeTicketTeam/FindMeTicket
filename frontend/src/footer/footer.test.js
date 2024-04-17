/* eslint-disable no-undef */
import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import '@testing-library/jest-dom';

import Footer from './index';

test('Footer renders correctly and toggles detailed info', () => {
  const { getByText, queryByText, getByTestId } = render(
    <Router>
      <Footer />
    </Router>,
  );

  expect(getByText('© 2023 FindMeTicket')).toBeInTheDocument();

  expect(queryByText('Kiev-Kharkiv')).toBeNull();
  const arrowIcon = getByTestId('Toggle Footer');
  fireEvent.click(arrowIcon);
  expect(getByText('Kiev-Kharkiv')).toBeInTheDocument();
});
