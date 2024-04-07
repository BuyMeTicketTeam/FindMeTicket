/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';

export default function Checkbox({
  children, error, errorMessage, dataTestid, id, otherProps,
}) {
  return (
    <div>
      <input
        data-testid={dataTestid}
        id={id}
        type="checkbox"
        className={error ? 'checkbox__field checkbox-error' : 'checkbox__field'}
        {...otherProps}
      />
      <label htmlFor={id} className="checkbox">{children}</label>
      {error && <p data-testid="error" className="confirm__error">{errorMessage}</p>}
    </div>
  );
}
