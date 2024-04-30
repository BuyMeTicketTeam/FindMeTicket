/* eslint-disable no-unused-vars */
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

function setUser(state, action) {
  state.isAuthenticated = true;
  state.username = action.payload.username;
  state.notification = action.payload.notification;
  state.userEmail = action.payload.email;
  state.userPhoto = action.payload.googlePicture ?? `data:image/jpeg;base64,${action.payload.basicPicture}`;
}

function logoutUser() {
  return initialState;
}

const slice = createSlice({
  name: 'user',
  initialState,
  extraReducers: (builder) => {
    builder.addMatcher(userApi.endpoints.login.matchFulfilled, setUser);
    builder.addMatcher(userApi.endpoints.loginGoogle.matchFulfilled, setUser);
    builder.addMatcher(userApi.endpoints.loginFacebook.matchFulfilled, setUser);
    builder.addMatcher(userApi.endpoints.logout.matchFulfilled, logoutUser);
    builder.addMatcher(userApi.endpoints.deleteUser.matchFulfilled, logoutUser);
  },
});

export default slice.reducer;

export const { initUsers } = slice.actions;

export const selectIsAuthenticated = (state) => state.user.isAuthenticated;
