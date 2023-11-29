/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

createServer({
  routes() {
    // Responding to a POST request
    this.post('/login', () => new Response(200, { Authorization: process.env.REACT_APP_TEST_JWT_TOKEN }));
    this.post('/register', () => new Response(200));
    this.post('/confirm-email', () => new Response(200));
    this.post('/reset', () => new Response(200));
    this.post('/new-password', () => new Response(200));
    this.post('/resend-confirm-token', () => new Response(200));
    this.post('/logout', () => new Response(200));
  },
});
