import Cookies from 'universal-cookie';

export default function responseInterceptor(response, bodyResponse) {
  const cookies = new Cookies(null, { path: '/' });
  if (response.status === 200) {
    response.headers.forEach((value, key) => {
      if (key === 'authorization') {
        localStorage.setItem('JWTtoken', value);
      }

      if (key === 'rememberme') {
        cookies.set('rememberMe', bodyResponse, { maxAge: 260000 * 60 * 1000 });
      }

      if (key === 'user_id') {
        cookies.set('USER_ID', value);
      }
    });
  }
}
