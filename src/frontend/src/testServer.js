/* eslint-disable arrow-body-style */
/* eslint-disable max-len */
/* eslint-disable no-new */
/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

const from = [
  { cityUkr: 'Дніпро', cityEng: 'Дніпро' },
  { cityUkr: 'Київ', cityEng: 'Київ' },
  { cityUkr: 'Одеса', cityEng: 'Одеса' },
];
const destination = [
  { cityUkr: 'Дніпро', cityEng: 'Дніпро' },
  { cityUkr: 'Дністер', cityEng: 'Київ' },
  { cityUkr: 'Одеса', cityEng: 'Одеса' },
];
createServer({
  routes() {
    // Responding to a POST request
    this.post('/login', () => {
      // document.cookie = 'rememberMe=cookie-content-here; path=/; expires=123123123123;';
      return new Response(200, { rememberMe: process.env.REACT_APP_TEST_JWT_TOKEN, userId: 1231231421 }, { Authorization: process.env.REACT_APP_TEST_JWT_TOKEN });
    });
    this.post('/register', () => new Response(200));
    this.get('/oauth2/authorize/google', () => new Response(401));
    this.post('/confirm-email', () => new Response(200));
    this.post('/reset', () => new Response(200));
    this.post('/new-password', () => new Response(200));
    this.post('/resend-confirm-token', () => new Response(200));
    this.post('/logout', () => new Response(200));
    this.post('/get1', () => new Response(200));
    this.post('/typeAhead', (schema, request) => {
      console.log(JSON.parse(request.requestBody).value);
      if (JSON.parse(request.requestBody).startLetters === 'Дн') {
        return new Response(200, undefined, JSON.stringify(from));
      }
      return new Response(200, undefined, JSON.stringify(destination));
    });
    this.post('/request', () => new Response(200));
  },
});
