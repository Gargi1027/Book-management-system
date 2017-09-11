var connect = require('connect');
var serveStatic = require('serve-static');
var port = 3000;

var app = connect();

app.use(serveStatic(".", {'index': ['src/index.html']}))
   .listen(port);
console.log("[API Testing Harness] Running: http://localhost:" + port);
