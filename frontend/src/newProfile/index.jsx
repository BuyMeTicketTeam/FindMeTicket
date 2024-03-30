/* eslint-disable no-shadow */
/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useRef, useState } from 'react';
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
  const [loading, setLoading] = useState(false);
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

  const getIcon = (historyData) => {
    if (historyData.bus && historyData.train) {
      return everythingIcon;
    }
    if (historyData.bus) {
      return busIcon;
    }
    if (historyData.train) {
      return trainIcon;
    }
    return everythingIcon;
  };

  async function getHistory() {
    setLoading(true);
    const response = await makeQuerry('getHistory', undefined, undefined, 'GET');
    setLoading(false);
    switch (response.status) {
      case 200:
        setHistory(response.body);
        break;
      default:
        break;
    }
  }

  function defineRoute(historyData) {
    let request = `/?from=${historyData.departureCity}&to=${historyData.arrivalCity}&endpoint=1`;
    if (+new Date() <= Date.parse(historyData.departureDate)) {
      request += `&departureDate=${Date.parse(historyData.departureDate)}`;
    }
    if (historyData.bus && historyData.train) {
      request += '&type=all';
    } else if (historyData.bus) {
      request += '&type=bus';
    } else if (historyData.train) {
      request += '&type=train';
    } else {
      request += '&type=all';
    }
    return request;
  }

  useEffect(() => {
    getHistory();
  }, []);

  useEffect(() => {
    console.log(status);
    if (status) {
      setNotificationEnabled(status.notification);
    }
  }, [status]);

  const timerId = useRef();
  const sendNotification = async (notificationEnabled) => {
    clearInterval(timerId.current);
    await new Promise(() => {
      timerId.current = setTimeout(() => {
        if (notificationEnabled) {
          makeQuerry('notifications/enable', undefined, undefined, 'GET');
        } else {
          makeQuerry('notifications/disable', undefined, undefined, 'GET');
        }
      }, 2000);
    });
  };

  useEffect(() => {
    sendNotification(notificationEnabled);
  }, [notificationEnabled]);

  if (!status) {
    navigate('/login', { state: { navigate: '/' } });
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
          {t('notice')}
        </p>
        <label className="switch">
          <input
            type="checkbox"
            checked={notificationEnabled}
            onChange={() => setNotificationEnabled(() => {
              updateAuthValue({ ...status, notification: !notificationEnabled });
              return !notificationEnabled;
            })}
          />
          <span className="slider round" />
        </label>
      </div>
      <div className="contact-wrapper">
        <p className="contact-text">{t('account')}</p>
        <div className="contact-item">
          <div className="column">
            <div className="column-header">
              <img src={Email} className="contact-icon" alt="Email" />
              <p className="contact-info-two email-info">{t('email')}</p>
            </div>
            <div className="contact-info-data">
              {status.email}
            </div>
          </div>
          <div className="column">
            <div className="column-header">
              <img src={Phone} className="contact-icon phone" alt="Phone" />
              <p className="contact-info-two phone-info">{t('phone')}</p>
            </div>
            <div className="contact-info-data">
              <button
                type="button"
                className="custom-button"
              >
                {t('add')}
              </button>

            </div>
          </div>
          <div className="column">
            <div className="column-header">
              <img src={Address} className="contact-icon" alt="Dia" />
              <p className="contact-info-two actions-info">{t('account-management')}</p>
            </div>
            <div className="contact-info-data">
              <Link
                type="button"
                to="/change-password"
                className="change-password"
                data-testid="change-password-link"
              >
                {t('change-password')}
              </Link>
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
        <p className="history-text">{t('history')}</p>
        <div className={`history-arrow ${isHistoryExpanded ? 'arrow-down' : ''}`} />
      </div>
      {isHistoryExpanded && (
        <div className="history-content">
          {loading && <img className="ticket-price__loading" src={loaderIcon} alt="Loading..." />}
          {(history.length > 0 && !loading)
            ? history.map((historyItem) => (
              <Link to={defineRoute(historyItem)} key={uuidv4()} className="history-item">
                <span className="history-date">{historyItem.addingTime}</span>
                <img src={getIcon(historyItem)} alt="Transport icon" className="history-icon" />
                <span className="history-from">{historyItem.departureCity}</span>
                <div className="history-central-column">
                  <div className="history-departure-date">{historyItem.departureDate}</div>
                  <hr className="history-line" />
                </div>
                <span className="history-to">{historyItem.arrivalCity}</span>
              </Link>
            )) : <div className="history-item no-items">{t('no-items')}</div>}
        </div>
      )}
    </div>
  );
}

export default Popup;
