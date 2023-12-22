import React from 'react';
import './ad.css';

export default function Index({ isBig }) {
  return (
    <div className={`ad-container ${isBig ? 'ad_big' : ''}`}>
      <div className="ad-text">Тут може бути ваша реклама!</div>
    </div>
  );
}
