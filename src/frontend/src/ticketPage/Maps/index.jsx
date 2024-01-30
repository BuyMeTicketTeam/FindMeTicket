/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useState } from 'react';
import './style.css';
// import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
// import HotelsMap from './image 6.png';
// import './googleMap';
import RestaurantMap from './image 7.png';
import TouristPlaces from './image 8.png';

function Maps() {
  const [selectedCategory, setSelectedCategory] = useState(0);
  const [hoveredCategory, setHoveredCategory] = useState(null);

  const handleCategoryClick = (index) => {
    setSelectedCategory(selectedCategory === index ? null : index);
  };

  const handleCategoryHover = (index) => {
    setHoveredCategory(index);
  };

  const handleCategoryLeave = () => {
    setHoveredCategory(null);
  };

  // const containerStyle = {
  //   width: '100%',
  //   height: '400px',
  // };

  // const { isLoaded } = useJsApiLoader({
  //   id: 'google-map-script',
  //   googleMapsApiKey: 'AIzaSyDEZtKdK2xPWTbf6ydCsVm4ryehF1ph5A0',
  // });

  // const mapRef = useRef(null);

  // const onLoad = React.useCallback((map) => {
  //   // This is just an example of getting and using the map instance!!! don't just blindly copy!
  //   mapRef.current = map;
  //   const geocoder = new window.google.maps.Geocoder();
  //   const address = 'Днепр';
  //   geocoder.geocode({ address }, (results, status) => {
  //     if (status === 'OK') {
  //       console.log(results);
  //       map.setCenter(results[0].geometry.location);
  //       const marker = new window.google.maps.Marker({
  //         map,
  //         position: results[0].geometry.location,
  //       });
  //     } else {
  //       alert(`Geocode was not successful for the following reason: ${status}`);
  //     }
  //   });
  // }, []);

  // const onUnmount = useCallback(() => {
  //   mapRef.current = null;
  // }, []);

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
      {/* {selectedCategory === 0 && (
        isLoaded && (
          <GoogleMap
            mapContainerStyle={containerStyle}
            zoom={100}
            onLoad={onLoad}
            onUnmount={onUnmount}
          />
        )
      )} */}
      {selectedCategory === 1 && <img src={RestaurantMap} alt="RestaurantMap" />}
      {selectedCategory === 2 && <img src={TouristPlaces} alt="TouristPlaces" />}
    </div>
  );
}

export default Maps;
