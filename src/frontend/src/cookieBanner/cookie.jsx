import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import './cookie.css';

function CookieBanner() {
  const [acceptedCookies, setAcceptedCookies] = useState(
    localStorage.getItem('acceptedCookies') === 'true',
  );

  const acceptCookies = () => {
    setAcceptedCookies(true);
  };

  const { t } = useTranslation('translation', { keyPrefix: 'cookie' });

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
      <p>{t('text')}</p>
      <button onClick={acceptCookies} type="button">
        {t('accept-btn')}
      </button>
    </div>
  );
}

export default CookieBanner;
