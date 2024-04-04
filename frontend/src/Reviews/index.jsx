/* eslint-disable import/no-unresolved */
import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import {
  EffectCoverflow, Autoplay, Keyboard, Mousewheel,
} from 'swiper/modules';
import ReviewsCard from './ReviewsCard';

import 'swiper/scss';
import './reviews.scss';

export default function Reviews() {
  const reviews = [
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
  ];

  return (
    <div className="reviews main">
      <div className="container">
        <h1 className="reviews-title">Many amazing people are using our service daily</h1>
        <div className="reviews-swiper">
          <Swiper
            effect="coverflow"
            spaceBetween={50}
            slidesPerView={3}
            modules={[EffectCoverflow, Autoplay, Keyboard, Mousewheel]}
            coverflowEffect={{
              rotate: 0,
              depth: 150,
              stretch: 0,
              modifier: 1,
              slideShadows: false,
            }}
            autoplay={{
              delay: 3000,
            }}
            loop
            speed={800}
            keyboard={{
              enabled: true,
            }}
            mousewheel={{
              enabled: true,
            }}
          >
            {reviews.map((slide, index) => (
              <SwiperSlide key={slide} virtualIndex={index}>
                <ReviewsCard />
              </SwiperSlide>
            ))}
          </Swiper>
        </div>
      </div>
    </div>
  );
}
