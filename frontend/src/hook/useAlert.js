import { useState } from 'react';

export default function useAlert(time = 1000) {
  const [showAlert, setShowAlert] = useState(false);

  function callAlert() {
    showAlert(true);
    setTimeout(() => setShowAlert(false), time);
  }

  return [showAlert, callAlert];
}
