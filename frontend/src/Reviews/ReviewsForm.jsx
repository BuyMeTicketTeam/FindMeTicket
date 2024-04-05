/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-unresolved */
import React, { useEffect, useState, useRef } from 'react';
import { useSwiper } from 'swiper/react';
import { Rating } from 'react-simple-star-rating';
import noImage from './no-image.jpg';

export default function ReviewsForm({ status, reviews, setReviews }) {
  const swiper = useSwiper();
  const didMount = useRef(false);
  const [reviewText, setReviewText] = useState('');
  const [rating, setRating] = useState(0);

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
      <h2 className="reviews-form__title">Your comment</h2>
      <textarea value={reviewText} onChange={(event) => setReviewText(event.target.value)} className="reviews-form__textarea input" />
      <div className="reviews-form__actions">
        <div className="reviews-form__stars">
          <Rating
            onClick={setRating}
            initialValue={rating}
          />
        </div>
        <button type="button" className="reviews-form__submit-btn button" onClick={submitReview}>Submit</button>
      </div>
    </div>
  );
}
