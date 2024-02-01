/* eslint-disable import/no-extraneous-dependencies */
import { useEffect, useState } from 'react';
import Cookies from 'universal-cookie';

function useAuthCheck() {
  const [auth, setAuth] = useState(false);
  const cookies = new Cookies(null, { path: '/' });
  const authStoredValue = sessionStorage.getItem('auth');
  useEffect(() => {
    if (authStoredValue === 'true') {
      setAuth(authStoredValue);
      return;
    }
    if (cookies.get('rememberMe')) {
      setAuth(true);
    }
  }, []);
  function updateAuthValue(value) {
    setAuth(value);
    sessionStorage.setItem('auth', value);
  }
  return { auth, updateAuthValue };
}

export default useAuthCheck;
