/* eslint-disable react/jsx-props-no-spreading */
import React from "react";

import "./Checkbox.scss";

export default function Checkbox({
  children,
  error,
  errorMessage,
  dataTestid,
  id,
  otherProps,
}) {
  return (
    <div className="checkbox">
      <input
        data-testid={dataTestid}
        id={id}
        type="checkbox"
        className={error ? "checkbox__input checkbox_error" : "checkbox__input"}
        {...otherProps}
      />
      <label htmlFor={id} className="checkbox__label">
        {children}
      </label>
      {error && (
        <p data-testid="error" className="checkbox__error input-error">
          {errorMessage}
        </p>
      )}
    </div>
  );
}
