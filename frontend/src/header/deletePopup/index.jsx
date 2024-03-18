import React from 'react';
import './style.scss';

function DeleteConfirmationPopup({ onCancel, onConfirm }) {
  return (
    <div className="delete-confirmation-popup">
      <p>Ви дійсно хочете видалити аккаунт?</p>
      <div className="confirmation-buttons">
        <button type="button" onClick={() => onConfirm()}>Так</button>
        <button type="button" onClick={() => onCancel()}>Ні</button>
      </div>
    </div>
  );
}

export default DeleteConfirmationPopup;
