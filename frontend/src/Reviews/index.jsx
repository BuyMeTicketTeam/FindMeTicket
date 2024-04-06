/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable import/no-unresolved */
import React, { useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import {
  EffectCoverflow, Autoplay, Keyboard, Mousewheel, Navigation,
} from 'swiper/modules';
import { v4 as uuidv4 } from 'uuid';
import { useTranslation } from 'react-i18next';

import ReviewsCard from './ReviewsCard';
import ReviewsForm from './ReviewsForm';

import 'swiper/scss';
import './reviews.scss';

import StepanPhoto from './image 13.png';
import MykhailoPhoto from './image15.png';
import MaximPhoto from './image14.png';
import KirillPhoto from './image16.png';

export default function Reviews({ status }) {
  const [reviews, setReviews] = useState([
    {
      rating: 5, text: 'Some text', username: 'Михайло', useravatar: MykhailoPhoto,
    },
    {
      rating: 5, text: 'Нещодавно я використав застосунок FindMeTicket, і можу сказати, що це дійсно зручний і корисний інструмент для тих, хто подорожує. Сайт пропонує широкий вибір квитків на автобуси та потяги, що дозволяє легко порівнювати ціни та вибирати найбільш вигідні пропозиції.', username: 'Степан', useravatar: StepanPhoto,
    },
    {
      rating: 5, text: 'Some text', username: 'Максим', useravatar: MaximPhoto,
    },
    {
      rating: 5, text: 'Some text', username: 'Кирило', useravatar: KirillPhoto,
    },
  ]);

  const { t } = useTranslation('translation', { keyPrefix: 'reviews' });

  return (
    <div className="reviews main">
      <div className="container">
        <h1 className="reviews-title">{t('title')}</h1>
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
