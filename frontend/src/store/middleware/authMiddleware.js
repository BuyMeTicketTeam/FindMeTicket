import { createListenerMiddleware, isAnyOf } from '@reduxjs/toolkit';
import Cookies from 'universal-cookie';
import { userApi } from '../../services/userApi';

const authListenerMiddleware = createListenerMiddleware();

const cookies = new Cookies(null, { path: '/' });

authListenerMiddleware.startListening({
  matcher: isAnyOf(
    userApi.endpoints.logout.matchFulfilled,
    userApi.endpoints.deleteUser.matchFulfilled,
  ),
  effect: () => {
    localStorage.removeItem('JWTtoken');
    localStorage.removeItem('userData');
    sessionStorage.removeItem('userData');
    cookies.remove('USER_ID');
  },
});

authListenerMiddleware.startListening({
  matcher: isAnyOf(
    userApi.endpoints.login.matchFulfilled,
    userApi.endpoints.loginGoogle.matchFulfilled,
    userApi.endpoints.loginFacebook.matchFulfilled,
  ),
  effect: (action) => {
    const userData = {
      isAuthenticated: true,
      username: action.payload.username,
      notification: action.payload.notification,
      userEmail: action.payload.email,
      userPhoto: action.payload.googlePicture ?? `data:image/jpeg;base64,${action.payload.basicPicture}`,
    };
    sessionStorage.setItem('userData', JSON.stringify(userData));
  },
});

export { authListenerMiddleware };
