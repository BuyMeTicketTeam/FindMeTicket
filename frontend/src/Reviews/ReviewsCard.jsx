/* eslint-disable react/no-array-index-key */
import React from 'react';
import starIcon from './star.svg';

export default function ReviewsCard({
  rating, text, username, useravatar,
}) {
  return (
    <div className="reviews-card">
      <div className="reviews-card__info">
        <ul className="reviews-card__stars">
          {[...Array(rating)].map((_, i) => <li key={i} className="reviews-card__star"><img src={starIcon} alt="Star" /></li>)}
        </ul>
        <p className="reviews-card__text">{text}</p>
      </div>
      <div className="reviews-card__user-data">
        <img className="reviews-card__user-avatar" src={useravatar} alt="Avatar" referrerPolicy="no-referrer" />
        <h3 className="reviews-card__user-name">{username}</h3>
      </div>
    </div>
  );
}
