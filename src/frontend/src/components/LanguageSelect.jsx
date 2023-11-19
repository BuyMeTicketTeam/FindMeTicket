import React from 'react';

export default function LanguageSelect({ language, changeLanguage }) {
  return (
    <select className="select" value={language} onChange={(e) => changeLanguage(e.target.value)}>
      <option value="Ua">UA</option>
      <option value="Eng">ENG</option>
    </select>
  );
}
