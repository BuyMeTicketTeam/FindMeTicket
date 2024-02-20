import React from 'react';
import PlacePreviewItem from './placePreviewItem';
import noImage from './no-image.jpg';

export default function PlacePreviewList({ placesInfo, setCurrentPlaceId, updateMarker }) {
  return (
    <div className="placeList">
      {placesInfo.map((placeInfo) => (
        <PlacePreviewItem
          key={placeInfo.place_id}
          name={placeInfo.name}
          img={placeInfo.photos ? placeInfo.photos[0].getUrl() : noImage}
          openNow={placeInfo?.opening_hours?.isOpen() ?? false}
          rating={placeInfo.rating}
          onClick={() => {
            setCurrentPlaceId(placeInfo.place_id);
            updateMarker(placeInfo);
          }}
        />
      ))}
    </div>
  );
}
