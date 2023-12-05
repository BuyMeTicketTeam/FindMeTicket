/* eslint-disable no-new */
/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

const from = [
  { value: 'Дніпро', label: 'Дніпро' },
  { value: 'Київ', label: 'Київ' },
  { value: 'Одеса', label: 'Одеса' },
];
const destination = [
  { value: 'Харків', label: 'Харків' },
  { value: 'Львів', label: 'Львів' },
  { value: 'Одеса', label: 'Одеса' },
];
createServer({
  routes() {
    // Responding to a POST request
    this.post('/login', () => new Response(200, { Authorization: process.env.REACT_APP_TEST_JWT_TOKEN }, { Authorization: process.env.REACT_APP_TEST_JWT_TOKEN }));
    this.post('/register', () => new Response(200));
    this.post('/confirm-email', () => new Response(200));
    this.post('/reset', () => new Response(200));
    this.post('/new-password', () => new Response(200));
    this.post('/resend-confirm-token', () => new Response(200));
    this.post('/logout', () => new Response(200));
    this.post('/get1', () => new Response(200));
    this.post('/cities', (schema, request) => {
      console.log(JSON.parse(request.requestBody).value);
      if (JSON.parse(request.requestBody).value === 'Дн') {
        return new Response(200, undefined, JSON.stringify(from));
      }
      return new Response(200, undefined, JSON.stringify(destination));
    });
  },
});
