/* eslint-disable no-shadow */
/* eslint-disable import/no-cycle */
import Cookies from 'universal-cookie';
import makeQuerry from './querry';

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
  if (response.status === 302) {
    console.log('Location', response.headers.get('Location'));
    makeQuerry(response.headers.get('Location') ?? 'logout', undefined, undefined, 'GET').then((response) => {
      switch (response.status) {
        case 200:
          localStorage.removeItem('JWTtoken');
          cookies.remove('rememberMe');
          cookies.remove('USER_ID');
          break;
        default:
          break;
      }
    });
  }
}
