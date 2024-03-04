/* eslint-disable jsx-a11y/alt-text */
import React, { useState } from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';
import Arrow from './arrow.svg';
import ArrowLeft from './arrowLeft.svg';
import ArrowRight from './arrowRight.svg';
import Chernivtsi from './chernivtsi.jpg';
import Kharkiv from './kharkiv.jpg';
import Dnipro from './dnipro.jpg';
import Ivanofrankivsk from './ivano-frankivsk.jpg';
import Kyiv from './kyiv.jpg';
import Odessa from './odessa.jpg';
import Poltava from './poltava.jpg';
import Ternopil from './ternopil.jpg';
import Zaporizhzhia from './zaporizhzhia.jpg';
import Kherson from './Kherson.jpg';
import Kryvyirih from './kryvyi rih.jpg';
import Lviv from './lviv.jpg';

function Footer() {
  const { t } = useTranslation('translation', { keyPrefix: 'cities' });
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
  const handleWheelScroll = (e) => {
    if (isFooterVisible) {
      e.preventDefault();
      const container = document.getElementById('footerContainer');
      container.scrollLeft += e.deltaY * 3;
      e.stopPropagation();
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      toggleDetailedInfo();
    }
  };
  const scrollLeft = () => {
    document.getElementById('footerContainer').scrollBy({
      left: -300,
      behavior: 'smooth',
    });
  };

  const scrollRight = () => {
    document.getElementById('footerContainer').scrollBy({
      left: 300,
      behavior: 'smooth',
    });
  };
  const handleLeftArrowKey = (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      scrollLeft();
    }
  };

  const handleRightArrowKey = (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      scrollRight();
    }
  };

  return (
    <div className="footer-container" onWheel={handleWheelScroll}>
      <div className="footer-container">
        <div className="footer-content-container">
          <footer style={{ height: footerHeight }}>
            <div
              style={{ display: isFooterVisible ? 'block' : 'none' }}
              className="arrow-right"
              onClick={scrollRight}
              onKeyDown={handleRightArrowKey}
              role="button"
              tabIndex={0}
            >
              <img src={ArrowRight} alt="Scroll Right" />
            </div>
            <div
              style={{ display: isFooterVisible ? 'block' : 'none' }}
              className="arrow-left"
              onClick={scrollLeft}
              onKeyDown={handleLeftArrowKey}
              role="button"
              tabIndex={0}
            >
              <img src={ArrowLeft} alt="Scroll Left" />
            </div>
            <div className="footer-container container">
              <div className="footer-column-container" id="footerContainer">
                {isFooterVisible && (
                  <>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Kharkiv} alt="Icon 1" />
                        <p>{t('Kiev-Kharkiv')}</p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Chernivtsi} alt="Icon 2" />
                        <p>{t('Dnipro-Chernivtsi')}</p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Dnipro} alt="Icon 3" />
                        <p>{t('Kiev-Dnipro')}</p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Ivanofrankivsk} alt="Icon 4" />
                        <p>
                          {t('Kharkiv-Ivano-Frankivsk')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Kyiv} alt="Icon 4" />
                        <p>
                          {t('Lviv-Kiev')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Odessa} alt="Icon 4" />
                        <p>
                          {t('Dnipro-Odessa')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Poltava} alt="Icon 4" />
                        <p>
                          {t('Kiev-Poltava')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Ternopil} alt="Icon 4" />
                        <p>
                          {t('Lviv-Ternopil')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Zaporizhzhia} alt="Icon 4" />
                        <p>
                          {t('Kharkiv-Zaporizhzhya')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Kherson} alt="Icon 4" />
                        <p>
                          {t('Odessa-Kherson')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle-k">
                        <img src={Kryvyirih} alt="Icon 4" />
                        <p>
                          {t('Zaporizhzhya-Krivyi Rih')}
                        </p>
                      </div>
                    </div>
                    <div className="footer-column">
                      <div className="rectangle">
                        <img src={Lviv} alt="Icon 4" />
                        <p>
                          {t('Vinnytsia-Lviv')}
                        </p>
                      </div>
                    </div>

                  </>
                )}
              </div>
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
              <a
                className="buy-me-coffee"
                href="https://www.buymeacoffee.com/findmeticket"
              >
                <img className="coffee" src="https://cdn.buymeacoffee.com/buttons/bmc-new-btn-logo.svg" />
                Buy me a coffee
              </a>
              <span className="copyright">© 2023 FindMeTicket</span>
              <div className="contact-footer">
                <span className="privacy-policy">
                  <a href="/privacy-policy">Privacy policy</a>
                </span>
                <span className="contact-info">findmeticketweb@gmail.com</span>
                <span className="contact-info">+380958454545</span>
              </div>
            </div>
          </footer>
        </div>
      </div>
    </div>
  );
}
export default Footer;
