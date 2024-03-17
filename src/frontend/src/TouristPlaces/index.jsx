import React from 'react';
import './touristPlaces.scss';
import { useParams } from 'react-router-dom';
import UnauthorizedPopup from './UnauthorizedPopup';
import SearchCity from './SearchCity';
import Map from './Map';

function TouristPlaces({ auth }) {
  const { city } = useParams();

  if (!auth) {
    return <UnauthorizedPopup />;
  }
  if (!city) {
    return <SearchCity />;
  }
  return <Map address={city} />;
}

export default TouristPlaces;
