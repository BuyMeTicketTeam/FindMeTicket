import React from "react";
import {useTranslation} from "react-i18next";
import {Link, useSearchParams} from "react-router-dom";
import "./Tourist.scss";

function Banner() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation("translation", { keyPrefix: "banner" });
  const arrivalCity = searchParams.get("arrivalCity");

  return (
    <div className="banner-container">
      <div className="centered-content">
        <p className="banner-text">
          {t("bannerText")} {arrivalCity}
        </p>
        <Link
          to={`/tourist-places/${arrivalCity}`}
          className="button banner__btn"
        >
          {t("bannerBtn")}
        </Link>
      </div>
    </div>
  );
}

export default Banner;
