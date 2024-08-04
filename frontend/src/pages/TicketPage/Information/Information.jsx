import React from "react";
import "./Information.scss";

function Information({ ticketData }) {
  return (
    <div className="information-block">
      <div className="time">
        <div className="time-text">{ticketData.departureTime}</div>
        <div className="time-text small">{ticketData.travelTime}</div>
        <div className="time-text">{ticketData.arrivalTime}</div>
      </div>
      <div className="vertical-line" />
      <div className="location">
        <div className="city-above">
          <div className="location-text">{ticketData.departureCity}</div>
          <div className="location-text small">{ticketData.placeFrom}</div>
        </div>
        <div className="location-text">{ticketData.arrivalCity}</div>
        <div className="location-text small ">{ticketData.placeAt}</div>
      </div>
    </div>
  );
}

export default Information;
