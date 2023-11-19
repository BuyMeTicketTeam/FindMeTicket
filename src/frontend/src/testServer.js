/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

createServer({
  routes() {
    // Responding to a POST request
    this.post('/login', () => {
      document.cookie = 'remember_me=true; path=/; max-age=600';
      return new Response(200, { Authorization: 'alkshfksadfjs2143234' });
    });
    this.post('/register', () => new Response(200));
    this.post('/confirm-email', () => new Response(200));
    this.post('/reset', () => new Response(200));
    this.post('/new-password', () => new Response(200));
    this.post('/resend-confirm-token', () => new Response(200));
  },
});
