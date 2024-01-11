/* eslint-disable no-constant-condition */
/* eslint-disable no-await-in-loop */
export default async function eventSourceQuery(address, body, headers, method = 'POST') {
  const token = localStorage.getItem('JWTtoken');
  const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/${address}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token || null,
      ...headers,
    },
    credentials: 'include',
    method,
    body,
  });

  async function toJSON(responseBody) {
    const reader = responseBody.getReader(); // `ReadableStreamDefaultReader`
    const decoder = new TextDecoder();
    const chunks = [];

    async function read() {
      const { done, value } = await reader.read();

      // all chunks have been read?
      if (done) {
        return JSON.parse(chunks.join(''));
      }

      const chunk = decoder.decode(value, { stream: true });
      console.log(chunk);
      chunks.push(chunk);
      return read(); // read the next chunk
    }

    return read();
  }

  const jsonData = await toJSON(response.body);

  console.log(jsonData);

  // const reader = response.body.getReader();
  // const chunks = [];
  // while (true) {
  //   const { done, value } = await reader.read();

  //   if (done) {
  //     console.log(JSON.parse(chunks.join('')));
  //     return JSON.parse(chunks.join(''));
  //   }
  //   const chunk = decoder.decode(value, { stream: true });
  //   chunks.push(chunk);
  // }
}
