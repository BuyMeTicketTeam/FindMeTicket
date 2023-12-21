import React from 'react';
import { useTranslation } from 'react-i18next';
import './ticket.css';

export default function Ticket({ data }) {
  const placeFrom = data.placeFrom.length > 27 ? `${data.placeFrom.slice(0, 25)}...` : data.placeFrom;
  const placeAt = data.placeAt.length > 27 ? `${data.placeAt.slice(0, 25)}...` : data.placeAt;
  const placeFromTitle = data.placeFrom.length > 27 ? data.placeFrom : null;
  const placeAtTitle = data.placeAt.length > 27 ? data.placeAt : null;
  const { t } = useTranslation('translation', { keyPrefix: 'ticket' });
  return (
    <div className="ticket">
      <div className="ticket__body">
        <div className="information">
          <div className="information__row">
            <p className="ticket__date">{`${data.departureTime} | ${data.departureDate}`}</p>
            <p className="ticket__travel-time">
              <img src="../img/schedule.svg" alt="clock" />
              {data.travelTime}
            </p>
            <p className="ticket__date ticket-last-element">{`${data.arrivalTime} | ${data.arrivalDate}`}</p>
          </div>
          <div className="information__row">
            <p className="ticket__city">{data.departureCity}</p>
            <div className="ticket__line" />
            <p className="ticket__city ticket-last-element">{data.arrivalCity}</p>
          </div>
          <div className="information__row">
            <div className="ticket__station" title={placeFromTitle}>{placeFrom}</div>
            <div className="ticket__station ticket-last-element" title={placeAtTitle}>{placeAt}</div>
          </div>
        </div>
        <div className="price-options">
          <div className="price-options__row">
            <div className="ticket__price">
              <p className="price__ordinry">{`${data.price} ${t('uan')}`}</p>
              <p className="price__discond">{data.priceOld ? `${data.priceOld} ${t('uan')}` : null}</p>
            </div>
            <a className="ticket__buy button" href={data.url} target="blank">{t('select')}</a>
          </div>
        </div>
      </div>
    </div>
  );
}
