/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
/* @license
 * Copyright 2019 Google LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">
let geocoder;
let map;

function codeAddress() {
  const address = 'Днепр';
  geocoder.geocode({ address }, (results, status) => {
    if (status === 'OK') {
      console.log(results);
      map.setCenter(results[0].geometry.location);
      const marker = new google.maps.Marker({
        map,
        position: results[0].geometry.location,
      });
    } else {
      alert(`Geocode was not successful for the following reason: ${status}`);
    }
  });
}

function initMap() {
  geocoder = new google.maps.Geocoder();
  const latlng = new google.maps.LatLng(-34.397, 150.644);
  const mapOptions = {
    zoom: 8,
    center: latlng,
  };
  map = new google.maps.Map(document.getElementById('map'), mapOptions);
  codeAddress();
}

initMap();
