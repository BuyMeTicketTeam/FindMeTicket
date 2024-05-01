/* eslint-disable import/no-extraneous-dependencies */
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import Popup from 'reactjs-popup';
import { useDebounce } from 'use-debounce';
import { Link } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';

import {
  useDeleteUserMutation,
  useGetHistoryQuery,
  useLazyLogoutQuery,
  useLazyNotificationDisableQuery,
  useLazyNotificationEnableQuery,
} from '../../services/userApi';

import AvatarPopup from './AvatarPopup';
import DeleteConfirmationModal from './DeleteConfirmModal';

import rank1 from '../../images/ranks/rank1.png';
import mark from '../../images/mark.svg';
import logoutImg from '../../images/logout.svg';
import emailIcon from '../../images/email.svg';
import phoneIcon from '../../images/phone.svg';
import addressIcon from '../../images/address.svg';
import historyIcon from '../../images/history.svg';
import loaderIcon from '../../images/spinning-loading.svg';
import { busIcon, trainIcon, everythingIcon } from '../../images/transport-img/img';

import './profile.scss';

export default function Profile() {
  const [openAvatarModal, setOpenAvatarModal] = useState(false);
  const closeAvatarModal = () => setOpenAvatarModal(false);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const {
    username, userPhoto, userEmail, notification,
  } = useSelector((state) => state.user);
  const [notificationState, setNotificationState] = useState(notification);
  const [isHistoryExpanded, setIsHistoryExpanded] = useState(false);
  const [notificationValue] = useDebounce(notificationState, 2000);
  const { data: history, isLoading } = useGetHistoryQuery();
  const [logout] = useLazyLogoutQuery();
  const [
    enableNotification, { isError: enableNotificationError },
  ] = useLazyNotificationEnableQuery();
  const [
    disableNotification, { isError: disableNotificationError },
  ] = useLazyNotificationDisableQuery();
  const [deleteUser, { isError: deleteError }] = useDeleteUserMutation();
  // const [showNotificationAlert, callNotificationAlert] = useAlert();
  const { t } = useTranslation('translation', { keyPrefix: 'profile' });

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

  useEffect(() => {
    if (notificationValue) {
      enableNotification();
    } else {
      disableNotification();
    }
  }, [notificationValue]);

  useEffect(() => {
    setNotificationState(notification);
  }, [enableNotificationError, disableNotificationError]);

  return (
    <div className="popup-content main">
      <div className="error-container">
        {enableNotificationError && <p className="error">Помилка активації повідомлень</p>}
        {disableNotificationError && <p className="error">{t()}</p>}
        {deleteError && <p className="error">{t()}</p>}
      </div>
      <div className="centered-block">
        <button
          className="avatar"
          data-testid="avatar"
          onClick={() => setOpenAvatarModal(true)}
          type="button"
        >
          <img src={rank1} alt="rank1" className="image-rank1-profile" />
          <img src={userPhoto} alt="Avatar" referrerPolicy="no-referrer" className="ava" />
        </button>
        <p className="username">
          {t('hello')}
          {' '}
          {username}
          {' '}
          <div className="Lvl">Lvl 1</div>
          <Popup
            trigger={<img src={mark} alt="mark" className="mark" />}
            position="right center"
            on={['hover']}
          >
            {t('lvl')}
          </Popup>
        </p>
        <button
          type="button"
          onClick={() => logout()}
          className="exit-button"
        >
          <img
            src={logoutImg}
            alt="Exit"
            className="exit-icon"
          />
        </button>
      </div>
      <div className="notification-wrapper">
        <p className="notification-text">
          {t('notice')}
        </p>
        <label htmlFor="notification" className="switch">
          <input
            id="notification"
            type="checkbox"
            checked={notificationState}
            onChange={() => setNotificationState((prevState) => !prevState)}
          />
          <span className="slider round" />
        </label>
      </div>
      <div className="contact-wrapper">
        <p className="contact-text">{t('account')}</p>
        <div className="contact-item">
          <div className="column">
            <div className="column-header">
              <div className="email-two">
                <img src={emailIcon} className="contact-icon" alt="Email" />
                <p className="contact-info-two email-info">{t('email')}</p>
              </div>
            </div>
            <div className="contact-info-data">
              <div className="email-data">
                {userEmail}
              </div>
            </div>
          </div>
          <div className="column">
            <div className="column-header">
              <img src={phoneIcon} className="contact-icon phone" alt="Phone" />
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
              <img src={addressIcon} className="contact-icon" alt="Dia" />
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
                onClick={() => setOpenDeleteModal(true)}
                data-testid="delete-account-button"
              >
                {t('delete-account')}
              </button>
            </div>
          </div>
        </div>
      </div>
      <button
        className={`history-wrapper ${isHistoryExpanded ? 'expanded' : ''}`}
        onClick={() => setIsHistoryExpanded(!isHistoryExpanded)}
        type="button"
      >
        <img src={historyIcon} alt="History" className="new-icon" />
        <p className="history-text">{t('history')}</p>
        <div className={`history-arrow ${isHistoryExpanded ? 'arrow-down' : ''}`} />
      </button>
      {isHistoryExpanded && (
        <div className="history-content">
          {isLoading && <img className="ticket-price__loading" src={loaderIcon} alt="Loading..." />}
          {(history.length > 0 && !isLoading)
            ? history.map((historyItem) => (
              <Link to="/" key={uuidv4()} className="history-item">
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
      <AvatarPopup open={openAvatarModal} closeModal={closeAvatarModal} userAvatar={userPhoto} />
      <DeleteConfirmationModal
        open={openDeleteModal}
        closeModal={() => setOpenDeleteModal(false)}
        deleteUser={deleteUser}
      />
    </div>
  );
}
