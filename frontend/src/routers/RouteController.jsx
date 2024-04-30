/* eslint-disable react/destructuring-assignment */
/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import { Navigate } from 'react-router-dom';

export default function RouteController({ access, children }) {
  if (!access) {
    return <Navigate to="/" />;
  }
  return children;
}
