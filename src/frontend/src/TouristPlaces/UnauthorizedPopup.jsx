import React from 'react';
import { Link } from 'react-router-dom';

export default function UnauthorizedPopup() {
  return (
    <div className="unauthorized">
      <div className="unauthorized-background" />
      <div className="unauthorized-content">
        <h2 className="unauthorized-content__title">Увага!</h2>
        <p className="unauthorized-content__text">Карта туристичних місць доступна тільки авторизованим користувачам</p>
        <Link to="/login" className="unauthorized-content__button button">Авторизуватися</Link>
      </div>
    </div>
  );
}
