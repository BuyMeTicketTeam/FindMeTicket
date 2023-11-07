import React from 'react';
import Input from './Input';

// eslint-disable-next-line object-curly-newline
export default function Field({ name, value, onInputChange, type, tip }) {
  return (
    <div className="field">
      <div className="field__name">{name}</div>
      <Input value={value} onInputChange={onInputChange} type={type} />
      <div className="tip">{tip}</div>
    </div>
  );
}
