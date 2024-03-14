/* eslint-disable no-unused-vars */
/* eslint-disable no-new */
/* eslint-disable max-len */
/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useRef, useState, useCallback } from 'react';
import './touristPlaces.scss';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { useTranslation } from 'react-i18next';
import UnauthorizedPopup from './UnauthorizedPopup';

const containerStyle = {
  width: '100vw',
  height: 'calc(100vh - 121.6px)',
};

const zoom = 10;

function TouristPlaces() {
  const [placesInfo, setPlacesInfo] = useState([]);
  const [currentPlaceId, setCurrentPlaceId] = useState(null);
  const [locked, setLocked] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'ticket-page' });

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAP_KEY,
    language: 'uk',
  });

  const mapRef = useRef(null);

  const onLoad = (map) => {
    const center = {
      lat: -3.745,
      lng: -38.523,
    };
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    mapRef.current = map;
  };

  const onUnmount = useCallback(() => {
    mapRef.current = null;
  }, []);

  return locked ? isLoaded && (
  <GoogleMap
    mapContainerStyle={containerStyle}
    zoom={zoom}
    onLoad={onLoad}
    onUnmount={onUnmount}
    options={{
      streetViewControl: false,
    }}
  />
  ) : <UnauthorizedPopup />;
}

export default TouristPlaces;
