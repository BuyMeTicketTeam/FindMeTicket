import React from 'react';
import PlacePreviewItem from './placePreviewItem';

export default function PlacePreviewList({ placesInfo, setCurrentPlaceId }) {
  return (
    <div className="placeList">
      {placesInfo.map((placeInfo) => (
        <PlacePreviewItem
          key={placeInfo.place_id}
          name={placeInfo.name}
          img={placeInfo.photos[0]}
          openNow={placeInfo.opening_hours.isOpen()}
          rating={placeInfo.rating}
          onClick={() => setCurrentPlaceId(placeInfo.place_id)}
        />
      ))}
    </div>
  );
}
