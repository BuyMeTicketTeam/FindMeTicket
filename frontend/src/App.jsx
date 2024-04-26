/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable max-len */
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import Header from './components/Header';
import useAuthCheck from './hook/useAuthCheck';
import Routers from './routers';
import CookieBanner from './cookieBanner/cookie';
// import './testServer';
import Footer from './footer';
import './App.scss';
import './locales/i18n';
import ScrollButton from './scrollButton';
import { initUsers } from './store/user/userSlice';

function App() {
  const { auth, updateAuthValue } = useAuthCheck();
  const [ticketsData, setTicketsData] = useState([]);
  const [selectedTransport, setSelectedTransport] = useState({
    bus: true,
    train: true,
    airplane: false,
    ferry: false,
  });
  const [urlSearch, setUrlSearch] = useState(null);

  const dispatch = useDispatch();

  useEffect(() => {
    const userDataFromStorage = localStorage.getItem('userData');
    if (userDataFromStorage) {
      dispatch(initUsers(JSON.parse(userDataFromStorage)));
    }
  }, []);

  return (
    <Router>
      <div className="body">
        <Header />
        <Routers
          auth={auth}
          updateAuthValue={updateAuthValue}
          ticketsData={ticketsData}
          setTicketsData={setTicketsData}
          selectedTransport={selectedTransport}
          setSelectedTransport={setSelectedTransport}
          requestBody={requestBody}
          setRequestBody={setRequestBody}
        />
        <CookieBanner />
        <ScrollButton />
        <Footer />
      </div>
    </Router>
  );
}

export default App;
