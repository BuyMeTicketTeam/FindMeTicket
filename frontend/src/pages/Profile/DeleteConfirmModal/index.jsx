import React from 'react';
import Popup from 'reactjs-popup';

function DeleteConfirmationModal({ open, closeModal, deleteUser }) {
  return (
    <Popup Popup open={open} onClose={closeModal} position="right center" modal>
      <div className="delete-confirmation-popup">
        <p>Ви дійсно хочете видалити аккаунт?</p>
        <div className="confirmation-buttons">
          <button type="button" onClick={deleteUser}>Так</button>
          <button type="button" onClick={closeModal}>Ні</button>
        </div>
      </div>
    </Popup>
  );
}

export default DeleteConfirmationModal;
