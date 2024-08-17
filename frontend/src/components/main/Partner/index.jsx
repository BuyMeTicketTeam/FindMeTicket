import React from 'react';
import './Style.scss';
import {useTranslation} from 'react-i18next';
import Proizd from './proizd.webp';
import BusFor from './busfor.png';
import Infobus from './infobus.png';
import Unnamed from './unnamed.jpg';

function Partner() {
  const { t } = useTranslation('translation', { keyPrefix: 'partners' });
  return (
    <div>
      <div className="partner">
        <div className="our-partners">{t('partners')}</div>
        <div className="partner-body">
          <a href="https://proizd.ua/" className="partner-item">
            <img src={Proizd} alt="Proizd" />
            <div className="partner-title">proizd</div>
          </a>
          <a href="https://busfor.ua/" className="partner-item">
            <img src={BusFor} alt="Busfor" />
            <div className="partner-title">busfor</div>
          </a>
          <a href="https://infobus.eu" className="partner-item">
            <img src={Infobus} alt="Infobus" />
            <div className="partner-title">infobus</div>
          </a>
          <a href="https://tickets.ua/" className="partner-item">
            <img src={Unnamed} alt="Infobus" />
            <div className="partner-title">tickets</div>
          </a>
        </div>
      </div>
    </div>
  );
}

export default Partner;
