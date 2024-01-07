import { useEffect, useState } from 'react';

function useAuthCheck() {
  const [auth, setAuth] = useState(false);
  const authStoredValue = sessionStorage.getItem('auth');
  useEffect(() => {
    if (authStoredValue === 'true') {
      setAuth(authStoredValue);
    }
  }, []);
  function updateAuthValue(value) {
    setAuth(value);
    sessionStorage.setItem('auth', value);
  }
  return { auth, updateAuthValue };
}

export default useAuthCheck;
