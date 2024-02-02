/* eslint-disable import/no-extraneous-dependencies */
import { useEffect, useState } from 'react';
import Cookies from 'universal-cookie';

function useAuthCheck() {
  const [auth, setAuth] = useState(null);
  const cookies = new Cookies(null, { path: '/' });
  const authStoredValue = sessionStorage.getItem('auth');
  const authValueCookie = cookies.get('rememberMe');
  useEffect(() => {
    if (authStoredValue) {
      setAuth(authStoredValue);
      return;
    }
    if (authValueCookie) {
      setAuth(authValueCookie);
    }
  }, []);
  function updateAuthValue(value) {
    setAuth(value);
    sessionStorage.setItem('auth', value);
  }
  return { auth, updateAuthValue };
}

export default useAuthCheck;
