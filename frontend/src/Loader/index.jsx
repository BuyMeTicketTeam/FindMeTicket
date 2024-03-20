import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import './loader.scss';

export default function Loader() {
  const [animation, setAnimation] = useState('first');
  const { t } = useTranslation('translation', { keyPrefix: 'loading' });

  function changeText() {
    switch (animation) {
      case 'first':
        setAnimation('second');
        break;
      case 'second':
        setAnimation('third');
        break;
      case 'third':
        setAnimation('fourth');
        break;
      case 'fourth':
        setAnimation('fifth');
        break;
      default:
        setAnimation('first');
        break;
    }
  }

  useEffect(() => {
    const interval = setTimeout(changeText, 3000);
    return () => clearTimeout(interval);
  }, [animation]);

  return (
    <div data-testid="loader" className="loader-wrapper">
      <div className="truck-wrapper">
        <div className="loader__text-wrapper">
          <ul className={`loader__inner-text ${animation}`}>
            <li>{t('text-our-service')}</li>
            <li>{t('text-thanks')}</li>
            <li>{t('text-wait')}</li>
            <li>{t('text-almost')}</li>
            <li>{t('text-tourist-places')}</li>
          </ul>
        </div>
        <div className="truck">
          <div className="truck-container" />
          <div className="glases" />
          <div className="bonet" />
          <div className="base" />
          <div className="base-aux" />
          <div className="wheel-back" />
          <div className="wheel-front" />
          <div className="smoke" />
        </div>
      </div>
    </div>
  );
}
