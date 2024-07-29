import React from 'react';
import arrowLeftImg from './arrowLeft.svg';
import arrowRightImg from './arrowRight.svg';

export default function CarouselArrow({ refProp, direction, onClick }) {
  return (
    <button ref={refProp} type="button" className="arrow" onClick={onClick}>
      <img className="arrow-img" src={direction === 'right' ? arrowRightImg : arrowLeftImg} alt="arrow" />
    </button>
  );
}
