import React from "react";
import { useTranslation } from "react-i18next";
import "./Ad.scss";

export default function Ad() {
  const { t } = useTranslation("translation", { keyPrefix: "ad" });
  return (
    <div className="ad-container">
      <div className="ad-text">{t("title")}</div>
      <div className="contact-email">{t("email")}</div>
    </div>
  );
}
