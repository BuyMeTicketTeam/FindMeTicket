import { api } from './api';

export const typeAheadAPI = api.injectEndpoints({
  endpoints: (builder) => ({
    typeAheadAPI: builder.query({
      query: (startLetters) => ({
        url: '/cities/typeahead',
        method: 'GET',
        params: { startLetters },
      }),
    }),
  }),
});

export const {
  useLazyTypeAheadAPIQuery,
} = typeAheadAPI;
