/* eslint-disable max-len */
import React, { forwardRef } from 'react';
import Input from './Input';

// eslint-disable-next-line object-curly-newline
export default forwardRef(({ name, value, onInputChange, type, tip, dataTestId, tipDataTestId, placeholder, error, show, setShow, className, onClick, space }, ref) => (
  <div ref={ref} className={`field ${className}`}>
    <div className="field__name">{name}</div>
    <Input
      dataTestId={dataTestId}
      value={value}
      onInputChange={onInputChange}
      type={type}
      placeholder={placeholder}
      error={error}
      show={show}
      onShow={setShow}
      tip={tip}
      tipDataTestId={tipDataTestId}
      onClick={onClick}
      space={space}
    />

  </div>
));
