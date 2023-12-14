import React, { useState } from 'react';
import './main.css';

function Button({
  label, isActive, onClick, img,
}) {
  return (
    <button
      type="button"
      className={`Button ${isActive ? 'active' : ''}`}
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

function App() {
  const [activeButton, setActiveButton] = useState('автобус');

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <Button
        label="Усі"
        isActive={activeButton === 'усі'}
        onClick={() => handleButtonClick('усі')}
        img="../img/boat.svg"
      />
      <Button
        label="Автобус"
        isActive={activeButton === 'автобус'}
        onClick={() => handleButtonClick('автобус')}
        img="../img/bus.svg"
      />
      <Button
        label="Літак"
        isActive={activeButton === 'літак'}
        onClick={() => handleButtonClick('літак')}
        img="../img/plane.svg"
      />
      <Button
        label="Потяг"
        isActive={activeButton === 'потяг'}
        onClick={() => handleButtonClick('потяг')}
        img="../img/train.svg"
      />
      <Button
        label="Пором"
        isActive={activeButton === 'пором'}
        onClick={() => handleButtonClick('пором')}
        img="../img/boat.svg"
      />
    </div>
  );
}

export default App;
