import React, { useState } from 'react';
import eyeIcon from './img/eye.svg';

export default function Input({
  value,
  onInputChange,
  type,
  dataTestId,
  placeholder,
  error,
  tip,
  tipDataTestId,
  space,
}) {
  const [show, setShow] = useState(false);

  function handleChange(e) {
    const trimmedValue = space ? e.target.value : e.target.value.trim();
    if (typeof onInputChange === 'function') {
      onInputChange(trimmedValue);
    }
  }

  let inputType = type;
  if (type === 'password') {
    inputType = show ? 'text' : 'password';
  }

  let showPasswordClassName = 'show-password';
  if (!show) {
    showPasswordClassName += ' hide';
  }

  return (
    <div className="input-wrapper">
      <input
        placeholder={placeholder}
        data-testid={dataTestId}
        type={inputType}
        className={error ? 'input input-error' : 'input'}
        value={value}
        onChange={handleChange}
      />
      {type === 'password' && (
        <button
          className={showPasswordClassName}
          type="button"
          onClick={() => setShow(!show)}
        >
          <img src={eyeIcon} alt="showPassword" />
        </button>
      )}
      <div data-testid={tipDataTestId} className="tip">
        {tip}
      </div>
    </div>
  );
}
