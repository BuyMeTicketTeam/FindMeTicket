import React from "react";
import Select from "react-select";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useLockBodyScroll } from "@uidotdev/usehooks";

import useLanguage from "../../hook/useLanguage";

import planetIcon from "../../images/language.svg";
import instagramIcon from "../../images/social-media/instagram.svg";
import githubIcon from "../../images/social-media/github.svg";
import linkedinIcon from "../../images/social-media/linkedin.svg";

import "./Menu.scss";

export default function Menu({ isOpen, menuRef }) {
  const { language, setLanguageValue, languagesList } = useLanguage();
  const { t } = useTranslation("translation", { keyPrefix: "header" });
  useLockBodyScroll();

  return (
    <>
      {isOpen && <div className="menu-overlay" />}
      <div
        ref={menuRef}
        className={`menu ${isOpen ? "menu-open" : "menu-close"}`}
      >
        <ul className="nav-list">
          <li className="nav-list__item">
            <Link to="/">{t("reviews")}</Link>
          </li>
          <li className="nav-list__item">
            <Link to="/tourist-places">{t("tourist-places")}</Link>
          </li>
          <li className="nav-list__item language_item">
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
          </li>
        </ul>
        <h3 className="menu__title">Контакти</h3>
        <ul className="menu-contacts-list">
          <li className="menu-contacts-list__item">
            <a href="mailto:findmeticketweb@gmail.com">
              findmeticketweb@gmail.com
            </a>
          </li>
          <li className="menu-contacts-list__item">
            <a href="tel:+380958454545">+380958454545</a>
          </li>
        </ul>
        <ul className="menu-social-media">
          <li className="menu-social-media__item">
            <a href="/">
              <img src={instagramIcon} alt="instagram" />
            </a>
          </li>
          <li className="menu-social-media__item">
            <a href="https://github.com/FindMeTicketTeam/FindMeTicket">
              <img src={githubIcon} alt="github" />
            </a>
          </li>
          <li className="menu-social-media__item">
            <a href="/">
              <img src={linkedinIcon} alt="linkedIn" />
            </a>
          </li>
        </ul>
      </div>
    </>
  );
}
