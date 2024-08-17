/* eslint-disable max-len */
import React, {useState} from "react";
import {Outlet} from "react-router-dom";
import SearchField from "./SearchField";
import Transport from "./Transport";
import Ad from "../../common/Ad/index";
import "./Main.scss";
import TicketsBody from "./Body";
import Partner from "./Partner";
import Footer from "../footer/index";

export default function Index({
  ticketsData,
  setTicketsData,
  selectedTransport,
  setSelectedTransport,
}) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [urlSearch, setUrlSearch] = useState();

  return (
    <div className="main-block main">
      <div className="container">
        <div className="search_index">
          <Ad />
          <Transport
            loading={loading}
            selectedTransport={selectedTransport}
            setSelectedTransport={setSelectedTransport}
            ticketsData={ticketsData}
            setTicketsData={setTicketsData}
          />
          <SearchField
            loading={loading}
            setLoading={setLoading}
            setTicketsData={setTicketsData}
            urlSearch={urlSearch}
            setUrlSearch={setUrlSearch}
            setError={setError}
            ticketsData={ticketsData}
            selectedTransport={selectedTransport}
          />
        </div>
        <TicketsBody
          loading={loading}
          error={error}
          setTicketsData={setTicketsData}
          ticketsData={ticketsData}
          selectedTransport={selectedTransport}
        />
        <Partner />
        <Footer />
      </div>
      <Outlet />
    </div>
  );
}
