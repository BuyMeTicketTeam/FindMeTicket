import React, { useState } from 'react';

export default function LanguageSelect() {
  const [value, changeValue] = useState('Ua');
  return (
    <select className="select" value={value} onChange={(e) => changeValue(e.target.value)}>
      <option value="Ua">Ua</option>
      <option value="Eng">Eng</option>
    </select>
  );
}
