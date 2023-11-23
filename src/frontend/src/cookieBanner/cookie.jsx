import React, { useState, useEffect } from 'react';
import './cookie.css';

function CookieBanner() {
  const [acceptedCookies, setAcceptedCookies] = useState(
    localStorage.getItem('acceptedCookies') === 'true',
  );

  const acceptCookies = () => {
    setAcceptedCookies(true);
  };

  useEffect(() => {
    localStorage.setItem('acceptedCookies', String(acceptedCookies));
  }, [acceptedCookies]);

  if (acceptedCookies) {
    return null;
  }

  return (
    <div className="cookie-banner">
      <div className="cookie-img">
        <img src="../img/cookie.png" alt="cookie" />
      </div>
      <p>Этот веб-сайт использует куки для улучшения вашего опыта.</p>
      <button onClick={acceptCookies} type="button">
        Принять куки
      </button>
    </div>
  );
}

export default CookieBanner;
