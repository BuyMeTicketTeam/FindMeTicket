import React from 'react';
import './style.css';

function Information({ ticketData }) {
  return (
    <div className="information-block">
      <div className="time">
        <div className="time-text">{ticketData.timeFrom}</div>
        <div className="time-text small">{ticketData.time}</div>
        <div className="time-text-p">{ticketData.timeTo}</div>
      </div>
      <div className="vertical-line" />
      <div className="location">
        <div className="city-above">
          <div className="location-text">{ticketData.cityFrom}</div>
          <div className="location-text small">{ticketData.cityFromDetails}</div>
        </div>
        <div className="location-text">{ticketData.cityTo}</div>
        <div className="location-text small ">{ticketData.cityToDetails}</div>
      </div>
    </div>
  );
}

export default Information;
