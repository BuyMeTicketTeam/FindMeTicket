/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import { ticketsApi } from '../../services/ticketsApi';

export const initialState = {
  tickets: [],
};

function setTickets(state, action) {
  state.tickets = action.payload;
}

const slice = createSlice({
  name: 'tickets',
  initialState,
  extraReducers: (builder) => {
    builder.addMatcher(ticketsApi.endpoints.sortBy.matchFulfilled, setTickets);
  },
});

export default slice.reducer;
