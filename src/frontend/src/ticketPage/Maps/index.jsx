import React, { useState } from 'react';
import './style.scss';
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

  const mapOptions = ['Готелі', 'Ресторани', 'Туристичні місця'];

  return (
    <div className="maps-block">
      <div className="categories">
        {mapOptions.map((title, index) => (
          <button
            type="button"
            className={getCategoryClassName(index)}
            onClick={() => handleCategoryClick(index)}
            onMouseEnter={() => handleCategoryHover(index)}
            onMouseLeave={handleCategoryLeave}
          >
            {title}
          </button>
        ))}
      </div>
      <hr className="horizontal-line" data-testid="horizontal-line" />
      {selectedCategory === 1 && <img src={RestaurantMap} alt="RestaurantMap" />}
      {selectedCategory === 2 && <img src={TouristPlaces} alt="TouristPlaces" />}
    </div>
  );
}

export default Maps;
