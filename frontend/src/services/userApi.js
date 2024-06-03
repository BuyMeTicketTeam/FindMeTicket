import Cookies from 'universal-cookie';
import { api } from './api';

const cookies = new Cookies(null, { path: '/' });

async function loginResponseHandler(response) {
  if (response.headers.has('Authorization')) {
    localStorage.setItem('JWTtoken', response.headers.get('Authorization'));
  }

  const parsedJSON = await response.json();
  if (response.headers.has('rememberme')) {
    const userData = {
      isAuthenticated: true,
      username: parsedJSON.username,
      notification: parsedJSON.notification,
      userEmail: parsedJSON.email,
      userPhoto: parsedJSON.socialMediaAvatar ?? `data:image/jpeg;base64,${parsedJSON.defaultAvatar}`,
    };
    localStorage.setItem('userData', JSON.stringify(userData));
  }

  return parsedJSON;
}

export const userApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (userData) => ({
        url: '/auth/sign-in',
        method: 'POST',
        body: userData,
        responseHandler: loginResponseHandler,
      }),
    }),
    loginGoogle: builder.mutation({
      query: (token) => ({
        url: '/auth/sign-in/google',
        method: 'POST',
        body: token,
      }),
    }),
    loginFacebook: builder.mutation({
      query: (token) => ({
        url: '/auth/sign-in/facebook',
        method: 'POST',
        body: token,
      }),
    }),
    register: builder.mutation({
      query: (userData) => ({
        url: '/auth/sign-up',
        method: 'POST',
        body: userData,
        validateStatus: (response) => {
          console.log(response);
          return response.status === 200 && response.statusText === 'User registered successfully.';
        },
      }),
    }),
    confirm: builder.mutation({
      query: (code) => ({
        url: '/users/verify',
        method: 'POST',
        body: code,
        responseHandler: 'text',
      }),
    }),
    resendConfirmToken: builder.mutation({
      query: (email) => ({
        url: '/users/verification-code/send',
        method: 'POST',
        body: email,
      }),
    }),
    reset: builder.mutation({
      query: (email) => ({
        url: '/users/reset-code/send',
        method: 'POST',
        body: email,
        responseHandler: 'text',
      }),
    }),
    newPassword: builder.mutation({
      query: (userData) => ({
        url: `/users/${cookies.get('USER_ID')}/password/reset`,
        method: 'PATCH',
        body: userData,
        responseHandler: 'text',
      }),
    }),
    changePassword: builder.mutation({
      query: (passwords) => ({
        url: `/users/${cookies.get('USER_ID')}/password/update`,
        method: 'PATCH',
        body: passwords,
      }),
    }),
    logout: builder.query({
      query: () => ({
        url: '/logout',
        method: 'GET',
      }),
    }),
    deleteUser: builder.mutation({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}`,
        method: 'DELETE',
      }),
    }),
    getHistory: builder.query({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/history`,
        method: 'GET',
      }),
    }),
    notificationEnable: builder.query({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/notifications/on`,
        method: 'GET',
        responseHandler: 'text',
      }),
    }),
    notificationDisable: builder.query({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/notifications/off`,
        method: 'GET',
        responseHandler: 'text',
      }),
    }),
  }),
});

export const {
  useLoginMutation,
  useLoginFacebookMutation,
  useLoginGoogleMutation,
  useRegisterMutation,
  useConfirmMutation,
  useResendConfirmTokenMutation,
  useResetMutation,
  useNewPasswordMutation,
  useDeleteUserMutation,
  useLazyLogoutQuery,
  useLazyNotificationDisableQuery,
  useLazyNotificationEnableQuery,
  useGetHistoryQuery,
  useChangePasswordMutation,
} = userApi;

export const {
  endpoints: {
    login, loginFacebook, loginGoogle, register, confirm,
    resendConfirmToken, reset, newPassword, getHistory,
    logout, notificationDisable, notificationEnable, deleteUser,
  },
} = userApi;
