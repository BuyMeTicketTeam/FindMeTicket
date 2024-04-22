/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';

import eyeIcon from '../../images/eye.svg';

export default function Input({
  id, label, error, errorMessage, otherProps, showPassword, setShowPassword, type = 'text', placeholder = '',
}) {
  return (
    <div className="field">
      {label && <label htmlFor={id} className="field__name">{label}</label>}
      <input
        id={id}
        className={error ? 'input input-error' : 'input'}
        type={showPassword ? 'text' : type}
        placeholder={placeholder}
        {...otherProps}
      />
      {type === 'password' && (
        <button
          className={!showPassword ? 'show-password hide' : 'show-password'}
          type="button"
          onClick={() => setShowPassword((prevState) => !prevState)}
        >
          <img src={eyeIcon} alt="showPassword" />
        </button>
      )}
      {error && <p data-testid="error" className="confirm__error">{errorMessage}</p>}
    </div>
  );
}
