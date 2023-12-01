/* eslint-disable max-len */
import React from 'react';
import Input from './Input';

// eslint-disable-next-line object-curly-newline
export default function Field({ name, value, onInputChange, type, tip, dataTestId, tipDataTestId, placeholder, error, show, onShow, className }) {
  return (
    <div className={`field ${className}`}>
      <div className="field__name">{name}</div>
      <Input dataTestId={dataTestId} value={value} onInputChange={onInputChange} type={type} placeholder={placeholder} error={error} show={show} onShow={onShow} tip={tip} tipDataTestId={tipDataTestId} />
    </div>
  );
}
