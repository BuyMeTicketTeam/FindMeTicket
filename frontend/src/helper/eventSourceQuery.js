import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getI18n } from 'react-i18next';

async function eventSourceQuery({
  address, body, handleMessage, handleError, handleClose, handleOpen, headers,
}) {
  try {
    const token = localStorage.getItem('JWTtoken');
    await fetchEventSource(`${process.env.REACT_APP_SERVER_ADDRESS}${address}${new URLSearchParams({
      ...body,
    })}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Content-Language': getI18n().language.toLowerCase(),
        Accept: 'application/json',
        Authorization: token ?? '',
        ...headers,
      },
      credentials: 'include',
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
        if (handleOpen) {
          handleOpen(res);
        }
      },
      onmessage(event) {
        if (handleMessage) {
          handleMessage(event);
        }
        console.log('event', event);
      },
      onclose() {
        console.log('Connection closed by the server');
        if (handleClose) {
          handleClose();
        }
        throw new Error('Connection closed');
      },
      onerror(err) {
        if (handleError) {
          handleError(err);
        }
        throw err;
      },
    });
  } catch (error) {
    console.log('There was an error or connection was closed', error);
  }
}

export default eventSourceQuery;
