/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

createServer({
  routes() {
    // this.urlPrefix = 'http://localhost:3000';
    // this.namespace = 'api';
    // Responding to a POST request
    this.post('/login', () => new Response(200));
    this.post('/register', () => new Response(200));
    this.post('/confirm-email', () => new Response(200));
    this.post('/reset', () => new Response(200));
    this.post('/new-password', () => new Response(200));
  },
});
