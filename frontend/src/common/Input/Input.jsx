/* eslint-disable react/jsx-props-no-spreading */
import React from "react";

import eyeIcon from "../../images/eye.svg";

import "./Input.scss";

export default function Input({
  id,
  label,
  error,
  errorMessage,
  otherProps,
  showPassword,
  setShowPassword,
  type = "text",
  placeholder = "",
}) {
  return (
    <div className="field">
      {label && (
        <label htmlFor={id} className="field__name">
          {label}
        </label>
      )}
      <input
        id={id}
        className={error ? "input input_error" : "input"}
        type={showPassword ? "text" : type}
        placeholder={placeholder}
        {...otherProps}
      />
      {type === "password" && (
        <button
          className={`input__toggle-value-visibility ${
            !showPassword ? "hide" : ""
          }`}
          type="button"
          onClick={() => setShowPassword((prevState) => !prevState)}
        >
          <img src={eyeIcon} alt="showPassword" />
        </button>
      )}
      {error && (
        <p data-testid="error" className="input-error">
          {errorMessage}
        </p>
      )}
    </div>
  );
}
