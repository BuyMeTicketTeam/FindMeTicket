import responseInterceptor from './responseInterceptor';

/* eslint-disable quotes */
export default async function makeQuerry(address, body, headers, method = 'POST') {
  const token = localStorage.getItem('JWTtoken');
  let response;
  try {
    response = await fetch(`http://localhost:${process.env.REACT_SERVER_PORT}/${address}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: token || null,
        ...headers,
      },
      credentials: "include",
      method,
      body,
    });
  } catch (error) {
    console.log(error);
    return { status: 500, error };
  }
  let bodyResponse;
  try {
    bodyResponse = await response.json();
  } catch {
    bodyResponse = null;
  }
  responseInterceptor(response, bodyResponse);
  return { status: response.status, headers: response.headers, body: bodyResponse };
}
