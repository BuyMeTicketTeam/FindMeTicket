import React, { useEffect, useState } from 'react';
import './style.scss';
import PriceBlock from './PriceBlock';
import proizdIcon from './proizd.webp';
import spinningLoaderIcon from './spinning-loading.svg';
import skybookingIcon from './skybooking.png';

function PriceTrain({ ticketUrls, connection }) {
  const [proizdTickets, setProizdTickets] = useState([]);
  const [skybookingTickets, setSkybookingTickets] = useState([]);
  const [selctedResource, setSelectedResource] = useState(0);

  function groupTickets(resource, ticketUrl) {
    if (resource === 'skybooking') {
      setSkybookingTickets((prevTicket) => [...prevTicket, ticketUrl]);
    } else {
      setProizdTickets((prevTicket) => [...prevTicket, ticketUrl]);
    }
  }

  useEffect(() => {
    if (ticketUrls.length > 0) {
      ticketUrls.map((ticketUrl) => groupTickets(ticketUrl.resource, ticketUrl));
    }
  }, [ticketUrls]);

  return (
    <div className="train-price ticket-price">
      <div className="ticket-header">
        {proizdTickets.length > 0 && (
        <button className={`ticket-header__btn ${selctedResource === 0 ? 'active' : ''}`} type="button" onClick={() => setSelectedResource(0)}>
          <img src={proizdIcon} alt="proizdIcon" className="proizd-icon" />
          Proizd.ua
        </button>
        )}
        {skybookingTickets.length > 0 && (
        <button className={`ticket-header__btn ${selctedResource === 1 ? 'active' : ''} skybooking`} type="button" onClick={() => setSelectedResource(1)}>
          <img src={skybookingIcon} alt="proizdIcon" className="proizd-icon" />
          Skybooking.ua
        </button>
        )}
      </div>
      <div className="ticket-price__body">
        {selctedResource === 0 && proizdTickets.length > 0 && proizdTickets.map(
          (tickets) => <PriceBlock ticketUrl={tickets} />,
        )}
        {selctedResource === 1 && proizdTickets.length > 0 && skybookingTickets.map(
          (tickets) => <PriceBlock ticketUrl={tickets} />,
        )}
      </div>
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls.length === 0 && !connection) && <p>Error...</p>}
    </div>
  );
}

export default PriceTrain;
