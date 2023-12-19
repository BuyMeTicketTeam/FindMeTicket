import React, { useState } from 'react';
import InProgress from '../../InProgress/index';
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
  const [isOpen, setIsOpen] = useState(false);

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <TransportButton
        label="Усі"
        isActive={activeButton === 'усі'}
        onClick={() => setIsOpen(true)}
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
        onClick={() => setIsOpen(true)}
        img="../img/plane.svg"
      />
      <TransportButton
        label="Потяг"
        isActive={activeButton === 'потяг'}
        onClick={() => setIsOpen(true)}
        img="../img/train.svg"
      />
      <TransportButton
        label="Пором"
        isActive={activeButton === 'пором'}
        onClick={() => setIsOpen(true)}
        img="../img/boat.svg"
      />
      {isOpen && <InProgress title="Ця функція знаходиться в розробці ):" text="Ми постійно працюємо над покращенням продукту. Як тільки ця функція буде доступна, ми вам повідомимо." setIsOpen={setIsOpen} />}
    </div>
  );
}

export default Transport;
