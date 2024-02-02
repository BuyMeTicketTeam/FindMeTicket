import { fetchEventSource } from '@microsoft/fetch-event-source';

async function eventSourceQuery2({
  address, body, onMessage, onError, onClose, method = 'GET',
}) {
  try {
    await fetchEventSource(`http://localhost:8080/${address}`, {
      method,
      headers: {
        'Content-Type': 'application/json',
      },
      body,
      onopen(res) {
        if (res.ok && res.status === 200) {
          console.log('Connection made ', res);
        } else if (
          res.status >= 400
            && res.status < 500
            && res.status !== 429
        ) {
          console.log('Client side error ', res);
        }
      },
      onmessage(event) {
        onMessage(event);
        console.log('event', event);
      },
      onclose() {
        console.log('Connection closed by the server');
        onClose();
        throw new Error('Connection closed');
      },
      onerror(err) {
        onError(err);
        throw err;
      },
    });
  } catch (error) {
    console.log('There was an error or connection was closed', error);
  }
}

export default eventSourceQuery2;
