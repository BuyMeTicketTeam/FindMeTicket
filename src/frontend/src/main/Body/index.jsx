/* eslint-disable max-len */
import React, { useCallback } from 'react';
import Filters from '../Filters';
import Ticket from '../Ticket';
// import Button from '../../utils/Button';
import makeQuerry from '../../helper/querry';

export default function Body({
  ticketsData, requestBody, onTicketsData,
}) {
  async function sendRequest(sortArg) {
    const body = {
      ...requestBody,
      sortingBy: sortArg,
    };
    const response = await makeQuerry('sortedby', JSON.stringify(body));
    const responseBody = response.status === 200 ? response.body : null;
    onTicketsData(responseBody);
  }

  const handleSort = useCallback((sortArg) => sendRequest(sortArg), [requestBody]);

  return (
    <>
      <Filters handleSort={handleSort} />
      <div className="tickets">
        {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
        {/* TODO: Ticket upload button */}
        {/* {ticketsData.length !== 0 ? <Button className="tickets__more" name={loading ? <img className="tickets__loading-img" src="../img/loading.svg" alt="Loading..." /> : checkResponse()} onButton={handleSend} /> : null} */}
      </div>
    </>
  );
}
