import React from 'react';

export default function Error() {
  return (
    <div className="main-error">
      <img className="main-error__img" src="../img/error.svg" alt="Error" />
      <h2 className="main-error__title">Виникла помилка</h2>
      <p className="main-error__text">Просимо вибачення за незручності, спробуйте ще раз</p>
    </div>
  );
}
