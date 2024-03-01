import React from 'react';
import Loader from '../../Loader';
import Error from '../../Error';
import Tourist from '../Tourist';
import Filters from '../Filters';
import Ticket from '../Ticket';
import Ad from '../../Ad';

export default function TicketsBody({
  loading, error, ticketsData, setTicketsData, requestBody, selectedTransport,
}) {
  if (error) {
    return <Error error={error} />;
  }
  if (loading && ticketsData?.length === 0) {
    return <Loader />;
  }
  if (ticketsData?.length > 0) {
    return (
      <>
        <Tourist
          ticketsData={ticketsData}
          setTicketsData={setTicketsData}
          city={requestBody.arrivalCity}
        />
        <Filters
          loading={loading}
          setTicketsData={setTicketsData}
          requestBody={requestBody}
          selectedTransport={selectedTransport}
        />
        <div className="tickets">
          {ticketsData.map((item) => <Ticket key={item.id} data={item} />)}
        </div>
      </>
    );
  }
  return <Ad isBig />;
}
