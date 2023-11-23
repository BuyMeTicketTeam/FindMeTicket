/* eslint-disable quotes */
export default async function makeQuerry(address, body /* headers */) {
  // const token = localStorage.getItem('JWTtoken');
  const response = await fetch(`http://localhost:3000/${address}`, {
    headers: {
      "Content-Type": "application/json",
      // Authorization: token || null,
      // ...headers,
    },
    // credentials: 'include',
    method: 'POST',
    body,
  });
  return { status: response.status, headers: response.headers };
}
