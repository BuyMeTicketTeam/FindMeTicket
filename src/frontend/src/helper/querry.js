import writeToken from './writeToken';

/* eslint-disable quotes */
export default async function makeQuerry(address, body, headers) {
  const token = localStorage.getItem('JWTtoken');
  const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/${address}`, {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": `http://localhost:${process.env.REACT_APP_PORT}`,
      "Access-Control-Allow-Credentials": true,
      Authorization: token || null,
      ...headers,
    },
    credentials: 'include',
    method: 'POST',
    body,
  });
  writeToken(response);
  return { status: response.status, headers: response.headers };
}
