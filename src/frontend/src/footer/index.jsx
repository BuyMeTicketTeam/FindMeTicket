/* eslint-disable jsx-a11y/alt-text */
import React, { useState, useEffect } from 'react';
import './style.scss';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
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
  const [scrollEnd, setScrollEnd] = useState(false);
  const [scrollStart, setScrollStart] = useState(true);

  useEffect(() => {
    const container = document.getElementById('footerContainer');
    const handleScroll = () => {
      const isStart = container.scrollLeft === 0;
      const isEnd = container.scrollLeft + container.clientWidth === container.scrollWidth;
      setScrollStart(isStart);
      setScrollEnd(isEnd);
    };
    container.addEventListener('scroll', handleScroll);
    return () => {
      container.removeEventListener('scroll', handleScroll);
    };
  }, []);

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
              style={{ display: isFooterVisible ? 'block' : 'none', visibility: scrollEnd ? 'hidden' : 'visible' }}
              className="arrow-right"
              onClick={scrollRight}
              onKeyDown={handleRightArrowKey}
              role="button"
              tabIndex={0}
            >
              <img src={ArrowRight} alt="Scroll Right" />
            </div>
            <div
              style={{ display: isFooterVisible ? 'block' : 'none', visibility: scrollStart ? 'hidden' : 'visible' }}
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
                      <Link to="?&type=all&from=Київ&to=Харків&endpoint=1" className="rectangle">
                        <img src={Kharkiv} alt="Icon 1" />
                        <p>{t('Kiev-Kharkiv')}</p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Дніпро&to=Чернівці&endpoint=1" className="rectangle">
                        <img src={Chernivtsi} alt="Icon 2" />
                        <p>{t('Dnipro-Chernivtsi')}</p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Київ&to=Дніпро&endpoint=1" className="rectangle">
                        <img src={Dnipro} alt="Icon 3" />
                        <p>{t('Kiev-Dnipro')}</p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Харків&to=Івано-Франківськ&endpoint=1" className="rectangle">
                        <img src={Ivanofrankivsk} alt="Icon 4" />
                        <p>
                          {t('Kharkiv-Ivano-Frankivsk')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Львів&to=Київ&endpoint=1" className="rectangle">
                        <img src={Kyiv} alt="Icon 4" />
                        <p>
                          {t('Lviv-Kiev')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Дніпро&to=Одеса&endpoint=1" className="rectangle">
                        <img src={Odessa} alt="Icon 4" />
                        <p>
                          {t('Dnipro-Odessa')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Київ&to=Полтава&endpoint=1" className="rectangle">
                        <img src={Poltava} alt="Icon 4" />
                        <p>
                          {t('Kiev-Poltava')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Львів&to=Тернопіль&endpoint=1" className="rectangle">
                        <img src={Ternopil} alt="Icon 4" />
                        <p>
                          {t('Lviv-Ternopil')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Харків&to=Запоріжжя&endpoint=1" className="rectangle">
                        <img src={Zaporizhzhia} alt="Icon 4" />
                        <p>
                          {t('Kharkiv-Zaporizhzhya')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Одеса&to=Херсон&endpoint=1" className="rectangle">
                        <img src={Kherson} alt="Icon 4" />
                        <p>
                          {t('Odessa-Kherson')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Запоріжжя&to=Кривий Ріг&endpoint=1" className="rectangle">
                        <img src={Kryvyirih} alt="Icon 4" />
                        <p>
                          {t('Zaporizhzhya-Krivyi Rih')}
                        </p>
                      </Link>
                    </div>
                    <div className="footer-column">
                      <Link to="?&type=all&from=Віниця&to=Львів&endpoint=1" className="rectangle">
                        <img src={Lviv} alt="Icon 4" />
                        <p>
                          {t('Vinnytsia-Lviv')}
                        </p>
                      </Link>
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
                Підтримка
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
