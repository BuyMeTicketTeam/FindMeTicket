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
  }),
});

export const {
  useLoginMutation,
  useLoginFacebookMutation,
  useLoginGoogleMutation,
} = userApi;

export const {
  endpoints: { login, loginFacebook, loginGoogle },
} = userApi;
