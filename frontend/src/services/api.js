/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/prefer-default-export */
import { createApi, fetchBaseQuery, retry } from '@reduxjs/toolkit/query/react';
import Cookies from 'universal-cookie';

const cookies = new Cookies(null, { path: '/' });

const baseQuery = fetchBaseQuery({
  baseUrl: `${process.env.REACT_APP_SERVER_ADDRESS}`,
  prepareHeaders: (headers) => {
    const token = localStorage.getItem('JWTtoken');

    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  },
  responseHandler: async (response) => {
    const parsedJSON = await response.json();
    if (response.headers.has('Authorization')) {
      localStorage.setItem('JWTtoken', response.headers.get('Authorization'));
    }

    if (response.headers.has('rememberme')) {
      localStorage.setItem('userData', JSON.stringify(parsedJSON));
    }

    if (response.headers.has('user_id')) {
      cookies.set('USER_ID', response.headers.get('user_id'));
    }

    return parsedJSON;
  },
});

const baseQueryWithRetry = retry(baseQuery, { maxRetries: 0 });

export const api = createApi({
  reducerPath: 'splitApi',
  baseQuery: baseQueryWithRetry,
  refetchOnMountOrArgChange: true,
  endpoints: () => ({}),
});
