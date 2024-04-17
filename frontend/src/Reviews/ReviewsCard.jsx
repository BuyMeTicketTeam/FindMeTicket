/* eslint-disable react/no-array-index-key */
import React from 'react';
import starIcon from './star.svg';
import rank1 from '../newProfile/avatarPopup/img/rank1.png';
import noImage from './no-image.jpg';

export default function ReviewsCard({
  grade, reviewText, username, profilePicture, urlPicture, writingDate,
}) {
  return (
    <div className="reviews-card">
      <div className="reviews-card__info">
        <div className="reviews-card__row">
          <ul className="reviews-card__stars">
            {[...Array(grade)].map((_, i) => <li key={i} className="reviews-card__star"><img src={starIcon} alt="Star" /></li>)}
          </ul>
          <p className="reviews-card__date">{writingDate}</p>
        </div>
        <p className="reviews-card__text">{reviewText}</p>
      </div>
      <div className="reviews-card__user-data">
        <div className="reviews-card__user-avatar">
          <img className="avatar__outline" src={rank1} alt="avatar outline" />
          <img className="avatar__img" src={urlPicture ?? (profilePicture ? `data:image/jpeg;base64,${profilePicture}` : noImage)} alt="Avatar" referrerPolicy="no-referrer" />
        </div>
        <h3 className="reviews-card__user-name">{username}</h3>
      </div>
    </div>
  );
}
