/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-shadow */
import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useClickAway, useMediaQuery } from "@uidotdev/usehooks";
import Select from "react-select";
import useLanguage from "../../hook/useLanguage";
import LoginBtn from "../LoginBtn/index";
import "./header.scss";
import logo from "../../images/logo.svg";
import planetIcon from "../../images/language.svg";
import Menu from "../Menu";

export default function Header() {
  const [menuIsOpen, setMenuIsOpen] = useState(false);
  const ref = useClickAway(() => setMenuIsOpen(false));
  const { language, setLanguageValue, languagesList } = useLanguage();
  const isMediumDevice = useMediaQuery("(max-width : 767px)");
  const { t } = useTranslation("translation", { keyPrefix: "header" });

  return (
    <header data-testid="header" className="header">
      <div className="logo">
        <Link to="/">
          <img src={logo} alt="logo" />
        </Link>
      </div>
      <ul className="header-menu">
        <li className="header-menu__item">
          <Link to="/">{t("reviews")}</Link>
        </li>
        <li className="header-menu__item">
          <Link to="/tourist-places">{t("tourist-places")}</Link>
        </li>
      </ul>
      <div className="language-select">
        <img src={planetIcon} alt="Language-icon" />
        <Select
          data-testid="language-select"
          options={languagesList}
          classNamePrefix="react-select"
          placeholder={null}
          value={language}
          isSearchable={false}
          onChange={(lang) => setLanguageValue(lang.value)}
        />
      </div>
      <LoginBtn isMediumDevice={isMediumDevice} />
      {isMediumDevice && <Menu menuRef={ref} isOpen={menuIsOpen} />}
      {isMediumDevice && (
        <button
          ref={ref}
          className={`header-menu-toggler ${menuIsOpen ? "toggler_open" : ""}`}
          type="submit"
          aria-label="Open menu"
          onClick={() => setMenuIsOpen((prevState) => !prevState)}
        >
          <span />
        </button>
      )}
    </header>
  );
}
