import React from 'react';
import starIcon from './star.svg';

export default function ReviewsCard({ text, username }) {
  return (
    <div className="reviews-card">
      <div className="reviews-card__info">
        <ul className="reviews-card__stars">
          <li className="reviews-card__star"><img src={starIcon} alt="Star" /></li>
          <li className="reviews-card__star"><img src={starIcon} alt="Star" /></li>
          <li className="reviews-card__star"><img src={starIcon} alt="Star" /></li>
          <li className="reviews-card__star"><img src={starIcon} alt="Star" /></li>
          <li className="reviews-card__star"><img src={starIcon} alt="Star" /></li>
        </ul>
        <p className="reviews-card__text">{text}</p>
      </div>
      <div className="reviews-card__user-data">
        <img className="reviews-card__user-avatar" src="asd" alt="Avatar" />
        <h3 className="reviews-card__user-name">{username}</h3>
      </div>
    </div>
  );
}
