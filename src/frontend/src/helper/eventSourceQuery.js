export default async function eventSourceQuery(address, body, headers, method = 'POST') {
  const token = localStorage.getItem('JWTtoken');
  const eventSource = new EventSource(`http://localhost:${process.env.REACT_APP_PORT}/${address}`);
  await fetch(`http://localhost:${process.env.REACT_APP_PORT}/${address}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token || null,
      ...headers,
    },
    credentials: 'include',
    method,
    body,
  });

  return { eventSource };
}
