/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-unresolved */
import React, { useEffect, useState, useRef } from 'react';
import { useSwiper } from 'swiper/react';
import { Link } from 'react-router-dom';
import { Rating } from 'react-simple-star-rating';
import { useTranslation } from 'react-i18next';

import noImage from './no-image.jpg';
import lockIcon from './lock.svg';

export default function ReviewsForm({ status, reviews, setReviews }) {
  const swiper = useSwiper();
  const didMount = useRef(false);
  const [reviewText, setReviewText] = useState('');
  const [rating, setRating] = useState(0);
  const { t } = useTranslation('translation', { keyPrefix: 'reviews' });

  function submitReview() {
    setReviews((prevData) => [...prevData, {
      rating,
      text: reviewText,
      username: status.username,
      useravatar: status.googlePicture || (status.basicPicture ? `data:image/jpeg;base64,${status.basicPicture}` : noImage),
    }]);
  }

  useEffect(() => {
    if (didMount.current) swiper.slideTo(reviews.length);
    else didMount.current = true;
  }, [reviews.length]);

  return (
    <div className="reviews-form">
      {!status && (
      <div className="reviews-lock">
        <img className="reviews-lock__img" src={lockIcon} alt="Lock img" />
        <h2 className="reviews-lock__title">{t('lock-message')}</h2>
        <Link
          to="/login"
          className="reviews-lock__btn button"
          state={{ successNavigate: '/reviews', closeNavigate: '/reviews' }}
        >
          {t('lock-button')}
        </Link>
      </div>
      )}
      <div className={`reviews-form__body ${!status ? 'blur' : ''}`}>
        <h2 className="reviews-form__title">{t('form-title')}</h2>
        <textarea disabled={!status} value={reviewText} onChange={(event) => setReviewText(event.target.value)} className="reviews-form__textarea input" />
        <div className="reviews-form__actions">
          <div className="reviews-form__stars">
            <Rating
              style={!status ? { visibility: 'hidden' } : {}}
              onClick={setRating}
              initialValue={rating}
            />
          </div>
          <button disabled={!status} type="button" className="reviews-form__submit-btn button" onClick={submitReview}>{t('form-submit-btn')}</button>
        </div>
      </div>
    </div>
  );
}
