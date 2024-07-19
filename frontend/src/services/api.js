/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/prefer-default-export */
import { createApi, fetchBaseQuery, retry } from '@reduxjs/toolkit/query/react';
import { getI18n } from 'react-i18next';

const baseQuery = fetchBaseQuery({
  baseUrl: `${process.env.REACT_APP_SERVER_ADDRESS}`,
  redirect: 'follow',
  credentials: 'include',
  prepareHeaders: (headers) => {
    const token = localStorage.getItem('JWTtoken');

    if (token) {
      headers.set('Authorization', token);
    }

    headers.set('Content-Language', getI18n().language.toLowerCase());

    return headers;
  },
  responseHandler: async (response) => {
    if (response.headers.has('Authorization')) {
      localStorage.setItem('JWTtoken', response.headers.get('Authorization'));
    }
    return response.json();
  },
});

const baseQueryWithRetry = retry(baseQuery, { maxRetries: 0 });

export const api = createApi({
  reducerPath: 'splitApi',
  baseQuery: baseQueryWithRetry,
  refetchOnMountOrArgChange: true,
  endpoints: () => ({}),
});
