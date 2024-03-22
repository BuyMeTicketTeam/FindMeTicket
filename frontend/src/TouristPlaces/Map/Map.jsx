import React, { useCallback, useState, useRef } from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { useTranslation } from 'react-i18next';
import PlacePreviewList from '../../utils/PlacePreviewList/placePreviewList';
import PlacePreview from '../../utils/PlacePreview/placePreview';
import './Map.scss';

const containerStyle = {
  width: '75vw',
  height: 'calc(100vh - 121.6px)',
};

const libraries = ['places', 'marker'];
const radius = '10000';
const zoom = 11;

export default function Map({ address }) {
  const [placesInfo, setPlacesInfo] = useState([]);
  const [currentPlaceId, setCurrentPlaceId] = useState(null);
  const [nextPage, setNextPage] = useState(null);
  const { t } = useTranslation('translation', { keyPrefix: 'tourist-places' });

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAP_KEY,
    language: 'uk',
    libraries,
  });

  const mapRef = useRef(null);
  const markerRef = useRef(null);

  function updateMarker(place) {
    const svgMarker = {
      path: 'M-1.547 12l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM0 0q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z',
      fillColor: '#355982',
      fillOpacity: 1,
      strokeWeight: 0,
      rotation: 0,
      scale: 2,
      anchor: new window.google.maps.Point(0, 20),
    };
    if (markerRef.current) {
      markerRef.current.setIcon();
    }
    place.marker.setIcon(svgMarker);
    mapRef.current.panTo(place.marker.getPosition());
    if (mapRef.current.getZoom() !== 13) {
      mapRef.current.setZoom(13);
    }
    markerRef.current = place.marker;
  }

  function createMarker(place, map, icon) {
    if (!place.geometry || !place.geometry.location) return false;

    const marker = new window.google.maps.Marker({
      map,
      position: place.geometry.location,
      icon,
    });

    return marker;
  }

  function createMarkerWithClick(place, map) {
    const marker = createMarker(place, map);
    window.google.maps.event.addListener(marker, 'click', () => {
      setCurrentPlaceId(place.place_id);
      updateMarker({ marker });
    });
    return marker;
  }

  function compareOpenStatus(placeA, placeB) {
    const getOpenStatus = (place) => (place.opening_hours ? place.opening_hours.open_now : false);

    const openStatusA = getOpenStatus(placeA);
    const openStatusB = getOpenStatus(placeB);

    if (openStatusA === openStatusB) {
      return 0;
    }
    if (openStatusA) {
      return -1;
    }
    return 1;
  }

  function nearbySearch(stationLocation, map) {
    const request = {
      location: stationLocation,
      radius,
      type: 'tourist_attraction',
    };
    const service = new window.google.maps.places.PlacesService(map);
    setPlacesInfo([]);
    service.nearbySearch(request, (results, status, pagination) => {
      if (pagination.hasNextPage) {
        setNextPage(pagination);
      }
      if (status === window.google.maps.places.PlacesServiceStatus.OK && results) {
        const filterResults = results.filter((place) => place.rating);
        const resultsWithMarker = filterResults.map((result) => {
          const placeMarker = createMarkerWithClick(result, map);
          return ({ ...result, marker: placeMarker });
        });
        setPlacesInfo((prevPage) => [...prevPage, ...resultsWithMarker].sort(
          (placeA, placeB) => compareOpenStatus(placeA, placeB) || placeB.rating - placeA.rating,
        ));
      }
    });
  }

  const onLoad = useCallback((map) => {
    const geocoder = new window.google.maps.Geocoder();
    geocoder.geocode({ address }, (results, status) => {
      if (status === 'OK') {
        map.setCenter(results[0].geometry.location);
        const stationLocation = new window.google.maps.LatLng(
          results[0].geometry.location.lat(),
          results[0].geometry.location.lng(),
        );
        nearbySearch(stationLocation, map);
      } else {
        console.error(`Geocode was not successful for the following reason: ${status}`);
      }
    });

    mapRef.current = map;
  }, []);

  const onUnmount = useCallback(() => {
    mapRef.current = null;
  }, []);

  return (
    isLoaded && (
    <div className="tourist-places__map">
      <div className="places">
        <h1 className="place-list__title">
          {t('title')}
          {' '}
          {address}
        </h1>
        <PlacePreviewList
          placesInfo={placesInfo}
          setCurrentPlaceId={setCurrentPlaceId}
          updateMarker={(place) => updateMarker(place)}
          loadMore={nextPage}
        />
      </div>
      {currentPlaceId && (
      <PlacePreview
        placeId={currentPlaceId}
        placesInfo={placesInfo}
        setCurrentPlaceId={setCurrentPlaceId}
        map={mapRef.current}
      />
      )}
      <GoogleMap
        mapContainerStyle={containerStyle}
        zoom={zoom}
        onLoad={onLoad}
        onUnmount={onUnmount}
        options={{
          streetViewControl: false,
        }}
      />
    </div>
    ));
}
