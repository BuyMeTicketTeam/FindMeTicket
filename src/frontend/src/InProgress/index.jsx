import React from 'react';
import Button from '../utils/Button';
import './inProgress.css';

export default function index({ title, text, setIsOpen }) {
  return (
    <div className="message background">
      <div className="message__content">
        <div className="message__header">
          <h2 className="message__title">{title}</h2>
          <button
            type="button"
            className="message__close close"
            aria-label="Close"
            onClick={() => setIsOpen(false)}
          />
        </div>
        <p className="message__text">{text}</p>
        <Button className="message__button" name="Зрозуміло" onButton={() => setIsOpen(false)} />
      </div>
    </div>
  );
}
