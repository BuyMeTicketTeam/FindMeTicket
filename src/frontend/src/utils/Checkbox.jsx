import React from 'react';

export default function Checkbox({
  onChange, children, policyError, dataTestid,
}) {
  return (
    <>
      <input
        data-testid={dataTestid}
        id="remember"
        type="checkbox"
        className={policyError ? 'checkbox__field checkbox-error' : 'checkbox__field'}
        onChange={onChange}
      />
      <label htmlFor="remember" className="checkbox">{children}</label>
    </>
  );
}
