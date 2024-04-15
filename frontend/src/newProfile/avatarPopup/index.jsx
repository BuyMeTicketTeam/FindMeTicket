import React from 'react';
import './style.scss';
import Ellipse from '../Ellipse 9.png';

import {
  rank1, rank2, rank3, rank4, rank5, rank6, lockImage,
} from './img/img';

function AvatarPopup({ closeAvatarPopup, googlePicture, basicPicture }) {
  return (
    <div className="avatar-popup">
      <div className="avatar-popup-content">
        <div className="image-container">
          <div className="image-card">
            <img src={rank1} alt="rank1" className="image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-s" />
            <span className="level-text">Lvl 1</span>
          </div>

          <div className="image-card">
            <img src={rank2} alt="rank2" className="image" />
            <img src={lockImage} alt="lock" className="lock-image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-a" />
            <span className="level-text">Lvl 1</span>
          </div>
          <div className="image-card">
            <img src={rank3} alt="rank3" className="image" />
            <img src={lockImage} alt="lock" className="lock-image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-b" />
            <span className="level-text">Lvl 1</span>
          </div>
          <div className="image-card">
            <img src={rank4} alt="rank4" className="image" />
            <img src={lockImage} alt="lock" className="lock-image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-c" />
            <span className="level-text">Lvl 1</span>
          </div>
          <div className="image-card">
            <img src={rank5} alt="rank5" className="image" />
            <img src={lockImage} alt="lock" className="lock-image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-d" />
            <span className="level-text">Lvl 1</span>
          </div>
          <div className="image-card">
            <img src={rank6} alt="rank6" className="image-rank6" />
            <img src={lockImage} alt="lock" className="lock-image" />
            <img src={googlePicture || basicPicture || Ellipse} alt="Avatar" className="avatar-overlay-e" />
            <span className="level-text">Lvl 1</span>
          </div>
        </div>
        <button className="close-button-rank" type="button" onClick={closeAvatarPopup}>
          &times;
        </button>
      </div>
    </div>
  );
}

export default AvatarPopup;
