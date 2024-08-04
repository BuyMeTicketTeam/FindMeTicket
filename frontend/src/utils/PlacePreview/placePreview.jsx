/* eslint-disable no-plusplus */
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import starIcon from "../img/star.svg";
import phoneIcon from "./phone.svg";
import addressIcon from "./location.svg";
import websiteIcon from "./websiteIcon.svg";
import arrowIcon from "./arrowIcon.svg";
import loadingIcon from "../img/spinning-loading.svg";
import "./PlacePreview.scss";

function View({ placeData, renderStars }) {
  const { t } = useTranslation("translation", { keyPrefix: "ticketPwage" });
  const {
    name,
    website,
    international_phone_number: phone,
    formatted_address: address,
    rating,
    photos,
    reviews,
    user_ratings_total: userRatingsTotal,
    opening_hours: openingHours,
  } = placeData;

  return (
    <>
      <h2 className="place-preview__title">{name}</h2>
      {rating && (
        <div className="place-preview__rating">
          {renderStars(Math.round(rating))}
          <span className="place-preview-rating__text">{rating}</span>
          <span className="place-preview-rating__number">
            ({userRatingsTotal}) {t("reviews")}
          </span>
        </div>
      )}
      {address && (
        <div className="place-preview__info">
          <img src={addressIcon} alt="Address" />
          {address}
        </div>
      )}
      {website && (
        <div className="place-preview__info">
          <img src={websiteIcon} alt="Website" />
          <a href={website} target="blank">
            {website}
          </a>
        </div>
      )}
      {phone && (
        <div className="place-preview__info">
          <img src={phoneIcon} alt="Phone" />
          {phone}
        </div>
      )}
      {openingHours && (
        <div className="place-preview__working-hours">
          <h3 className="place-preview-working-hours__title">
            {t("workingHours")}
          </h3>
          <ul>
            {openingHours.weekday_text.map((day) => (
              <li className="place-preview-working-hours__day">{day}</li>
            ))}
          </ul>
        </div>
      )}
      {photos && (
        <div className="place-preview__photos">
          {photos.map((photo) => (
            <div className="place-preview__photo">
              <img key={photo.getUrl()} src={photo.getUrl()} alt={name} />
            </div>
          ))}
        </div>
      )}
      <div className="place-preview__reviews">
        {reviews &&
          reviews.map((review) => (
            <div key={review.time} className="place-preview-review">
              <div className="place-preview-review__row-flex">
                <img
                  src={review.profile_photo_url}
                  alt={review.author_name}
                  className="place-preview-review__img"
                  referrerPolicy="no-referrer"
                />
                <h4 className="place-preview-review-name">
                  {review.author_name}
                </h4>
              </div>
              <div className="place-preview-review__row">
                {renderStars(Math.round(review.rating))}
                <span className="place-preview-rating__text">
                  {review.rating}
                </span>
                <span className="place-preview-review__time">
                  {review.relative_time_description}
                </span>
              </div>
              <p className="place-preview-review__text">{review.text}</p>
            </div>
          ))}
      </div>
    </>
  );
}

export default function PlacePreview({ placeId, setCurrentPlaceId, map }) {
  const [placeData, setPlaceData] = useState(null);
  const { t } = useTranslation("translation", { keyPrefix: "ticket-page" });

  function getPlaceDetails() {
    const service = new window.google.maps.places.PlacesService(map);
    const request = {
      placeId,
    };
    service.getDetails(request, (result) => {
      setPlaceData(result);
    });
  }

  useEffect(() => {
    getPlaceDetails();
  }, [placeId]);

  const renderStars = (count) => {
    const stars = [];
    for (let i = 0; i < count; i++) {
      stars.push(
        <img className="place-preview-rating__img" src={starIcon} alt="star" />
      );
    }
    return stars;
  };

  return (
    <div className="place-preview">
      <button
        className="place-preview__close"
        type="button"
        onClick={() => setCurrentPlaceId(null)}
      >
        <img src={arrowIcon} alt="Arrow" />
        {t("backBtn")}
      </button>
      {placeData ? (
        <View
          placeData={placeData}
          renderStars={(rating) => renderStars(rating)}
        />
      ) : (
        <img
          className="place-preview__loading-img"
          src={loadingIcon}
          alt="Loading..."
        />
      )}
    </div>
  );
}
