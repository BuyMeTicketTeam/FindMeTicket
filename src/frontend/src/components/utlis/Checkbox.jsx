import React from 'react';

export default function Checkbox({ onClick }) {
  return (
    <>
      <input id="remember" type="checkbox" className="checkbox__field" onChange={onClick} />
      <label htmlFor="remember" className="checkbox">Запам&apos;ятати мене</label>
    </>
  );
}
