/* eslint-disable max-len */
/* eslint-disable no-param-reassign */
import React from 'react';

export default function Passangers({
  status, adultsValue, onAdultsValue, childrenValue, onChildrenValue,
}) {
  return (
    <ul className={status ? 'passangers__list open' : 'passangers__list close'}>
      <li className="passangers__item">
        <p className="passangers__category">Дорослі</p>
        <div className="passangers__form">
          <button
            className="passangers__btn"
            type="button"
            onClick={() => onAdultsValue(adultsValue + 1)}
          >
            +
          </button>
          <input
            type="number"
            className="passangers__input"
            value={adultsValue}
            onChange={(e) => onAdultsValue(e.target.value)}
          />
          <button
            className="passangers__btn"
            type="button"
            onClick={() => onAdultsValue((adultsValue - 1 > 0) ? adultsValue - 1 : adultsValue)}
          >
            -
          </button>
        </div>
      </li>
      <li className="passangers__item">
        <p className="passangers__category">Діти до 14 років</p>
        <div className="passangers__form">
          <button
            className="passangers__btn"
            type="button"
            onClick={() => onChildrenValue(childrenValue + 1)}
          >
            +
          </button>
          <input
            type="number"
            className="passangers__input"
            value={childrenValue}
            onChange={(e) => onChildrenValue(e.target.value)}
          />
          <button
            className="passangers__btn decrease"
            type="button"
            onClick={() => onChildrenValue((childrenValue - 1 >= 0) ? childrenValue - 1 : childrenValue)}
          >
            -
          </button>
        </div>
      </li>
    </ul>
  );
}
