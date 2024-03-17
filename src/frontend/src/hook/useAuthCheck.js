/* eslint-disable import/no-extraneous-dependencies */
import { useEffect, useState } from 'react';
import Cookies from 'universal-cookie';

function useAuthCheck() {
  const [auth, setAuth] = useState(null);
  const cookies = new Cookies(null, { path: '/' });
  const authValueCookie = cookies.get('rememberMe');
  useEffect(() => {
    if (authValueCookie) {
      setAuth(authValueCookie);
    }
  }, []);
  function updateAuthValue(value) {
    setAuth(value);
    if (!cookies.get('rememberMe')) {
      cookies.set('rememberMe', JSON.stringify(value));
    }
  }
  return { auth, updateAuthValue };
}

export default useAuthCheck;
