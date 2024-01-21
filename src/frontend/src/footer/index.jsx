import React, { useState } from 'react';
import './style.scss';
import Arrow from './arrow.svg';

function Footer() {
  const [isFooterVisible, setIsFooterVisible] = useState(false);
  const [footerHeight, setFooterHeight] = useState('60px');
  const [isDetailedInfoVisible, setIsDetailedInfoVisible] = useState(true);

  const toggleFooter = () => {
    setIsFooterVisible(!isFooterVisible);
    setFooterHeight(isFooterVisible ? '60px' : 'auto');
    setIsDetailedInfoVisible(isFooterVisible);
  };

  const toggleDetailedInfo = () => {
    setIsDetailedInfoVisible(!isDetailedInfoVisible);
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      toggleDetailedInfo();
    }
  };

  return (
    <div className="footer-container">
      <div className="footer-content-container">
        <footer style={{ height: footerHeight }}>
          <div className="footer-container container">
            {isFooterVisible && (
              <>
                <div className="footer-column">
                  <h4>Туристичні місця</h4>
                  <p>&#8226; Київ</p>
                  <p>&#8226; Львів</p>
                  <p>&#8226; Одеса</p>
                </div>

                <div className="footer-column">
                  <h4>Популярні маршрути</h4>
                  <p>&#8226; Київ-Одеса</p>
                  <p>&#8226; Львів-Дніпро</p>
                  <p>&#8226; Одеса-Львів</p>
                </div>

                <div className="footer-column">
                  <h4>Контакти</h4>
                  <div className="footer-contact-line">
                    <span>findmeticketweb@gmail.com</span>
                  </div>
                  <div className="footer-contact-line">
                    <span>+380958454545</span>
                  </div>
                </div>
              </>
            )}
          </div>
          <div
            className={`footer-arrow-icon ${isFooterVisible ? 'open' : ''}`}
            data-testid="Toggle Footer"
            onClick={toggleFooter}
            role="button"
            tabIndex={0}
            onKeyDown={handleKeyDown}
          >
            <img src={Arrow} alt="Arrow" />
          </div>
          <div
            className="footer-year"
            role="button"
            tabIndex={isDetailedInfoVisible ? '0' : '-1'}
            onClick={toggleDetailedInfo}
            onKeyDown={handleKeyDown}
          >
            <span>2023 FindMeTicket</span>
          </div>
        </footer>
      </div>
    </div>
  );
}

export default Footer;
