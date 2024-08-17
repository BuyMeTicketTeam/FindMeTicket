import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "../../services/axiosConfig";

const clearAuthHeader = () => {
  api.defaults.headers.Authorization = "";
};

export const signUp = createAsyncThunk(
  "auth/signup",
  async (credentials, { rejectWithValue }) => {
    try {
      const response = await api.post("/auth/sign-up", credentials);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const signIn = createAsyncThunk(
  "auth/signin",
  async (credentials, { rejectWithValue }) => {
    try {
      const response = await api.post("/auth/sign-in", credentials);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const signOut = createAsyncThunk(
  "auth/signout",
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.get("/auth/sign-out");
      clearAuthHeader();
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);
