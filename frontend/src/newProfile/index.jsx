import React, { useState } from 'react';
import './profile.scss';
import { useTranslation } from 'react-i18next';
import Cookies from 'universal-cookie';
import { useNavigate } from 'react-router-dom';
import makeQuerry from '../helper/querry';
import DeleteConfirmationPopup from '../header/deletePopup';
import Frame from './Frame.svg';
import BusIcon from './bus.svg';
import History from './history.svg';
import Email from './email.svg';
import Phone from './phone.svg';
import Address from './address.svg';

function Popup({
  // setIsProfilePopup,
  updateAuthValue,
  status,
}) {
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const cookies = new Cookies(null, { path: '/' });
  const [notificationEnabled, setNotificationEnabled] = useState(false);
  const [isHistoryExpanded, setIsHistoryExpanded] = useState(false);
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

  // const handleDeleteAccount = () => {
  //   setShowDeleteConfirmation(true);
  // };

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

  if (!status) {
    navigate('/login');
    return <p>redirect</p>;
  }
  return (
    <div className="popup-content main">
      <div className="centered-block">
        <div className="avatar" data-testid="avatar">
          <img src={status.googlePicture || (status.basicPicture ? `data:image/jpeg;base64,${status.basicPicture}` : '')} alt="Avatar" referrerPolicy="no-referrer" />
        </div>
        <p className="username">
          {' '}
          {t('hello')}
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
        <p className="contact-text">Контакти</p>
        <div className="contact-item">
          <img src={Email} className="contact-icon" alt="Email" />
          <p className="contact-info-two email-info">Електронна пошта</p>
          <img src={Address} className="contact-icon" alt="Dia" />
          <p className="contact-info-two actions-info">Дії</p>
          <img src={Phone} className="contact-icon" alt="Phone" />
          <p className="contact-info-two phone-info">Номер телефону</p>
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
          <div className="history-item">
            <span className="history-date">12.02.2024</span>
            <img src={BusIcon} alt="Bus" className="history-icon" />
            <span className="history-from">Київ</span>
            <hr className="history-line" />
            <span className="history-to">Дніпро</span>
          </div>
          <div className="history-item">
            <span className="history-date">12.02.2024</span>
            <img src={BusIcon} alt="Bus" className="history-icon" />
            <span className="history-from">Київ</span>
            <hr className="history-line" />
            <span className="history-to">Харків</span>
          </div>
          <div className="history-item">
            <span className="history-date">12.02.2024</span>
            <img src={BusIcon} alt="Bus" className="history-icon" />
            <span className="history-from">Київ</span>
            <hr className="history-line" />
            <span className="history-to">Херсон</span>
          </div>
        </div>
      )}

      {/* <p>
          <Link to="/change-password"
           className="change-password" onClick={() =>
            setIsProfilePopup(false)} data-testid="change-password-link">
            {t('change-password')}</Link>
        </p>

        <button type="button" className="delete-account"
        onClick={handleDeleteAccount} data-testid="delete-account-button">
          {t('delete-account')}
        </button> */}
    </div>
  );
}

export default Popup;
