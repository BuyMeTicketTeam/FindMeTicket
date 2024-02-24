const { log } = require('console');
const http = require('http');

http.createServer((req, res) => {
  // Set the response headers
  res.writeHead(200, {
    'Content-Type': 'text/event-stream',
    'Cache-Control': 'no-cache',
    'Connection': 'keep-alive',
    'Access-Control-Allow-Origin': '*'
  });

  // Send a message to the client every 5 seconds
  const interval = setInterval(() => {
    console.log('sending message');
    res.write('data: ' + new Date().toLocaleTimeString() + '\n\n');
  }, 5000);

  req.on('close', () => {
    console.log('client closed connection');
    clearInterval(interval); // Stop sending messages after user closes connection
  });
}).listen(3000);