/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import { userApi } from '../../services/userApi';

const initialState = {
  isAuthenticated: false,
  username: null,
  userPhoto: null,
  userEmail: null,
  notification: false,
};

function userRequest(state, action) {
  state.isAuthenticated = true;
  state.username = action.payload.username;
  state.notification = action.payload.notification;
  state.userEmail = action.payload.email;
  state.userPhoto = action.payload.googlePicture ?? action.payload.basicPicture;
}

const slice = createSlice({
  name: 'user',
  initialState,
  extraReducers: (builder) => {
    builder.addMatcher(userApi.endpoints.login.matchFulfilled, userRequest);
    builder.addMatcher(userApi.endpoints.loginGoogle.matchFulfilled, userRequest);
    builder.addMatcher(userApi.endpoints.loginFacebook.matchFulfilled, userRequest);
  },
});

export default slice.reducer;

export const selectIsAuthenticated = (state) => state.user.isAuthenticated;
