/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState } from 'react';
import './profile.scss';
import { useTranslation } from 'react-i18next';
import Cookies from 'universal-cookie';
import { Link, useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import makeQuerry from '../helper/querry';
import DeleteConfirmationPopup from '../header/deletePopup';
import Frame from './Frame.svg';
import History from './history.svg';
import Email from './email.svg';
import Phone from './phone.svg';
import Address from './address.svg';
import Ellipse from './Ellipse 9.png';
import { busIcon, trainIcon, everythingIcon } from './transport-img/img';
import loaderIcon from './spinning-loading.svg';

function Popup({
  // setIsProfilePopup,
  updateAuthValue,
  status,
}) {
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const cookies = new Cookies(null, { path: '/' });
  const [notificationEnabled, setNotificationEnabled] = useState(false);
  const [isHistoryExpanded, setIsHistoryExpanded] = useState(false);
  const [history, setHistory] = useState([]);
  const { t } = useTranslation('translation', { keyPrefix: 'profile' });
  const navigate = useNavigate();

  function handleLogoutButton() {
    makeQuerry('logout').then((response) => {
      switch (response.status) {
        case 200:
          navigate('/');
          updateAuthValue(null);
          localStorage.removeItem('JWTtoken');
          cookies.remove('rememberMe');
          cookies.remove('USER_ID');
          break;
        default:
          break;
      }
    });
  }

  function handleDeleteButton() {
    makeQuerry('delete-user', undefined, undefined, 'DELETE').then((response) => {
      switch (response.status) {
        case 200:
          navigate('/');
          updateAuthValue(null);
          localStorage.removeItem('JWTtoken');
          cookies.remove('rememberMe');
          cookies.remove('USER_ID');
          break;
        default:
          break;
      }
    });
  }

  const handleDeleteAccount = () => {
    setShowDeleteConfirmation(true);
  };

  const handleCancelDelete = () => {
    setShowDeleteConfirmation(false);
  };

  const handleConfirmDelete = () => {
    handleDeleteButton();
    setShowDeleteConfirmation(false);
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleLogoutButton();
    }
  };

  const handleHistoryToggle = () => {
    setIsHistoryExpanded(!isHistoryExpanded);
  };

  const getIcon = (type) => {
    switch (type) {
      case 'BUS':
        return busIcon;
      case 'TRAIN':
        return trainIcon;
      default:
        return everythingIcon;
    }
  };

  async function getHistory() {
    const response = await makeQuerry('history');
    switch (response.status) {
      case 200:
        setHistory(response.body);
        break;
      default:
        break;
    }
  }

  useEffect(() => {
    getHistory();
  }, []);

  if (!status) {
    navigate('/login');
    return <p>redirect</p>;
  }

  return (
    <div className="popup-content main">
      <div className="centered-block">
        <div className="avatar" data-testid="avatar">
          <img src={status.googlePicture || (status.basicPicture ? `data:image/jpeg;base64,${status.basicPicture}` : Ellipse)} alt="Avatar" referrerPolicy="no-referrer" />
        </div>
        <p className="username">
          {t('hello')}
          {' '}
          {status.username}
        </p>
        <button
          type="button"
          onClick={handleLogoutButton}
          onKeyPress={handleKeyPress}
          className="exit-button"
        >
          <img
            src={Frame}
            alt="Exit"
            className="exit-icon"
          />
        </button>
        {showDeleteConfirmation && (
          <DeleteConfirmationPopup
            onCancel={handleCancelDelete}
            onConfirm={handleConfirmDelete}
            data-testid="delete-confirmation-popup"
          />
        )}
      </div>
      <div className="notification-wrapper">
        <p className="notification-text">
          Бажаєте отримувати сповіщення про наявність квитків?
        </p>
        <label className="switch">
          <input type="checkbox" checked={notificationEnabled} onChange={() => setNotificationEnabled(!notificationEnabled)} />
          <span className="slider round" />
        </label>
      </div>
      <div className="contact-wrapper">
        <p className="contact-text">Акаунт</p>
        <div className="contact-item">
          <div className="column">
            <img src={Email} className="contact-icon" alt="Email" />
            <p className="contact-info-two email-info">Електронна пошта</p>
            <div className="contact-info-data">
              misha@gmail.com
            </div>
          </div>
          <div className="column">
            <img src={Phone} className="contact-icon" alt="Phone" />
            <p className="contact-info-two phone-info">Номер телефону</p>
            <div className="contact-info-data">
              Додати
            </div>
          </div>
          <div className="column">
            <img src={Address} className="contact-icon" alt="Dia" />
            <p className="contact-info-two actions-info">Керування обліковим записом</p>
            <p>
              <Link
                to="/change-password"
                className="change-password"
                data-testid="change-password-link"
              >
                {t('change-password')}
              </Link>
            </p>
            <button
              type="button"
              className="delete-account"
              onClick={handleDeleteAccount}
              data-testid="delete-account-button"
            >
              {t('delete-account')}
            </button>
          </div>
        </div>
      </div>
      <div
        className={`history-wrapper ${isHistoryExpanded ? 'expanded' : ''}`}
        onClick={handleHistoryToggle}
        role="button"
        tabIndex={0}
        onKeyPress={(event) => {
          if (event.key === 'Enter') {
            handleHistoryToggle();
          }
        }}
      >
        <img src={History} alt="History" className="new-icon" />
        <p className="history-text">Історія</p>
        <div className={`history-arrow ${isHistoryExpanded ? 'arrow-down' : ''}`} />
      </div>
      {isHistoryExpanded && (
        <div className="history-content">
          {history.length > 0
            ? history.map((historyItem) => (
              <div key={uuidv4()} className="history-item">
                <span className="history-date">{historyItem.date}</span>
                <img src={getIcon(historyItem.type)} alt="Transport icon" className="history-icon" />
                <span className="history-from">{historyItem.cityFrom}</span>
                <div className="history-central-column">
                  <div className="history-departure-date">{historyItem.departureDate}</div>
                  <hr className="history-line" />
                </div>
                <span className="history-to">{historyItem.cityTo}</span>
              </div>
            )) : <img className="ticket-price__loading" src={loaderIcon} alt="Loading..." />}
        </div>
      )}
    </div>
  );
}

export default Popup;
