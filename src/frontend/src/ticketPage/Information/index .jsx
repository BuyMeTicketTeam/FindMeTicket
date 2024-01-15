import React from 'react';
import './style.css';

function Information() {
  return (
    <div className="information-block">
      <div className="time">
        <div className="time-text">8:40</div>
        <div className="time-text small">8 год 5 хв</div>
        <div className="time-text-p">16:45</div>
      </div>
      <div className="vertical-line" />
      <div className="location">
        <div className="city-above">
          <div className="location-text">Київ</div>
          <div className="location-text small">Автовокзал Центральний</div>
        </div>
        <div className="location-text">Кам’янець-Подільський</div>
        <div className="location-text small ">Южный автовокзал</div>
      </div>
    </div>
  );
}

export default Information;
