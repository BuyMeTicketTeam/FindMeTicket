import React from "react";
import { useTranslation } from "react-i18next";
// import './style.scss';

export default function PriceBlock({ title, price, url, img }) {
  const { t } = useTranslation("translation", { keyPrefix: "ticketPage" });
  return (
    <div className="price-block" data-testid="price-block">
      {img && <img src={img} alt={title} />}
      <span>{title}:</span>
      <div className="price-container" data-testid="price-container">
        <div className="price">
          {Number(price).toFixed(2)} {t("uan")}
        </div>
      </div>
      <a
        href={url}
        className="button buy-button"
        target="_blank"
        rel="noreferrer"
      >
        {t("buy")}
      </a>
    </div>
  );
}
