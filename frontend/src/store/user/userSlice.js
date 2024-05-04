/* eslint-disable no-unused-vars */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import { userApi } from '../../services/userApi';

export const initialState = {
  isAuthenticated: false,
  username: null,
  userPhoto: null,
  userEmail: null,
  notification: false,
};

function setUser(state, action) {
  console.log(state);
  console.log(action);
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
    builder.addMatcher(userApi.endpoints.logout.matchPending, logoutUser);
    builder.addMatcher(userApi.endpoints.deleteUser.matchFulfilled, logoutUser);
    builder.addMatcher(userApi.endpoints.notificationEnable.matchFulfilled, (state) => {
      state.notification = true;
    });
    builder.addMatcher(userApi.endpoints.notificationDisable.matchFulfilled, (state) => {
      state.notification = false;
    });
  },
});

export default slice.reducer;

export const { initUsers } = slice.actions;

export const selectIsAuthenticated = (state) => state.user.isAuthenticated;
