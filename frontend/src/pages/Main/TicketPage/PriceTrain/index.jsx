import React, { useEffect, useState } from 'react';
import './style.scss';
import PriceBlock from './PriceBlock';
import proizdIcon from './proizd.webp';
import spinningLoaderIcon from './spinning-loading.svg';
import ticketsuaIcon from './ticketsua.jpg';

function PriceTrain({ ticketUrls, connection }) {
  const [proizdTickets, setProizdTickets] = useState([]);
  const [ticketsuaTickets, setTicketsuaTickets] = useState([]);
  const [selctedResource, setSelectedResource] = useState(0);

  function groupTickets(resource, ticketUrl) {
    if (resource === 'ticketsua') {
      setTicketsuaTickets((prevTicket) => [...prevTicket, ticketUrl]);
    } else {
      setProizdTickets((prevTicket) => [...prevTicket, ticketUrl]);
    }
  }

  useEffect(() => {
    setTicketsuaTickets([]);
    setProizdTickets([]);
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
        {ticketsuaTickets.length > 0 && (
        <button className={`ticket-header__btn ${selctedResource === 1 ? 'active' : ''}`} type="button" onClick={() => setSelectedResource(1)}>
          <img src={ticketsuaIcon} alt="proizdIcon" className="proizd-icon" />
          Tickets.ua
        </button>
        )}
      </div>
      <div className="ticket-price__body">
        {selctedResource === 0 && proizdTickets.length > 0 && proizdTickets.map(
          (tickets) => <PriceBlock ticketUrl={tickets} />,
        )}
        {selctedResource === 1 && proizdTickets.length > 0 && ticketsuaTickets.map(
          (tickets) => <PriceBlock ticketUrl={tickets} />,
        )}
      </div>
      {connection && <img className="ticket-price__loading" src={spinningLoaderIcon} alt="loader" />}
      {(ticketUrls.length === 0 && !connection) && <p>Error...</p>}
    </div>
  );
}

export default PriceTrain;
