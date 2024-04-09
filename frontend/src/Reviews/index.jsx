/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable import/no-unresolved */
import React, {
  useState, useRef, useCallback, useEffect,
} from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import {
  EffectCoverflow, Autoplay, Keyboard, Mousewheel,
} from 'swiper/modules';
import { v4 as uuidv4 } from 'uuid';
import { useTranslation } from 'react-i18next';

import ReviewsCard from './ReviewsCard';
import ReviewsForm from './ReviewsForm';
import CarouselArrow from './carouselArrow';

import 'swiper/scss';
import './reviews.scss';

import StepanPhoto from './image 13.png';
import MykhailoPhoto from './image15.png';
import MaximPhoto from './image14.png';
import KirillPhoto from './image16.png';

export default function Reviews({ status }) {
  const [reviews, setReviews] = useState([
    {
      rating: 5, text: "I recently had the opportunity to use Findmeticket to look for tickets, and I must say, it exceeded my expectations in several ways. As a frequent traveler, I'm always on the lookout for convenient search propositions and efficient ways to plan my journeys, and Findmeticket certainly delivered on both fronts.", username: 'Mykhailo', useravatar: MykhailoPhoto,
    },
    {
      rating: 5, text: 'Нещодавно я використав застосунок FindMeTicket, і можу сказати, що це дійсно зручний і корисний інструмент для тих, хто подорожує. Сайт пропонує широкий вибір квитків на автобуси та потяги, що дозволяє легко порівнювати ціни та вибирати найбільш вигідні пропозиції.', username: 'Степан', useravatar: StepanPhoto,
    },
    {
      rating: 5, text: 'Цей агрегатор - справжнє відкриття! Швидкий пошук, зручний інтерфейс, широкий вибір квитків. Рекомендую усім шукачам найзручніших способів подорожувати!', username: 'Максим', useravatar: MaximPhoto,
    },
    {
      rating: 5, text: 'Ефективний і зручний агрегатор квитків. Заощаджує час і гроші. Ідеальний для планування подорожей. Відтепер - мій основний інструмент для планування подорожей!', username: 'Кирило', useravatar: KirillPhoto,
    },
  ]);

  const { t } = useTranslation('translation', { keyPrefix: 'reviews' });
  const sliderRef = useRef();

  const handlePrev = useCallback(() => {
    if (!sliderRef.current) return;
    sliderRef.current.swiper.slidePrev();
  }, []);

  const handleNext = useCallback(() => {
    if (!sliderRef.current) return;
    sliderRef.current.swiper.slideNext();
  }, []);

  const didMount = useRef(false);

  useEffect(() => {
    if (didMount.current) sliderRef.current.swiper.slideTo(reviews.length);
    else didMount.current = true;
  }, [reviews.length]);

  return (
    <div className="reviews main">
      <div className="container">
        <h1 className="reviews-title">{t('title')}</h1>
        <div className="reviews-swiper">
          <CarouselArrow direction="left" onClick={handlePrev} />
          <Swiper
            ref={sliderRef}
            effect="coverflow"
            spaceBetween={50}
            slidesPerView={3}
            centeredSlides
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
          </Swiper>
          <CarouselArrow direction="right" onClick={handleNext} />
        </div>
        <ReviewsForm status={status} setReviews={setReviews} />
      </div>
    </div>
  );
}
