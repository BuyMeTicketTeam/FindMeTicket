/* eslint-disable react/jsx-one-expression-per-line */
/* eslint-disable import/no-extraneous-dependencies */
import React from 'react';
import Popup from 'reactjs-popup';
import {
  rank1, rank2, rank3, rank4, rank5,
} from '../../../images/ranks/ranks';
import lockImage from '../../../images/lock.svg';

export default function AvatarPopup({ open, closeModal, userAvatar }) {
  const outlines = [rank1, rank2, rank3, rank4, rank5];
  const level = 1;

  return (
    <Popup open={open} onClose={closeModal} position="right center">
      <div className="avatar-popup">
        {outlines.map((outlineItem, index) => (
          <div className="avatar-popup-card">
            <div className={`avatar-popup-card__body ${(index <= level - 1) ? 'dark_body' : ''}`}>
              <img src={outlineItem} alt="rank1" className="avatar-popup-card__image" />
              <img src={userAvatar} alt="Avatar" className="avatar-popup-card__overlay" />
              <span className="avatar-popup-card__level">Lvl {index}</span>
            </div>
            {(index <= level - 1) && <img src={lockImage} alt="lock" className="avatar-popup-card__lock" />}
          </div>
        ))}
        <button className="avatar-popup-close" type="button" onClick={closeModal}>
          &times;
        </button>
      </div>
    </Popup>
  );
}
