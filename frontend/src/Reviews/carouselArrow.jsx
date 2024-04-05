import React from 'react';
import arrowLeftImg from './arrowLeft.svg';
import arrowRightImg from './arrowRight.svg';

export default function CarouselArrow({ direction }) {
  return (
    <div className="arrow">
      <img className={`arrow-img ${direction === 'right' ? 'swiper-button-prev' : 'swiper-button-next'}`} src={direction === 'right' ? arrowRightImg : arrowLeftImg} alt="arrow" />
    </div>
  );
}
