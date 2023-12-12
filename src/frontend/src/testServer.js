/* eslint-disable no-new */
/* eslint-disable import/no-extraneous-dependencies */
import { createServer, Response } from 'miragejs';

const from = [
  { cityUkr: 'Дніпро', cityEng: 'Дніпро', country: 'UA' },
  { cityUkr: 'Київ', cityEng: 'Київ', country: 'UA' },
  { cityUkr: 'Одеса', cityEng: 'Одеса', country: 'UA' },
];
const destination = [
  { cityUkr: 'Дніпро', cityEng: 'Дніпро', country: 'UA' },
  { cityUkr: 'Дністер', cityEng: 'Київ', country: 'UA' },
  { cityUkr: 'Одеса', cityEng: 'Одеса', country: 'UA' },
];
const tickets = [
  {
    id: 1,
    departureTime: '8:40',
    departureDate: '29.11, вт',
    travelTime: '8 год 5 хв',
    arrivalTime: '16:30',
    arrivalDate: '29.11, вт',
    placeFrom: 'Кам’янець-Подільський',
    placeTo: 'Київ',
    placeFromDetails: 'Автовокзал "Центральний"',
    placeToDetails: 'Южный автовокзал',
    priceOrdinary: '4000',
    priceOld: '800',
    tickerCarrier: 'nikkaBus',
  },
  {
    id: 2,
    departureTime: '7:40',
    departureDate: '29.11, вт',
    travelTime: '8 год 45 хв',
    arrivalTime: '16:30',
    arrivalDate: '29.11, вт',
    placeFrom: 'Дніпро',
    placeTo: 'Івано-Франківськ',
    placeFromDetails: 'Автовокзал "Центральний"',
    placeToDetails: 'Южный автовокзал',
    priceOrdinary: '500',
    priceOld: '',
    tickerCarrier: 'nikkaBus',
  },
  {
    id: 3,
    departureTime: '8:40',
    departureDate: '29.11, вт',
    travelTime: '8 год 5 хв',
    arrivalTime: '16:30',
    arrivalDate: '29.11, вт',
    placeFrom: 'Одеса',
    placeTo: 'Київ',
    placeFromDetails: 'Автовокзал "Центральний"',
    placeToDetails: 'Южный автовокзал',
    priceOrdinary: '1000',
    priceOld: '8000',
    tickerCarrier: 'nikkaBus',
  },
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
    this.post('/typeAhead', (schema, request) => {
      if (JSON.parse(request.requestBody).startLetters === 'Дн') {
        return new Response(200, undefined, JSON.stringify(from));
      }
      return new Response(200, undefined, JSON.stringify(destination));
    });
    this.post('/request', () => new Response(200, undefined, JSON.stringify(tickets)), { timing: 3000 });
  },
});
