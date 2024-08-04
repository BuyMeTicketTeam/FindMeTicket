import React from "react";
import loaderIcon from "../../../images/loader.svg";

function TransportButton({ label, isActive, onClick, img, disabled, loading }) {
  return (
    <button
      type="button"
      className={`transport-btn ${isActive ? "active" : ""} ${
        disabled || loading ? "disabled" : ""
      }`}
      onClick={onClick}
      disabled={disabled || loading}
    >
      <img className="transportation" src={img} alt={label} />
      {loading && !disabled ? <img src={loaderIcon} alt="Loading..." /> : label}
    </button>
  );
}

export default TransportButton;
