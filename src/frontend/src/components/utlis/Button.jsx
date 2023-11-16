import React from 'react';

export default function Button({
  name, className, onButton, dataTestId,
}) {
  return (
    <button data-testid={dataTestId} className={`button ${className}`} onClick={() => onButton(true)} type="button">{name}</button>
  );
}
