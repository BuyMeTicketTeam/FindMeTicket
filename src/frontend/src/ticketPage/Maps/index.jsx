/* eslint-disable max-len */
/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useRef, useState, useCallback } from 'react';
import './style.css';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
// import HotelsMap from './image 6.png';
// import './googleMap';
import RestaurantMap from './image 7.png';
import TouristPlaces from './image 8.png';
import PlacePreviewList from './placePreviewList';
import PlacePreview from './placePreview';

function Maps() {
  const [selectedCategory, setSelectedCategory] = useState(0);
  const [hoveredCategory, setHoveredCategory] = useState(null);
  const [placesInfo, setPlacesInfo] = useState([]);
  const [currentPlaceId, setCurrentPlaceId] = useState(null);

  const handleCategoryClick = (index) => {
    setSelectedCategory(selectedCategory === index ? null : index);
  };

  const handleCategoryHover = (index) => {
    setHoveredCategory(index);
  };

  const handleCategoryLeave = () => {
    setHoveredCategory(null);
  };

  const containerStyle = {
    width: '100%',
    height: '400px',
  };

  const libraries = ['places', 'marker'];

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: 'AIzaSyDEZtKdK2xPWTbf6ydCsVm4ryehF1ph5A0',
    libraries,
    language: 'uk',
  });

  const mapRef = useRef(null);

  function createMarker(place, map) {
    if (!place.geometry || !place.geometry.location) return;

    const marker = new window.google.maps.Marker({
      map,
      position: place.geometry.location,
    });

    console.log(place.name);

    const infowindow = new window.google.maps.InfoWindow({
      content: place.name,
      ariaLabel: 'Uluru',
    });

    window.google.maps.event.addListener(marker, 'click', () => {
      console.log(place.place_id);
      setCurrentPlaceId(place.place_id);
      infowindow.open({
        anchor: marker,
        map,
      });
    });
  }

  function getPlaceDetails(service, place) {
    const request = {
      placeId: place.place_id,
    };
    service.getDetails(request, (result) => {
      setPlacesInfo((prevPlacesInfo) => [...prevPlacesInfo, result]);
    });
  }

  function nearbySearch(stationLocation, map) {
    const request = {
      location: stationLocation,
      radius: '500',
      type: ['restaurant'],
    };
    const service = new window.google.maps.places.PlacesService(map);
    service.nearbySearch(request, (results, status) => {
      if (status === window.google.maps.places.PlacesServiceStatus.OK && results) {
        results.forEach((result) => {
          getPlaceDetails(service, result);
          createMarker(result, map);
        });
      }
    });
  }

  const onLoad = useCallback((map) => {
    // This is just an example of getting and using the map instance!!! don't just blindly copy!
    mapRef.current = map;
    const geocoder = new window.google.maps.Geocoder();
    const address = 'пл. Вокзальная Днепр';
    geocoder.geocode({ address }, (results, status) => {
      if (status === 'OK') {
        map.setCenter(results[0].geometry.location);
        const stationLocation = new window.google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
        nearbySearch(stationLocation, map);
      } else {
        console.error(`Geocode was not successful for the following reason: ${status}`);
      }
    });
  }, []);

  const onUnmount = useCallback(() => {
    mapRef.current = null;
  }, []);

  const getCategoryClassName = (index) => `category ${selectedCategory === index ? 'active' : ''} ${hoveredCategory === index ? 'hovered' : ''}`;

  return (
    <div className="maps-block">
      <div className="categories">
        <div
          className={getCategoryClassName(0)}
          onClick={() => handleCategoryClick(0)}
          onMouseEnter={() => handleCategoryHover(0)}
          onMouseLeave={handleCategoryLeave}
        >
          Готелі
        </div>
        <div
          className={getCategoryClassName(1)}
          onClick={() => handleCategoryClick(1)}
          onMouseEnter={() => handleCategoryHover(1)}
          onMouseLeave={handleCategoryLeave}
        >
          Ресторани
        </div>
        <div
          className={getCategoryClassName(2)}
          onClick={() => handleCategoryClick(2)}
          onMouseEnter={() => handleCategoryHover(2)}
          onMouseLeave={handleCategoryLeave}
        >
          Туристичні місця
        </div>
      </div>
      <hr className="horizontal-line" data-testid="horizontal-line" />
      {selectedCategory === 0 && (
        isLoaded && (
          <GoogleMap
            mapContainerStyle={containerStyle}
            zoom={16}
            onLoad={onLoad}
            onUnmount={onUnmount}
          >
            <PlacePreviewList placesInfo={placesInfo} />
            {currentPlaceId && <PlacePreview placeId={currentPlaceId} placesInfo={placesInfo} />}
          </GoogleMap>
        )
      )}
      {selectedCategory === 1 && <img src={RestaurantMap} alt="RestaurantMap" />}
      {selectedCategory === 2 && <img src={TouristPlaces} alt="TouristPlaces" />}
    </div>
  );
}

export default Maps;
