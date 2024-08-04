/* eslint-disable react/jsx-one-expression-per-line */
/* eslint-disable import/no-extraneous-dependencies */
import React from "react";
import Popup from "reactjs-popup";
import { v4 as uuidv4 } from "uuid";
import { rank1, rank2, rank3, rank4, rank5 } from "../../../images/ranks/ranks";
import lockImage from "../../../images/lock.svg";

import "./AvatarPopup.scss";

export default function AvatarPopup({ open, closeModal, userAvatar }) {
  const outlines = [rank1, rank2, rank3, rank4, rank5];
  const level = 1;

  return (
    <Popup open={open} onClose={closeModal} position="right center">
      <div className="avatar-popup">
        {outlines.map((outlineItem, index) => (
          <div key={uuidv4()} className="avatar-popup-card">
            <div className="avatar-popup-card__body">
              <img
                src={outlineItem}
                alt="rank1"
                className={`avatar-popup-card__image image_${index}`}
              />
              <img
                src={userAvatar}
                alt="Avatar"
                className="avatar-popup-card__avatar"
              />
              <p className="avatar-popup-card__level">Lvl {index}</p>
            </div>
            {index > level - 1 && (
              <div className="avatar-popup-card__overlay" />
            )}
            {index > level - 1 && (
              <img
                src={lockImage}
                alt="lock"
                className="avatar-popup-card__lock"
              />
            )}
          </div>
        ))}
        <button
          className="avatar-popup-close"
          type="button"
          onClick={closeModal}
        >
          &times;
        </button>
      </div>
    </Popup>
  );
}
