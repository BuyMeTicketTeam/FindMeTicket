import React from 'react';

export default function Button({ name, className, onButton }) {
  return (
    <button className={`button ${className}`} onClick={() => onButton(true)} type="button">{name}</button>
  );
}
