export default async function makeQuerry(address, body) {
  const response = await fetch(`http://localhost:3000/${address}`, {
    method: 'POST',
    // headers: {
    //   'Content-Type': 'application/json',
    // },
    body,
  });
  return { status: response.status, body: await response.text() };
}
