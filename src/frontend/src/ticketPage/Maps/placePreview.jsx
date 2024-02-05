/* eslint-disable no-plusplus */
import React from 'react';
import starIcon from './star.svg';
import phoneIcon from './phone.svg';
import addressIcon from './location.svg';
import websiteIcon from './websiteIcon.svg';
import arrowIcon from './arrowIcon.svg';

export default function PlacePreviewItem({ placesInfo, placeId, setCurrentPlaceId }) {
  const placeInfo = placesInfo.find((place) => place.place_id === placeId);
  console.log(placeInfo);
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

  const renderStars = (count) => {
    const stars = [];
    for (let i = 0; i < count; i++) {
      stars.push(<img className="place-preview-rating__img" src={starIcon} alt="star" />);
    }
    return stars;
  };

  return (
    <div className="place-preview">
      <button className="place-preview__close" type="button" onClick={() => setCurrentPlaceId(null)}>
        <img src={arrowIcon} alt="Arrow" />
        Back
      </button>
      <h2 className="place-preview__title">{name}</h2>
      {rating && (
      <div className="place-preview__rating">
        {renderStars(Math.round(rating))}
        <span className="place-preview-rating__text">{rating}</span>
        <span className="place-preview-rating__number">
          (
          {userRatingsTotal}
          ) reviews
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
          {website}
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
          <h3 className="place-preview-working-hours__title">Робочий час:</h3>
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
            <img src={photo.getUrl()} alt={name} />
          </div>
        ))}
      </div>
      )}
      <div className="place-preview__reviews">
        {reviews && reviews.map((review) => (
          <div className="place-preview-review">
            <div className="place-preview-review__row-flex">
              <img src={review.profile_photo_url} alt={review.author_name} className="place-preview-review__img" />
              <h4 className="place-preview-review-name">{review.author_name}</h4>
            </div>
            <div className="place-preview-review__row">
              {renderStars(Math.round(review.rating))}
              <span className="place-preview-rating__text">{review.rating}</span>
              <span className="place-preview-review__time">{review.relative_time_description}</span>
            </div>
            <p className="place-preview-review__text">{review.text}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
