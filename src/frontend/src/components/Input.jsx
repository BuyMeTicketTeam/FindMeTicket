import React from 'react';
// eslint-disable-next-line object-curly-newline
export default function Input({ value, onInputChange, type, dataTestId }) {
  return (
    <input data-testid={dataTestId} type={type} className="input" value={value} onChange={(e) => onInputChange(e.target.value)} />
  );
}
