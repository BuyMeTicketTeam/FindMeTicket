/* eslint-disable import/prefer-default-export */
/* eslint-disable import/no-extraneous-dependencies */
import { configureStore } from '@reduxjs/toolkit';
import user from './user/userSlice';

export const store = configureStore({
  reducer: {
    user,
  },
});
