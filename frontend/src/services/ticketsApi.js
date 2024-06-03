/* eslint-disable no-await-in-loop */
import { api } from './api';
// import eventSourceQuery from '../helper/eventSourceQuery';

export const ticketsApi = api.injectEndpoints({
  endpoints: (builder) => ({
    sortBy: builder.query({
      query: (searchParams) => ({
        url: '/tickets/sortBy',
        method: 'GET',
        params: searchParams,
      }),
    }),
    searchTickets: builder.query({
      query: (searchParams) => ({
        url: '/tickets/search',
        method: 'GET',
        headers: {
          'Content-Type': 'text/event-stream',
        },
        params: searchParams,
        responseHandler: async (response) => {
          const reader = response.body.getReader();
          let done; let value;
          while (!done) {
            ({ value, done } = await reader.read());
            console.log(value, done);
            searchParams.onChunk(value);
          }
        },
      }),
    }),
  }),
});
export const {
  useLazySortByQuery,
  useLazySearchTicketsQuery,
} = ticketsApi;

export const {
  endpoints: {
    sortBy, searchTickets,
  },
} = ticketsApi;
