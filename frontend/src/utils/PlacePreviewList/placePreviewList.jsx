import React from 'react';
import { useTranslation } from 'react-i18next';
import PlacePreviewItem from '../PlacePreviewItem/placePreviewItem';
import './placePreviewList.scss';
import noImage from './no-image.jpg';

export default function PlacePreviewList({
  placesInfo, setCurrentPlaceId, updateMarker, loadMore,
}) {
  const { t } = useTranslation('translation', { keyPrefix: 'tourist-places' });
  return (
    <div className="placeList">
      {placesInfo.map((placeInfo) => (
        <PlacePreviewItem
          key={placeInfo.place_id}
          name={placeInfo.name}
          img={placeInfo.photos ? placeInfo.photos[0].getUrl() : noImage}
          openNow={placeInfo.opening_hours ? placeInfo.opening_hours.open_now : null}
          rating={placeInfo.rating}
          onClick={() => {
            setCurrentPlaceId(placeInfo.place_id);
            updateMarker(placeInfo);
          }}
        />
      ))}
      {loadMore
        && <button className="placeList__load button" type="button" onClick={() => loadMore.nextPage()}>{t('load-more')}</button>}
    </div>
  );
}
