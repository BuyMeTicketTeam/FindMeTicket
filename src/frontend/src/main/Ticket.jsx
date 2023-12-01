import React from 'react';
import Button from '../utils/Button';

export default function Ticket() {
  return (
    <div className="ticket">
      <div className="information">
        <div className="information__row">
          <p className="ticket__date">8:40 | 29.11, вт</p>
          <p className="ticket__travel-time">
            <img src="../img/schedule.svg" alt="clock" />
            8 год 5 хв
          </p>
          <p className="ticket__date ticket-last-element">16:30 | 29.11, вт</p>
        </div>
        <div className="information__row">
          <p className="ticket__city">Кам’янець-Подільський</p>
          <div className="ticket__line" />
          <p className="ticket__city ticket-last-element">Київ</p>
        </div>
        <div className="information__row">
          <div className="ticket__station">Автовокзал &quot;Центральний&quot;</div>
          <div className="ticket__station ticket-last-element">Южный автовокзал ...</div>
        </div>
      </div>
      <div className="price-options">
        <div className="price-options__row">
          <div className="ticket__price">
            <p className="price__ordinry">400 грн</p>
            <p className="price__discond">800 грн</p>
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
