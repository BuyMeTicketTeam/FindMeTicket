import React from "react";
import Popup from "reactjs-popup";
import { useTranslation } from "react-i18next";

import "./inProgress.scss";

export default function Index({ open, closeModal, title, text }) {
  const { t } = useTranslation("translation", { keyPrefix: "in-progress" });

  return (
    <Popup open={open} onClose={closeModal} position="right center">
      <div className="message background">
        <div className="message__content">
          <div className="message__header">
            <h2 className="message__title">{title}</h2>
            <button
              type="button"
              className="message__close close"
              aria-label="Close"
              onClick={closeModal}
            />
          </div>
          <p className="message__text">{text}</p>
          <button type="button" className="button" onClick={closeModal}>
            {t("ok")}
          </button>
        </div>
      </div>
    </Popup>
  );
}
