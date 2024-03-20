import React, { useState, useEffect } from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';

function ScrollButton() {
  const [isVisible, setIsVisible] = useState(false);
  const { t } = useTranslation('translation', { keyPrefix: 'scroll-button' });

  useEffect(() => {
    const toggleVisibility = () => {
      if (window.pageYOffset > 300) {
        setIsVisible(true);
      } else {
        setIsVisible(false);
      }
    };

    window.addEventListener('scroll', toggleVisibility);

    return () => window.removeEventListener('scroll', toggleVisibility);
  }, []);

  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  };

  return (
    <div className="scroll-container">
      {isVisible && (
        <button type="button" className="scroll" onClick={scrollToTop}>
          <span className="arrow-up" />
          <span className="scroll-text">{t('up')}</span>
        </button>
      )}
    </div>
  );
}

export default ScrollButton;
