import React from 'react';
import Button from '../utils/Button';

export default function Ticket({ data }) {
  return (
    <div className="ticket">
      <div className="information">
        <div className="information__row">
          <p className="ticket__date">{data.dateFrom}</p>
          <p className="ticket__travel-time">
            <img src="../img/schedule.svg" alt="clock" />
            {data.travelTime}
          </p>
          <p className="ticket__date ticket-last-element">{data.dateArrival}</p>
        </div>
        <div className="information__row">
          <p className="ticket__city">{data.placeFrom}</p>
          <div className="ticket__line" />
          <p className="ticket__city ticket-last-element">{data.placeTo}</p>
        </div>
        <div className="information__row">
          <div className="ticket__station">{data.placeFromDetails}</div>
          <div className="ticket__station ticket-last-element">{data.placeToDetails}</div>
        </div>
      </div>
      <div className="price-options">
        <div className="price-options__row">
          <div className="ticket__price">
            <p className="price__ordinry">{`${data.priceOrdinary} грн`}</p>
            <p className="price__discond">{data.priceOld ? `${data.priceOld} грн` : null}</p>
          </div>
          <Button name="Купити" />
        </div>
        <button className="ticket__more" type="button">
          Детальніше
          <img src="../img/arrow-more.svg" alt="arrow" />
        </button>
      </div>
    </div>
  );
}
