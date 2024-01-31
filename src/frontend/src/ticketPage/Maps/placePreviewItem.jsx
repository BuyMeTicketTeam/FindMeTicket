import React from 'react';
import starIcon from './star.svg';

export default function PlacePreviewItem({
  name, img, openNow, rating,
}) {
  const imgUrl = img.getUrl();
  return (
    <div className="place__item">
      <div className="place__info">
        <h3 className="place__name">{name}</h3>
        <div className="place__addInfo">
          {rating && (
            <div className="place__rating">
              <img src={starIcon} alt="star" />
              {rating}
            </div>
          )}
          <p className="place__status">{openNow ? 'Відчинено' : 'Зачинено'}</p>
        </div>
      </div>
      <img src={imgUrl} alt={name} className="place__img" />
    </div>
  );
}
