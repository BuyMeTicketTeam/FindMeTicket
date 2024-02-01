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

  function extractJsonFromResponse(responseString) {
    const jsonRegex = /\{.*\}/;
    const match = responseString.match(jsonRegex);

    if (match) {
      const jsonString = match[0];
      try {
        const jsonData = JSON.parse(jsonString);
        return jsonData;
      } catch (error) {
        console.error('JSON parser error:', error);
        return null;
      }
    } else {
      console.error('JSON not found');
      return null;
    }
  }

  const reader = response.body.getReader();
  const decoder = new TextDecoder();

  return {
    async* [Symbol.asyncIterator]() {
      while (true) {
        const { done, value } = await reader.read();
        const chunk = decoder.decode(value);
        const jsonData = extractJsonFromResponse(chunk);

        if (done) {
          break;
        }

        yield jsonData;
      }
    },
  };
}
