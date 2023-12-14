import writeToken from './writeToken';

/* eslint-disable quotes */
export default async function makeQuerry(address, body, headers, method = 'POST') {
  const token = localStorage.getItem('JWTtoken');
  // const refreshToken = localStorage.getItem('refreshToken');
  const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/${address}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: token || null,
      // "Refresh-Token": refreshToken || null,
      ...headers,
    },
    credentials: "include",
    method,
    body,
  });
  let bodyResponse;
  try {
    bodyResponse = await response.json();
  } catch {
    bodyResponse = null;
  }

  writeToken(response);
  return { status: response.status, headers: response.headers, body: bodyResponse };
}
