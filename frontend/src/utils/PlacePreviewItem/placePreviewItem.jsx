import React from "react";
import { useTranslation } from "react-i18next";
import starIcon from "../img/star.svg";
import "./placePreviewItem.scss";

export default function PlacePreviewItem({
  name,
  img,
  openNow,
  rating,
  onClick,
}) {
  const { t } = useTranslation("translation", { keyPrefix: "ticket-page" });
  return (
    <button className="place__item" type="button" onClick={() => onClick()}>
      <div className="place__info">
        <h3 className="place__name">{name}</h3>
        <div className="place__addInfo">
          {rating && (
            <div className="place__rating">
              <img src={starIcon} alt="star" />
              {rating}
            </div>
          )}
          <p className="place__status">
            {openNow ? t("status-open") : t("status-close")}
          </p>
        </div>
      </div>
      <img src={img} alt={name} className="place__img" />
    </button>
  );
}
