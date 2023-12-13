import React, { useState } from 'react';

function Button({ label, isActive, onClick }) {
  return (
    <button
      type="button"
      className={`button ${isActive ? 'active' : ''}`}
      onClick={onClick}
    >
      {label}
    </button>
  );
}

function App() {
  const [activeButton, setActiveButton] = useState(null);

  const handleButtonClick = (button) => {
    setActiveButton(button);
  };

  return (
    <div>
      <Button
        label="Автобус"
        isActive={activeButton === 'автобус'}
        onClick={() => handleButtonClick('автобус')}
      />
      <Button
        label="Літак"
        isActive={activeButton === 'літак'}
        onClick={() => handleButtonClick('літак')}
      />
      <Button
        label="Потяг"
        isActive={activeButton === 'потяг'}
        onClick={() => handleButtonClick('потяг')}
      />
      <Button
        label="Пором"
        isActive={activeButton === 'пором'}
        onClick={() => handleButtonClick('пором')}
      />
    </div>
  );
}

export default App;
