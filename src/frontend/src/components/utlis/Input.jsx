import React from 'react';
// eslint-disable-next-line object-curly-newline
export default function Input({ value, onInputChange, type, dataTestId, placeholder, error }) {
  return (
    <input placeholder={placeholder} data-testid={dataTestId} type={type} className={error ? 'input input-error' : 'input'} value={value} onChange={(e) => onInputChange(e.target.value)} />
  );
}
