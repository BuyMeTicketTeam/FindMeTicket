/* eslint-disable no-restricted-syntax */
/* eslint-disable consistent-return */
/* eslint-disable no-await-in-loop */
import { api } from './api';

async function sseResponseHandler(searchPayload, response) {
  const reader = response.body.getReader();
  const decoder = new TextDecoder('utf-8');
  let done = false;
  let value;
  const responseArray = [];
  while (!done) {
    try {
      ({ value, done } = await reader.read());
      if (done) {
        console.log('Stream reading completed');
        break;
      }
      if (value) {
        const jsonString = decoder.decode(value, { stream: true });
        // console.log('Decoded JSON string:', jsonString);
        try {
          const lines = jsonString.split('\n').filter((line) => line.trim() !== '');
          let event = null;
          let data = '';

          for (const line of lines) {
            if (line.startsWith('event:')) {
              event = line.substring(6).trim();
            } else if (line.startsWith('data:')) {
              data += `${line.substring(5).trim()}\n`;
            }
          }

          const parsedData = { data: JSON.parse(data.trim()), event };
          console.log('Parsed data:', parsedData);
          searchPayload.onChunk(parsedData);
          responseArray.push(parsedData);
        } catch (jsonError) {
          console.error('Error parsing JSON:', jsonError, 'Original string:', jsonString);
        }
      }
    } catch (readError) {
      console.error('Error reading from stream:', readError);
      break;
    }
  }
  return responseArray;
}

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
      query: (searchPayload) => ({
        url: '/events',
        method: 'GET',
        params: searchPayload.data,
        responseHandler: (response) => sseResponseHandler(searchPayload, response),
      }),
    }),
    getTicket: builder.query({
      query: (searchPayload) => ({
        url: `/tickets/${searchPayload.ticketId}`,
        method: 'GET',
        responseHandler: (response) => sseResponseHandler(searchPayload, response),
      }),
    }),
  }),
});
export const {
  useLazySortByQuery,
  useLazySearchTicketsQuery,
  useGetTicketQuery,
} = ticketsApi;

export const {
  endpoints: {
    sortBy, searchTickets,
  },
} = ticketsApi;
