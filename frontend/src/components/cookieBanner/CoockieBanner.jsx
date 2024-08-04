import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import cookieIcon from "../../images/cookie/cookie.png";
import "./CoockieBanner.scss";

function CookieBanner() {
  const [acceptedCookies, setAcceptedCookies] = useState(
    localStorage.getItem("acceptedCookies") === "true"
  );

  const acceptCookies = () => {
    setAcceptedCookies(true);
  };

  const { t } = useTranslation("translation", { keyPrefix: "cookieBanner" });

  useEffect(() => {
    localStorage.setItem("acceptedCookies", String(acceptedCookies));
  }, [acceptedCookies]);

  if (acceptedCookies) {
    return null;
  }

  return (
    <div className="cookie-banner">
      <div className="cookie-img">
        <img src={cookieIcon} alt="cookie" />
      </div>
      <p data-testid="cookie-text">{t("aboutCookieText")}</p>
      <button
        data-testid="cookie-accept-btn"
        onClick={acceptCookies}
        type="button"
      >
        {t("acceptBtn")}
      </button>
    </div>
  );
}

export default CookieBanner;
