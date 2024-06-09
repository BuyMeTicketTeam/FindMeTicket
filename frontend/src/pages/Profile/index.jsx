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
    <div className="profile main">
      <div className="container type_profile">
        <div className="profile-error-container">
          {enableNotificationError && <p className="error">{t('error_enable_notification')}</p>}
          {disableNotificationError && <p className="error">{t('error_disable_notification')}</p>}
          {deleteError && <p className="error">{t('error_delete_user')}</p>}
        </div>
        <div className="profile-block gen-info">
          <button
            className="profile-avatar"
            data-testid="avatar"
            onClick={() => setOpenAvatarModal(true)}
            type="button"
          >
            <img src={rank1} alt="rank1" className="profile-avatar__rank" />
            <img src={userPhoto} alt="Avatar" referrerPolicy="no-referrer" className="profile-avatar__img" />
          </button>
          <p className="gen-info__username">
            {t('hello')}
            {' '}
            {username}
            {' '}
          </p>
          <div className="gen-info-level">
            <div className="gen-info-level__text">Lvl 1</div>
            <Popup
              open
              trigger={<img src={mark} alt="mark" className="gen-info-level__tip-img" />}
              position="right center"
              className="profile-tip"
              on={['hover']}
            >
              {t('lvl')}
            </Popup>
          </div>
          <button
            type="button"
            onClick={() => logout()}
            className="gen-info__exit-button"
          >
            <img
              src={logoutImg}
              alt="Exit"
              className="gen-info__exit-icon"
            />
          </button>
        </div>
        <div className="profile-block profile-notification">
          <p className="profile-notification__text">
            {t('notification')}
          </p>
          <label htmlFor="notification" className="profile-notification-switch">
            <input
              id="notification"
              type="checkbox"
              checked={notificationState}
              onChange={() => setNotificationState((prevState) => !prevState)}
            />
            <span className="profile-notification-switch__slider" />
          </label>
        </div>
        <div className="profile-block profile-contacts">
          <p className="profile-block__title">{t('account')}</p>
          <div className="profile-contacts-group">
            <div className="profile-contacts-group__column">
              <div className="profile-contacts-group__row">
                <img src={emailIcon} className="profile-contacts-group__icon" alt="Email" />
                <p className="profile-contacts-group__text">{t('email')}</p>
              </div>
              <div className="profile-contacts-group__row">
                {userEmail}
              </div>
            </div>
            <div className="profile-contacts-group__column">
              <div className="profile-contacts-group__row content_center">
                <img src={phoneIcon} className="profile-contacts-group__icon type_phone" alt="Phone" />
                <p className="">{t('phone')}</p>
              </div>
              <div className="profile-contacts-group__row content_center">
                <button
                  type="button"
                  disabled
                  className="button"
                >
                  {t('add')}
                </button>
              </div>
            </div>
            <div className="profile-contacts-group__column column_wide">
              <div className="profile-contacts-group__row">
                <img src={addressIcon} className="profile-contacts-group__icon" alt="Dia" />
                <p className="">{t('account_management')}</p>
              </div>
              <div className="profile-contacts-group__row">
                <Link
                  type="button"
                  to="/change-password"
                  className="link"
                  data-testid="change-password-link"
                >
                  {t('change_password')}
                </Link>
                <button
                  type="button"
                  className="button profile-delete-acc"
                  onClick={() => setOpenDeleteModal(true)}
                  data-testid="delete-account-button"
                >
                  {t('delete_account')}
                </button>
              </div>
            </div>
          </div>
        </div>
        <button
          className={`profile-block profile-history-btn ${isHistoryExpanded ? 'expanded' : ''}`}
          onClick={() => setIsHistoryExpanded(!isHistoryExpanded)}
          type="button"
        >
          <img src={historyIcon} alt="History" className="profile-history-btn__icon" />
          <p className="profile-history-btn__text">{t('history')}</p>
          <div className={`profile-history-btn__arrow ${isHistoryExpanded ? 'arrow-down' : ''}`} />
        </button>
        {isHistoryExpanded && (
        <div className="profile-history">
          {isLoading && <img className="loading" src={loaderIcon} alt="Loading..." />}
          {(!isLoading && history && history.length > 0)
            ? history.map((historyItem) => (
              <Link to="/" key={uuidv4()} className="profile-history-item">
                <span className="profile-history-item__search-date">{historyItem.addingTime}</span>
                <img src={getIcon(historyItem)} alt="Transport icon" className="profile-history-item__icon" />
                <span className="profile-history-item__from">{historyItem.departureCity}</span>
                <div className="profile-history-item__column">
                  <div className="profile-history-item__date">{historyItem.departureDate}</div>
                  <hr className="profile-history-item__line" />
                </div>
                <span className="profile-history-item__to">{historyItem.arrivalCity}</span>
              </Link>
            )) : <div className="profile-history-item no-items">{t('no_items')}</div>}
        </div>
        )}
      </div>
      <AvatarPopup open={openAvatarModal} closeModal={closeAvatarModal} userAvatar={userPhoto} />
      <DeleteConfirmationModal
        open={openDeleteModal}
        closeModal={() => setOpenDeleteModal(false)}
        deleteUser={deleteUser}
      />
    </div>
  );
}
