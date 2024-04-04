import React from 'react';
import starIcon from './star.svg';

export default function ReviewsCard() {
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
        <p className="reviews-card__text">Lorem, ipsum dolor sit amet consectetur adipisicing elit. Nulla consectetur vitae, beatae repellendus sint illum nam libero magni in omnis officiis corporis nemo unde nostrum, dolore perferendis repudiandae, deserunt quo.</p>
      </div>
      <div className="reviews-card__user-data">
        <img className="reviews-card__user-avatar" src="asd" alt="Avatar" />
        <h3 className="reviews-card__user-name">Stepan Stadniuk</h3>
      </div>
    </div>
  );
}
