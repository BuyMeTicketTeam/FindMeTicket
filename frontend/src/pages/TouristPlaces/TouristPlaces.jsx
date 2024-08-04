import React from "react";
import "./TouristPlaces.scss";
import { useParams } from "react-router-dom";
import UnauthorizedPopup from "./UnauthorizedPopup/UnauthorizedPopup";
import SearchCity from "./SearchCity/SearchCity";
import Map from "./Map/Map";

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
