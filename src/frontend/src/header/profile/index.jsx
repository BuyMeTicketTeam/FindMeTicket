import React, { useState } from 'react';
import './profile.scss';
import { useTranslation } from 'react-i18next';
import Cookies from 'universal-cookie';
import { Link } from 'react-router-dom';
import makeQuerry from '../../helper/querry';
import DeleteConfirmationPopup from '../deletePopup';

function Popup({
  setIsProfilePopup,
  updateAuthValue,
  status,
}) {
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const cookies = new Cookies(null, { path: '/' });
  const { t } = useTranslation('translation', { keyPrefix: 'profile' });

  function handleLogoutButton() {
    makeQuerry('logout').then((response) => {
      switch (response.status) {
        case 200:
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
    makeQuerry('delete-user', null, null, 'DELETE').then((response) => {
      switch (response.status) {
        case 200:
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

  const handleAvatarClick = () => {
    const fileInput = document.getElementById('avatarInput');
    fileInput.click();
  };

  const handleAvatarKeyDown = (event) => {
    if (event.key === 'Enter') {
      handleAvatarClick();
    }
  };

  const handleDeleteAccount = () => {
    setShowDeleteConfirmation(true);
  };

  const handleCancelDelete = () => {
    setShowDeleteConfirmation(false);
  };

  const handleConfirmDelete = () => {
    handleDeleteButton();
    console.log('Deleting account...');
    setShowDeleteConfirmation(false);
  };

  return (
    <div className="popup open">
      <div className="popup-content">
        <button type="button" className="close-button" onClick={() => setIsProfilePopup(false)}>
          &#10006;
        </button>
        <div
          className="avatar"
          tabIndex="0"
          role="button"
          onClick={handleAvatarClick}
          onKeyDown={handleAvatarKeyDown}
          data-testid="avatar"
        >
          <img src={(status.basicPicture ? `data:image/jpeg;base64,${status.basicPicture}` : 'URL_ПО_УМОЛЧАНИЮ')} alt="Avatar" />
        </div>
        <div className="custom-input" data-testid="custom-input">
          {t('hello')}
          {' '}
          {status.username}
        </div>
        <p>
          <Link to="/change-password" className="change-password" onClick={() => setIsProfilePopup(false)} data-testid="change-password-link">{t('change-password')}</Link>
        </p>
        <a href="https://www.freeprivacypolicy.com/live/de2f7052-4f8d-42a4-ab02-0595dc2b02ac" className="privacy-policy-link" target="_blank" rel="noopener noreferrer" data-testd="privacy-policy-link">
          {t('privacy-policy')}
        </a>

        <button type="button" className="delete-account" onClick={handleDeleteAccount} data-testid="delete-account-button">
          {t('delete-account')}
        </button>

        <button type="button" className="exit" onClick={() => { setIsProfilePopup(false); handleLogoutButton(); }} data-testid="exit-button">{t('exit')}</button>

        {showDeleteConfirmation && (
        <DeleteConfirmationPopup
          onCancel={handleCancelDelete}
          onConfirm={handleConfirmDelete}
          data-testid="delete-confirmation-popup"
        />
        )}

      </div>
    </div>

  );
}

export default Popup;
