/* eslint-disable max-len */
/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useRef, useState, useCallback } from 'react';
import './style.scss';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { useTranslation } from 'react-i18next';
import PlacePreviewList from './placePreviewList';
import PlacePreview from './placePreview';

function Maps({ address }) {
  const [selectedCategory, setSelectedCategory] = useState(0);
  const [placesInfo, setPlacesInfo] = useState([]);
  const [currentPlaceId, setCurrentPlaceId] = useState(null);
  const { t } = useTranslation('translation', { keyPrefix: 'ticket-page' });

  const containerStyle = {
    width: '100%',
    height: '400px',
  };

  const libraries = ['places', 'marker'];
  const radius = '2000';
  const zoom = 14;

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAP_KEY,
    libraries,
    language: 'uk',
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
    markerRef.current = place.marker;
  }

  function createMarker(place, map) {
    if (!place.geometry || !place.geometry.location) return false;

    const marker = new window.google.maps.Marker({
      map,
      position: place.geometry.location,
    });

    window.google.maps.event.addListener(marker, 'click', () => {
      setCurrentPlaceId(place.place_id);
      updateMarker({ marker });
    });

    return marker;
  }

  function types() {
    switch (selectedCategory) {
      case 0:
        return ['restaurant'];
      case 1:
        return ['lodging'];
      default:
        return ['tourist_attraction'];
    }
  }

  function nearbySearch(stationLocation, map) {
    const request = {
      location: stationLocation,
      radius,
      type: types(),
    };
    const service = new window.google.maps.places.PlacesService(map);
    setPlacesInfo([]);
    service.nearbySearch(request, (results, status) => {
      if (status === window.google.maps.places.PlacesServiceStatus.OK && results) {
        const sortedResults = results.filter((place) => (place.opening_hours?.isOpen && place.photos && place.rating) || false).sort((placeA, placeB) => placeB.rating - placeA.rating);
        const resultsWithMarker = sortedResults.map((result) => {
          const placeMarker = createMarker(result, map);
          return ({ ...result, marker: placeMarker });
        });
        setPlacesInfo(resultsWithMarker);
      }
    });
  }

  const onLoad = useCallback((map) => {
    mapRef.current = map;
    const geocoder = new window.google.maps.Geocoder();
    geocoder.geocode({ address }, (results, status) => {
      if (status === 'OK') {
        map.setCenter(results[0].geometry.location);
        const stationLocation = new window.google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
        nearbySearch(stationLocation, map);
      } else {
        console.error(`Geocode was not successful for the following reason: ${status}`);
      }
    });
  }, [selectedCategory]);

  const onUnmount = useCallback(() => {
    mapRef.current = null;
  }, []);

  const mapOptions = ['restaurants', 'hotels', 'tourist places'];

  return (
    <div className="maps-block">
      <div className="categories">
        {mapOptions.map((title, index) => (
          <button
            type="button"
            className={`category ${index === selectedCategory ? 'active' : ''}`}
            onClick={() => setSelectedCategory(index)}
          >
            {t(title)}
          </button>
        ))}
      </div>
      <hr className="horizontal-line" data-testid="horizontal-line" />
      {selectedCategory === 0 && (
        isLoaded && (
          <div className="map-container">
            <PlacePreviewList placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} updateMarker={(place) => updateMarker(place)} />
            {currentPlaceId && <PlacePreview placeId={currentPlaceId} placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} map={mapRef.current} />}
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
        )
      )}
      {selectedCategory === 1 && (
        isLoaded && (
          <div className="map-container">
            <PlacePreviewList placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} updateMarker={(place) => updateMarker(place)} />
            {currentPlaceId && <PlacePreview placeId={currentPlaceId} placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} />}
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
        )
      )}
      {selectedCategory === 2 && (
        isLoaded && (
          <div className="map-container">
            <PlacePreviewList placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} updateMarker={(place) => updateMarker(place)} />
            {currentPlaceId && <PlacePreview placeId={currentPlaceId} placesInfo={placesInfo} setCurrentPlaceId={setCurrentPlaceId} map={mapRef.current} />}
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
        )
      )}
    </div>
  );
}

export default Maps;
