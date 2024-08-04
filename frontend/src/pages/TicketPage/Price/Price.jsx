import React from "react";
import PriceBlock from "../PriceBlock/PriceBlock";
import Error from "../../../common/Error/Error";

import spinningLoaderIcon from "../../../images/spinning-loading.svg";
import busforIcon from "../../../images/busfor.png";
import proizdIcon from "../../../images/proizd.webp";
import ticketsIcon from "../../../images/tickets.jpg";

import "./Price.scss";

function Price({ ticketUrls, connection }) {
  function defineImage(resource) {
    switch (resource) {
      case "busfor":
        return busforIcon;

      case "proizdua":
        return proizdIcon;

      default:
        return ticketsIcon;
    }
  }

  return (
    <div className="ticket-price">
      {ticketUrls.length > 0 &&
        ticketUrls.map((ticketUrl) => (
          <PriceBlock
            img={defineImage(ticketUrl.event)}
            title={ticketUrl.event}
            price={ticketUrl.data.price}
            url={ticketUrl.data.url}
          />
        ))}
      {connection && (
        <img
          className="ticket-price__loading"
          src={spinningLoaderIcon}
          alt="loader"
        />
      )}
      {ticketUrls === 0 && !connection && <Error />}
    </div>
  );
}

export default Price;
