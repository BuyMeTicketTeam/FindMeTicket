/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useState } from 'react';
import './style.css';
import HotelsMap from './image 6.png';
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
      {selectedCategory === 0 && <img src={HotelsMap} alt="HotelsMap" />}
      {selectedCategory === 1 && <img src={RestaurantMap} alt="RestaurantMap" />}
      {selectedCategory === 2 && <img src={TouristPlaces} alt="TouristPlaces" />}
    </div>
  );
}

export default Maps;
