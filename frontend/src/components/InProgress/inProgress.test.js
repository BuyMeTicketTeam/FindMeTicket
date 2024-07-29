import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Index from './index';

describe('Index component', () => {
  it('renders correctly', () => {
    const title = 'Test Title';
    const text = 'Test Text';
    const setIsOpenMock = jest.fn();

    render(<Index title={title} text={text} setIsOpen={setIsOpenMock} />);

    expect(screen.getByText(title)).toBeInTheDocument();
    expect(screen.getByText(text)).toBeInTheDocument();
    expect(screen.getByText('ok')).toBeInTheDocument();
  });

  it('calls setIsOpen when the close button is clicked', () => {
    const setIsOpenMock = jest.fn();

    render(<Index title="Test Title" text="Test Text" setIsOpen={setIsOpenMock} />);

    fireEvent.click(screen.getByLabelText('Close'));

    expect(setIsOpenMock).toHaveBeenCalledWith(false);
  });

  it('calls setIsOpen when the OK button is clicked', () => {
    const setIsOpenMock = jest.fn();

    render(<Index title="Test Title" text="Test Text" setIsOpen={setIsOpenMock} />);

    fireEvent.click(screen.getByText('ok'));

    expect(setIsOpenMock).toHaveBeenCalledWith(false);
  });
});
