/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable import/no-unresolved */
import React, { useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import {
  EffectCoverflow, Autoplay, Keyboard, Mousewheel, Navigation,
} from 'swiper/modules';
import { v4 as uuidv4 } from 'uuid';

import ReviewsCard from './ReviewsCard';

import 'swiper/scss';
import './reviews.scss';
import ReviewsForm from './ReviewsForm';

export default function Reviews({ status }) {
  const [reviews, setReviews] = useState([
    {
      rating: 5, text: 'Some text', username: 'Stepan', useravatar: 'qwerty',
    },
    {
      rating: 4, text: 'Some text', username: 'Misha', useravatar: 'qwerty',
    },
    {
      rating: 5, text: 'Some text', username: 'Somebody', useravatar: 'qwerty',
    },
    {
      rating: 5, text: 'Some text', username: 'Stepan', useravatar: 'qwerty',
    },
    {
      rating: 4, text: 'Some text', username: 'Misha', useravatar: 'qwerty',
    },
    {
      rating: 5, text: 'Some text', username: 'Somebody', useravatar: 'qwerty',
    },
  ]);

  return (
    <div className="reviews main">
      <div className="container">
        <h1 className="reviews-title">Many amazing people are using our service daily</h1>
        <div className="reviews-swiper">
          <Swiper
            effect="coverflow"
            spaceBetween={50}
            slidesPerView={3}
            centeredSlides
            modules={[EffectCoverflow, Autoplay, Keyboard, Mousewheel, Navigation]}
            coverflowEffect={{
              rotate: 0,
              depth: 150,
              stretch: 0,
              modifier: 1,
              slideShadows: false,
            }}
            autoplay={{
              delay: 3000,
              stopOnLastSlide: true,
            }}
            speed={800}
            keyboard={{
              enabled: true,
            }}
            mousewheel={{
              enabled: true,
            }}
          >
            {reviews.map((slide, index) => (
              <SwiperSlide key={uuidv4()} virtualIndex={index}>
                <ReviewsCard {...slide} />
              </SwiperSlide>
            ))}
            <ReviewsForm status={status} reviews={reviews} setReviews={setReviews} />
          </Swiper>
        </div>
      </div>
    </div>
  );
}
