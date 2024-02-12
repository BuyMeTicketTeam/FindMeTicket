import React from 'react';
import PlacePreviewItem from './placePreviewItem';

export default function PlacePreviewList({ placesInfo, setCurrentPlaceId, updateMarker }) {
  return (
    <div className="placeList">
      {placesInfo.map((placeInfo) => (
        <PlacePreviewItem
          key={placeInfo.place_id}
          name={placeInfo.name}
          img={placeInfo.photos[0]}
          openNow={placeInfo.opening_hours.isOpen()}
          rating={placeInfo.rating}
          onClick={() => {
            // console.log(placeInfo);
            setCurrentPlaceId(placeInfo.place_id);
            updateMarker(placeInfo);
          }}
        />
      ))}
    </div>
  );
}
