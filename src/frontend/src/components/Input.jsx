import React from 'react';

export default function Input({ value, onInputChange, type }) {
  return (
    <input type={type} className="input" value={value} onChange={(e) => onInputChange(e.target.value)} />
  );
}
