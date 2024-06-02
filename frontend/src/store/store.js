/* eslint-disable import/named */
/* eslint-disable max-len */
/* eslint-disable import/prefer-default-export */
/* eslint-disable import/no-extraneous-dependencies */
import { configureStore } from '@reduxjs/toolkit';
import { api } from '../services/api';
import user, { initialState as userInitialState } from './user/userSlice';
import tickets from './tickets/ticketsSlice';
import { userListenerMiddleware } from './middleware/userMiddleware';

const userDataFromStorage = localStorage.getItem('userData') ?? sessionStorage.getItem('userData');
const initUserState = userDataFromStorage ? JSON.parse(userDataFromStorage) : userInitialState;

export const store = configureStore({
  reducer: {
    [api.reducerPath]: api.reducer,
    user,
    tickets,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware()
    .concat(api.middleware, userListenerMiddleware.middleware),
  preloadedState: {
    user: initUserState,
  },
});
