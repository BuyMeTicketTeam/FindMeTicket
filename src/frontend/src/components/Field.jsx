/* eslint-disable max-len */
import React from 'react';
import Input from './Input';

// eslint-disable-next-line object-curly-newline
export default function Field({ name, value, onInputChange, type, tip, dataTestId, tipDataTestId, placeholder, error }) {
  return (
    <div className="field">
      <div className="field__name">{name}</div>
      <Input dataTestId={dataTestId} value={value} onInputChange={onInputChange} type={type} placeholder={placeholder} error={error} />
      <div data-testid={tipDataTestId} className="tip">{tip}</div>
    </div>
  );
}
