import React from "react";
import { Link, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";

import "./UnauthorizedPopup.scss";

export default function UnauthorizedPopup() {
  const { t } = useTranslation("translation", { keyPrefix: "touristPlaces" });
  const { pathname } = useLocation();
  return (
    <div className="unauthorized">
      <div className="tourist-places-background" />
      <div className="unauthorized-content">
        <h2 className="unauthorized-content__title">
          {t("unauthorizedAttention")}
        </h2>
        <p className="unauthorized-content__text">{t("unauthorizedText")}</p>
        <Link
          to="/login"
          state={{ successNavigate: pathname, closeNavigate: pathname }}
          className="unauthorized-content__button button"
        >
          {t("unauthorizedButton")}
        </Link>
      </div>
    </div>
  );
}
