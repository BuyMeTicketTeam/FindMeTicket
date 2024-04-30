/* eslint-disable max-len */
/* eslint-disable import/prefer-default-export */
/* eslint-disable import/no-extraneous-dependencies */
import { configureStore } from '@reduxjs/toolkit';
import { api } from '../services/api';
import user from './user/userSlice';
import { authListenerMiddleware } from './middleware/authMiddleware';

const userDataFromStorage = localStorage.getItem('userData') ?? sessionStorage.getItem('userData');
const initUserState = userDataFromStorage ? JSON.parse(userDataFromStorage) : {};

export const store = configureStore({
  reducer: {
    [api.reducerPath]: api.reducer,
    user,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware()
    .concat(api.middleware, authListenerMiddleware.middleware),
  preloadedState: {
    user: initUserState,
  },
});
