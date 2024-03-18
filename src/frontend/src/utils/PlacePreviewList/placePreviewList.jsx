import React from 'react';
import PlacePreviewItem from '../PlacePreviewItem/placePreviewItem';
import './placePreviewList.scss';

export default function PlacePreviewList({
  placesInfo, setCurrentPlaceId, updateMarker, loadMore,
}) {
  return (
    <div className="placeList">
      {placesInfo.map((placeInfo) => (
        <PlacePreviewItem
          key={placeInfo.place_id}
          name={placeInfo.name}
          img={placeInfo.photos[0].getUrl()}
          openNow={placeInfo.opening_hours?.open_now}
          rating={placeInfo.rating}
          onClick={() => {
            setCurrentPlaceId(placeInfo.place_id);
            updateMarker(placeInfo);
          }}
        />
      ))}
      <button className="placeList__load" type="button" onClick={() => loadMore.nextPage()}>Load more</button>
    </div>
  );
}
