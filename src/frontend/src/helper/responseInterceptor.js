import Cookies from 'universal-cookie';

export default function responseInterceptor(response) {
  const cookies = new Cookies(null, { path: '/' });
  if (response.status === 200) {
    response.headers.forEach((value, key) => {
      if (key === 'authorization') {
        localStorage.setItem('JWTtoken', value);
      }

      if (key === 'rememberme') {
        cookies.set('rememberMe', 'true', { maxAge: 260000 * 60 * 1000 });
      }

      if (key === 'userid') {
        cookies.set('USER_ID', value);
      }
    });
  }
}
