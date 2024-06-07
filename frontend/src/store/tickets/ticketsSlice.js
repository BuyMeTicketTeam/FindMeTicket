/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
import { ticketsApi } from '../../services/ticketsApi';

export const initialState = {
  tickets: [],
  departureCity: '',
  arrivalCity: '',
  departureDate: Date.now(),
  bus: true,
  train: true,
  airplane: false,
  ferry: false,
  sortBy: null,
  ascending: false,
};

function setTickets(state, action) {
  state.tickets = action.payload;
}

const slice = createSlice({
  name: 'tickets',
  initialState,
  reducers: {
    setSearchPayload: (state, action) => {
      state.train = action.payload.train;
      state.bus = action.payload.bus;
      state.arrivalCity = action.payload.arrivalCity;
      state.departureCity = action.payload.departureCity;
      state.departureDate = action.payload.departureDate;
    },
    setCities: (state, action) => {
      state.arrivalCity = action.payload.arrivalCity;
      state.departureCity = action.payload.departureCity;
    },
    setDepartureDate: (state, action) => {
      state.departureDate = action.payload;
    },
    setTransportType: (state, action) => {
      switch (action.payload) {
        case 'bus':
          state.bus = true;
          state.train = false;
          break;
        case 'train':
          state.bus = false;
          state.train = true;
          break;
        default:
          state.bus = true;
          state.train = true;
          break;
      }
    },
    setSorting: (state, action) => {
      state.sortBy = action.payload.sortBy;
      state.ascending = action.payload.ascending;
    },
  },
  extraReducers: (builder) => {
    builder.addMatcher(ticketsApi.endpoints.sortBy.matchFulfilled, setTickets);
    builder.addMatcher(ticketsApi.endpoints.searchTickets.matchFulfilled, setTickets);
  },
});

export const {
  setCities, setDepartureDate, setSearchPayload, setTransportType, setSorting,
} = slice.actions;

export default slice.reducer;
