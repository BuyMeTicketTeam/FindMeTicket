import React, { useState } from 'react';
import './style.css';

function TransportButton({
  label, isActive, onClick, img,
}) {
  return (
    <button
      type="button"
      className={`transport-btn ${isActive ? 'active' : ''}`}
      onClick={onClick}
    >
      <img
        className="transportion"
        src={img}
        alt={label}
      />
      {label}
    </button>
  );
}

function Transport() {
  const [activeButton, setActiveButton] = useState('автобус');

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <TransportButton
        label="Усі"
        isActive={activeButton === 'усі'}
        onClick={() => handleButtonClick('усі')}
        img="../img/boat.svg"
      />
      <TransportButton
        label="Автобус"
        isActive={activeButton === 'автобус'}
        onClick={() => handleButtonClick('автобус')}
        img="../img/bus.svg"
      />
      <TransportButton
        label="Літак"
        isActive={activeButton === 'літак'}
        onClick={() => handleButtonClick('літак')}
        img="../img/plane.svg"
      />
      <TransportButton
        label="Потяг"
        isActive={activeButton === 'потяг'}
        onClick={() => handleButtonClick('потяг')}
        img="../img/train.svg"
      />
      <TransportButton
        label="Пором"
        isActive={activeButton === 'пором'}
        onClick={() => handleButtonClick('пором')}
        img="../img/boat.svg"
      />
    </div>
  );
}

export default Transport;
