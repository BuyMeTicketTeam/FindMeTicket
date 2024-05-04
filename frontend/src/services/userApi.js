import { api } from './api';

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
      userPhoto: parsedJSON.googlePicture ?? `data:image/jpeg;base64,${parsedJSON.basicPicture}`,
    };
    localStorage.setItem('userData', JSON.stringify(userData));
  }

  return parsedJSON;
}

export const userApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (userData) => ({
        url: '/login',
        method: 'POST',
        body: userData,
        responseHandler: loginResponseHandler,
      }),
    }),
    loginGoogle: builder.mutation({
      query: (token) => ({
        url: '/oauth2/authorize/google',
        method: 'POST',
        body: token,
      }),
    }),
    loginFacebook: builder.mutation({
      query: (token) => ({
        url: '/oauth2/authorize/facebook',
        method: 'POST',
        body: token,
      }),
    }),
    register: builder.mutation({
      query: (userData) => ({
        url: '/register',
        method: 'POST',
        body: userData,
      }),
    }),
    confirm: builder.mutation({
      query: (code) => ({
        url: '/confirm-email',
        method: 'POST',
        body: code,
        responseHandler: 'text',
      }),
    }),
    resendConfirmToken: builder.mutation({
      query: (email) => ({
        url: '/resend/confirm-token',
        method: 'POST',
        body: email,
      }),
    }),
    reset: builder.mutation({
      query: (email) => ({
        url: '/reset-password',
        method: 'POST',
        body: email,
      }),
    }),
    newPassword: builder.mutation({
      query: (userData) => ({
        url: '/new-password',
        method: 'POST',
        body: userData,
      }),
    }),
    resendConfirmResetToken: builder.mutation({
      query: (email) => ({
        url: '/resend/reset-token',
        method: 'POST',
        body: email,
      }),
    }),
    changePassword: builder.mutation({
      query: (passwords) => ({
        url: '/update-password',
        method: 'POST',
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
        url: '/delete-user',
        method: 'DELETE',
      }),
    }),
    getHistory: builder.query({
      query: () => ({
        url: '/getHistory',
        method: 'GET',
        responseHandler: 'text',
      }),
    }),
    notificationEnable: builder.query({
      query: () => ({
        url: '/notifications/enable',
        method: 'GET',
        responseHandler: 'text',
      }),
    }),
    notificationDisable: builder.query({
      query: () => ({
        url: '/notifications/disable',
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
  useResendConfirmResetTokenMutation,
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
    resendConfirmToken, reset, newPassword, resendConfirmResetToken,
    getHistory, logout, notificationDisable, notificationEnable, deleteUser,
  },
} = userApi;