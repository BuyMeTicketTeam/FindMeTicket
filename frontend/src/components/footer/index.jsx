/* eslint-disable jsx-a11y/alt-text */
import React, { useState, useEffect } from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { Carousel } from '@mageplaza/react-carousel';
import CarouselArrow from './carouselArrow';
import Arrow from './arrow.svg';
import {
  Chernivtsi, Dnipro, IvanoFrankivsk, Kharkiv, KryvyiRih, Kyiv,
  Lviv, Odesa, Poltava, Ternopil, Kherson, Zaporizhzhia,
} from './img/img';

function Footer() {
  const { t } = useTranslation('translation', { keyPrefix: 'cities' });
  const [isFooterVisible, setIsFooterVisible] = useState(false);
  const [footerHeight, setFooterHeight] = useState('60px');
  const [isDetailedInfoVisible, setIsDetailedInfoVisible] = useState(true);

  useEffect(() => {
    if (isFooterVisible) {
      window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
    }
  }, [isFooterVisible]);

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
      <div className="footer-container">
        <div className="footer-content-container">
          <footer style={{ height: footerHeight }}>
            {isFooterVisible && (
              <div className="carousel-wrapper">
                <Carousel
                  show={4}
                  width={1300}
                  swiping
                  useArrowKeys
                  rightArrow={<CarouselArrow direction="right" />}
                  leftArrow={<CarouselArrow direction="left" />}
                >
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Kiev')}&to=${t('Kharkiv')}&endpoint=1`} className="rectangle">
                      <img src={Kharkiv} alt="Icon 1" />
                      <p>
                        {t('Kiev')}
                        {' - '}
                        {t('Kharkiv')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Dnipro')}&to=${t('Chernivtsi')}&endpoint=1`} className="rectangle">
                      <img src={Chernivtsi} alt="Icon 2" />
                      <p>
                        {t('Dnipro')}
                        {' - '}
                        {t('Chernivtsi')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Kiev')}&to=${t('Dnipro')}&endpoint=1`} className="rectangle">
                      <img src={Dnipro} alt="Icon 3" />
                      <p>
                        {t('Kiev')}
                        {' - '}
                        {t('Dnipro')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Kharkiv')}&to=${t('Ivano-Frankivsk')}&endpoint=1`} className="rectangle">
                      <img src={IvanoFrankivsk} alt="Icon 4" />
                      <p className="word-space">
                        {t('Kharkiv')}
                        {' - '}
                        <span style={{ whiteSpace: 'nowrap' }}>{t('Ivano-Frankivsk')}</span>
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Lviv')}&to=${t('Kiev')}&endpoint=1`} className="rectangle">
                      <img src={Kyiv} alt="Icon 4" />
                      <p>
                        {t('Lviv')}
                        {' - '}
                        {t('Kiev')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Dnipro')}&to=${t('Odessa')}&endpoint=1`} className="rectangle">
                      <img src={Odesa} alt="Icon 4" />
                      <p>
                        {t('Dnipro')}
                        {' - '}
                        {t('Odessa')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Kiev')}&to=${t('Poltava')}&endpoint=1`} className="rectangle">
                      <img src={Poltava} alt="Icon 4" />
                      <p>
                        {t('Kiev')}
                        {' - '}
                        {t('Poltava')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Lviv')}&to=${t('Ternopil')}&endpoint=1`} className="rectangle">
                      <img src={Ternopil} alt="Icon 4" />
                      <p>
                        {t('Lviv')}
                        {' - '}
                        {t('Ternopil')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Kharkiv')}&to=${t('Zaporizhzhya')}&endpoint=1`} className="rectangle">
                      <img src={Zaporizhzhia} alt="Icon 4" />
                      <p>
                        {t('Kharkiv')}
                        {' - '}
                        {t('Zaporizhzhya')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Odessa')}&to=${t('Kherson')}&endpoint=1`} className="rectangle">
                      <img src={Kherson} alt="Icon 4" />
                      <p>
                        {t('Odessa')}
                        {' - '}
                        {t('Kherson')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Zaporizhzhya')}&to=${t('Krivyi Rih')}&endpoint=1`} className="rectangle">
                      <img src={KryvyiRih} alt="Icon 4" />
                      <p className="word-space">
                        {t('Zaporizhzhya')}
                        {' - '}
                        {t('Krivyi Rih')}
                      </p>
                    </Link>
                  </div>
                  <div className="footer-column">
                    <Link to={`/?&type=all&from=${t('Vinnytsya')}&to=${t('Lviv')}&endpoint=1`} className="rectangle">
                      <img src={Lviv} alt="Icon 4" />
                      <p>
                        {t('Vinnytsya')}
                        {' - '}
                        {t('Lviv')}
                      </p>
                    </Link>
                  </div>
                </Carousel>
              </div>
            )}
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
              <span className="copyright">© 2023 FindMeTicket</span>
              <div className="contact-footer">
                <span className="privacy-policy">
                  <a href="/privacy-policy">
                    {t('Privacy policy')}
                  </a>
                </span>
                <span className="contact-info">
                  <a href="mailto:findmeticketweb@gmail.com">
                    findmeticketweb@gmail.com
                  </a>
                </span>
                <span className="contact-info">
                  <a href="tel:+380958454545">+380958454545</a>
                </span>
              </div>
            </div>
          </footer>
        </div>
      </div>
    </div>
  );
}
export default Footer;
