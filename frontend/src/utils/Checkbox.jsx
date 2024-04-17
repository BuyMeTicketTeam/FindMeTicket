import React from 'react';

export default function Checkbox({
  onChange, children, policyError, dataTestid, id,
}) {
  return (
    <div>
      <input
        data-testid={dataTestid}
        id={id}
        type="checkbox"
        className={policyError ? 'checkbox__field checkbox-error' : 'checkbox__field'}
        onChange={onChange}
      />
      <label htmlFor={id} className="checkbox">{children}</label>
    </div>
  );
}
