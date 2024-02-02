import React from 'react';
import starIcon from './star.svg';
import phoneIcon from './phone.svg';
import addressIcon from './location.svg';
import websiteIcon from './websiteIcon.svg';
import arrowIcon from './arrowIcon.svg';

export default function PlacePreviewItem({ placesInfo, placeId }) {
  const placeInfo = placesInfo.find((place) => place.place_id === placeId);
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
  } = placeInfo;
  return (
    <div className="place-preview">
      <button className="place-preview__close" type="button">
        <img src={arrowIcon} alt="Arrow" />
        Back
      </button>
      <h2 className="place-preview__title">{name}</h2>
      <div className="place-preview__rating">
        <img className="place-preview-rating__img" src={starIcon} alt="star" />
        <span className="place-preview-rating__text">{rating}</span>
        <span className="place-preview-rating__number">
          (
          {userRatingsTotal}
          ) reviews
        </span>
      </div>
      <div className="place-preview__info">
        <img src={addressIcon} alt="Address" />
        {address}
      </div>
      <div className="place-preview__info">
        <img src={websiteIcon} alt="Website" />
        {website}
      </div>
      <div className="place-preview__info">
        <img src={phoneIcon} alt="Phone" />
        {phone}
      </div>
      <div className="place-preview__working-hours">
        <h3 className="place-preview-working-hours__title">Робочий час:</h3>
        <ul>
          {openingHours.weekday_text.map((day) => (
            <li className="place-preview-working-hours__day">{day}</li>
          ))}
        </ul>
      </div>
      <div className="place-preview__photos">
        {photos.map((photo) => (
          <div className="place-preview__photo">
            <img src={photo.getUrl()} alt={name} />
          </div>
        ))}
      </div>
      <div className="place-preview__reviews">
        {reviews.map((review) => (
          <>
            <div className="place-preview-review-row">
              <img src={review.profile_photo_url} alt={review.author_name} className="place-preview-review__img" />
              <h4 className="place-preview-review-name">{review.author_name}</h4>
            </div>
            <div className="place-preview-review__row">
              <img className="place-preview-rating__img" src={starIcon} alt="star" />
              <span className="place-preview-rating__text">{review.rating}</span>
              <span className="place-preview-review__time">{review.relative_time_description}</span>
            </div>
            <p className="place-preview-review__text">{review.text}</p>
          </>
        ))}
      </div>
    </div>
  );
}
