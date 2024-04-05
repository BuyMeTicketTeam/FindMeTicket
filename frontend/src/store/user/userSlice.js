/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import { userApi } from '../../services/userApi';

const initialState = {
  isAuthenticated: false,
  username: null,
  userPhoto: null,
  userEmail: null,
  token: null,
};

const slice = createSlice({
  name: 'user',
  initialState,
  extraReducers: (builder) => {
    builder.addMatcher(userApi.endpoints.login.matchFulfilled, (state, action) => {
      state.isAuthenticated = true;
      state.token = action.payload.token;
    });
  },
});

export default slice.reducer;

export const selectIsAuthenticated = (state) => state.user.isAuthenticated;
