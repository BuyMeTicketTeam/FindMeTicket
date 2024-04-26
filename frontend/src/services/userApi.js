import { api } from './api';

export const userApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (userData) => ({
        url: '/login',
        method: 'POST',
        body: userData,
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
} = userApi;

export const {
  endpoints: {
    login, loginFacebook, loginGoogle, register, confirm,
    resendConfirmToken, reset, newPassword, resendConfirmResetToken,
  },
} = userApi;
