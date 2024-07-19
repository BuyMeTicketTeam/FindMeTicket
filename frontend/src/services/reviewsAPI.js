import Cookies from 'universal-cookie';
import { api } from './api';

const cookies = new Cookies(null, { path: '/' });

export const reviewsAPI = api.injectEndpoints({
  endpoints: (builder) => ({
    getAllReviews: builder.query({
      query: () => ({
        url: '/users/reviews',
        method: 'GET',
      }),
    }),
    getUserReview: builder.query({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/reviews`,
        method: 'GET',
      }),
    }),
    addReview: builder.mutation({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/reviews`,
        method: 'POST',
      }),
    }),
    // updateReview: builder.mutation({
    //   query: () => ({
    //     url: `/users/${cookies.get('USER_ID')}/reviews`,
    //     method: 'POST',
    //   }),
    // }),
    deleteReview: builder.mutation({
      query: () => ({
        url: `/users/${cookies.get('USER_ID')}/reviews`,
        method: 'DELETE',
      }),
    }),
  }),
});

export const {
  useGetAllReviewsQuery,
  useLazyGetUserReviewQuery,
  useAddReviewMutation,
  useDeleteReviewMutation,
} = reviewsAPI;
