import React from 'react';

export default function DropDown() {
  return (
    <ul className="dropdown__list">
      <li className="dropDown__item">
        <p className="dropDown__city" data-testid="dropDownCity">Дніпро</p>
        <p className="dropDown__country" data-testid="dropDownCountry">Україна</p>
      </li>
      <li className="dropDown__item">
        <p className="dropDown__city" data-testid="dropDownCity">Дніпро</p>
        <p className="dropDown__country" data-testid="dropDownCountry">Україна</p>
      </li>
      <li className="dropDown__item">
        <p className="dropDown__city" data-testid="dropDownCity">Дніпро</p>
        <p className="dropDown__country" data-testid="dropDownCountry">Україна</p>
      </li>
    </ul>
  );
}
