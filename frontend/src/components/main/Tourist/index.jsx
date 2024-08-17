import React from "react";
import {useTranslation} from "react-i18next";
import {Link, useSearchParams} from "react-router-dom";
import "./Tourist.scss";

function Banner() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation("translation", { keyPrefix: "banner" });

  return (
    <div className="banner-container">
      <div className="centered-content">
        <p className="banner-text">
          {t("bannerText")} {searchParams.get("to")}
        </p>
        <Link
          to={`/tourist-places/${searchParams.get("to")}`}
          className="button banner__btn"
        >
          {t("bannerBtn")}
        </Link>
      </div>
    </div>
  );
}

export default Banner;
