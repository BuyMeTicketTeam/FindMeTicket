/* eslint-disable no-shadow */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-unresolved */
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Rating } from 'react-simple-star-rating';
import { useTranslation } from 'react-i18next';

import noImage from './no-image.jpg';
import lockIcon from './lock.svg';

export default function ReviewsForm({ status, setReviews }) {
  const [reviewText, setReviewText] = useState('');
  const [formError, setFormError] = useState({ inputError: false, ratingError: false });
  const [rating, setRating] = useState(0);
  const { t } = useTranslation('translation', { keyPrefix: 'reviews' });

  function validateReview() {
    let errorFlag = false;
    setFormError({ inputError: false, ratingError: false });
    if (reviewText.length < 5 || reviewText.length > 400) {
      setFormError((prevErrors) => ({ ...prevErrors, inputError: true }));
      errorFlag = true;
    }
    if (rating === 0) {
      setFormError((prevErrors) => ({ ...prevErrors, ratingError: true }));
      errorFlag = true;
    }
    return errorFlag;
  }

  function submitReview() {
    if (validateReview()) {
      return;
    }
    setReviews((prevData) => [...prevData, {
      rating,
      text: reviewText,
      username: status.username,
      useravatar: status.googlePicture || (status.basicPicture ? `data:image/jpeg;base64,${status.basicPicture}` : noImage),
    }]);
  }

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
        {(formError.inputError || formError.ratingError) && (
        <p className="error">
          <span>{formError.inputError && t('textarea_placeholder')}</span>
          <span>{formError.ratingError && t('rating__error')}</span>
        </p>
        )}
        <textarea
          disabled={!status}
          value={reviewText}
          placeholder={t('textarea_placeholder')}
          onChange={(event) => {
            setReviewText(event.target.value);
            setFormError((prevErrors) => ({ ...prevErrors, inputError: false }));
          }}
          className={`reviews-form__textarea input ${formError.inputError ? 'input-error' : ''}`}
        />
        <div className="reviews-form__actions">
          <div className={`reviews-form__stars ${formError.ratingError ? 'rating-error' : ''}`}>
            <Rating
              style={!status ? { visibility: 'hidden' } : {}}
              onClick={(rating) => {
                setRating(rating);
                setFormError((prevErrors) => ({ ...prevErrors, ratingError: false }));
              }}
              initialValue={rating}
            />
          </div>
          <button disabled={!status} type="button" className="reviews-form__submit-btn button" onClick={submitReview}>{t('form-submit-btn')}</button>
        </div>
      </div>
    </div>
  );
}
