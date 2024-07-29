/* eslint-disable import/no-unresolved */
import { useEffect, useRef } from 'react';
import { useSwiper } from 'swiper/react';

export default function ReviewsController({ reviews }) {
  const swiper = useSwiper();
  const didMount = useRef(false);
  useEffect(() => {
    if (didMount.current) swiper.slideTo(reviews.length);
    else didMount.current = true;
  }, [reviews.length]);

  return null;
}
