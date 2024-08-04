import React from "react";
import { Link } from "react-router-dom";

import "./SuccessPopup.scss";

export default function SuccessPopup({ t }) {
  return (
    <p className="success-popup">
      {t("success_message")}{" "}
      <Link
        className="success-popup__link"
        data-testid=""
        to="/login"
        state={{ successNavigate: "/", closeNavigate: "/" }}
      >
        {t("auth_link")}
      </Link>
    </p>
  );
}
