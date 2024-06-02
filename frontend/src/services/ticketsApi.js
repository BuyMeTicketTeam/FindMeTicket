import { api } from './api';
import eventSourceQuery from '../helper/eventSourceQuery';

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
      query: () => '/tickets/search',
      async onCacheEntryAdded(arg, { updateCachedData, cacheEntryRemoved }) {
        try {
          console.log('query');
          // wait for the initial query to resolve before proceeding
          // await cacheDataLoaded;

          console.log('query2');

          // when data is received from the socket connection to the server,
          // if it is a message and for the appropriate channel,
          // update our query result with the received message
          const listener = (event) => {
            const data = JSON.parse(event.data);

            updateCachedData((draft) => {
              draft.push(data);
            });
          };

          eventSourceQuery('/tickets/search', arg, listener);
        } catch {
          // no-op in case `cacheEntryRemoved` resolves before `cacheDataLoaded`,
          // in which case `cacheDataLoaded` will throw
        }
        // cacheEntryRemoved will resolve when the cache subscription is no longer active
        await cacheEntryRemoved;
        // perform cleanup steps once the `cacheEntryRemoved` promise resolves
      },
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
