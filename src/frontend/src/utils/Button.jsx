import React from 'react';

export default function Button({
  name, className, onButton, dataTestId, disabled = false,
}) {
  return (
    <button
      data-testid={dataTestId}
      className={`button ${className}`}
      onClick={(arg = true) => onButton(arg)}
      type="button"
      disabled={disabled}
    >
      {name}
    </button>
  );
}
