/* eslint-disable max-len */
/* eslint-disable no-param-reassign */
import React from 'react';
import { useTranslation } from 'react-i18next';
import './passangers.scss';

export default function Passengers({
  status, adultsValue, setAdultsValue, childrenValue, onChildrenValue,
}) {
  const { t } = useTranslation('translation', { keyPrefix: 'passengers' });

  return (
    <ul data-testid="passengers-dropdown" className={status ? 'passengers__list open' : 'passengers__list close'}>
      <li className="passengers__item">
        <p className="passengers__category">{t('adults')}</p>
        <div className="passengers__form">
          <button
            className="passengers__btn"
            type="button"
            onClick={() => setAdultsValue(adultsValue + 1)}
          >
            +
          </button>
          <input
            type="number"
            className="passengers__input"
            value={adultsValue}
            onChange={(e) => setAdultsValue(e.target.value)}
          />
          <button
            className="passengers__btn"
            type="button"
            onClick={() => setAdultsValue((adultsValue - 1 > 0) ? adultsValue - 1 : adultsValue)}
          >
            -
          </button>
        </div>
      </li>
      <li className="passengers__item">
        <p className="passengers__category">{t('children')}</p>
        <div className="passengers__form">
          <button
            className="passengers__btn"
            type="button"
            onClick={() => onChildrenValue(childrenValue + 1)}
          >
            +
          </button>
          <input
            type="number"
            className="passengers__input"
            value={childrenValue}
            onChange={(e) => onChildrenValue(e.target.value)}
          />
          <button
            className="passengers__btn decrease"
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
