import React from 'react';
// eslint-disable-next-line object-curly-newline
export default function Input(
  {
    value, onInputChange, type, dataTestId, placeholder, error, show, onShow, tip, tipDataTestId,
    onClick,
  },
) {
  function ifPassword() {
    if (type === 'password') {
      return <input placeholder={placeholder} data-testid={dataTestId} type={show ? 'text' : 'password'} className={error ? 'input input-error' : 'input'} value={value} onChange={(e) => { if (typeof onInputChange === 'function') onInputChange(e.target.value); }} />;
    }
    return <input placeholder={placeholder} data-testid={dataTestId} type={type} className={error ? 'input input-error' : 'input'} value={value} onChange={(e) => { if (typeof onInputChange === 'function') onInputChange(e.target.value); }} onClick={() => onClick()} />;
  }
  return (
    <div className="input-wrapper">
      {ifPassword()}
      { type === 'password' && <button className={show ? 'show-password' : 'show-password hide'} type="button" onClick={() => { onShow(!show); }}><img src="../img/eye.svg" alt="showPassword" /></button> }
      <div data-testid={tipDataTestId} className="tip">{tip}</div>
    </div>
  );
}
