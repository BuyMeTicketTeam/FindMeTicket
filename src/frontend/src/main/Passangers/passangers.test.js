import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Passengers from './index';

test('renders Passengers component with initial values', () => {
  render(
    <Passengers
      status
      adultsValue={2}
      onAdultsValue={() => {}}
      childrenValue={1}
      onChildrenValue={() => {}}
    />,
  );

  // Проверяем, что компонент отрисовывается с правильными значениями
  expect(screen.getByText(/adults/i)).toBeInTheDocument();
  expect(screen.getByText(/children/i)).toBeInTheDocument();
  expect(screen.getByDisplayValue('2')).toBeInTheDocument();
  expect(screen.getByDisplayValue('1')).toBeInTheDocument();
});

// test('increments and decrements adults value when buttons are clicked', () => {
//   const onAdultsValueMock = jest.fn();

//   render(
//     <Passengers
//       status
//       adultsValue={2}
//       onAdultsValue={onAdultsValueMock}
//       childrenValue={1}
//       onChildrenValue={() => {}}
//     />,
//   );

//   fireEvent.click(screen.getByText('+', { selector: 'button' }));
//   expect(onAdultsValueMock).toHaveBeenCalledWith(3);

//   fireEvent.click(screen.getByText('-', { selector: 'button' }));
//   expect(onAdultsValueMock).toHaveBeenCalledWith(2);
// });

// test('increments and decrements children value when buttons are clicked', () => {
//   const onChildrenValueMock = jest.fn();

//   render(
//     <Passengers
//       status
//       adultsValue={2}
//       onAdultsValue={() => {}}
//       childrenValue={1}
//       onChildrenValue={onChildrenValueMock}
//     />,
//   );

//   fireEvent.click(screen.getByText('+', { selector: 'button' }));
//   expect(onChildrenValueMock).toHaveBeenCalledWith(2);

//   fireEvent.click(screen.getByText('-', { selector: 'button.decrease' }));
//   expect(onChildrenValueMock).toHaveBeenCalledWith(1);
// });
