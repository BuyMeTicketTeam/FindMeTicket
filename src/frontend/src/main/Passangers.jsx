import React, { useState } from 'react';

export default function Passangers() {
  const [adultsValue, onAdultsValue] = useState(1);
  const [childrenValue, onChildrenValue] = useState(1);
  return (
    <ul className="passangers__list">
      <li className="passangers__item">
        <p className="passangers__category">Дорослі</p>
        <div className="passangers__form">
          <button className="passangers__btn" type="button">+</button>
          <input type="number" className="passangers__input" value={adultsValue} onChange={(e) => onAdultsValue(e.target.value)} />
          <button className="passangers__btn" type="button">-</button>
        </div>
      </li>
      <li className="passangers__item">
        <p className="passangers__category">Діти до 14 років</p>
        <div className="passangers__form">
          <button className="passangers__btn" type="button">+</button>
          <input type="number" className="passangers__input" value={childrenValue} onChange={(e) => onChildrenValue(e.target.value)} />
          <button className="passangers__btn decrease" type="button">-</button>
        </div>
      </li>
    </ul>
  );
}
