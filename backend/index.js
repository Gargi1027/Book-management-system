const express = require("express");
const bodyParser = require("body-parser");
const models = require("./models");
const cors = require("cors");
const morgan= require("morgan");
const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use(morgan('tiny'));

//get books page
app.get("/book", function(req, res) {
    models.Book.findAll()
      .then(function(books) {
        res.json(books);
      });
});

//create new book
app.post("/book", function(req, res) {
  console.log(req.body);
  models.Book.create(req.body)
    .then(function() {
      return models.Book.findAll();
    }).then(function(books) {
      console.log(books);
      res.json(books);
    });
});

//delete book
app.get("/book/:id", function(req, res) {
  models.Book.findById(req.params.id)
    .then(function(newBook) {
      newBook.destroy()
      })
      .then(function(){
        return models.Book.findAll();
      })
      .then(function(books){
        res.json(books);
      })
    });

//update book
app.patch("/book/:id", function(req,res){
  console.log(req.body);
  let data={
    cat:req.body.cat,
    name:req.body.name,
    author: req.body.author,
    series_t: req.body.series_t,
    sequence_i: req.body.sequence_i,
    genre: req.body.genre,
    inStock: req.body.inStock,
    price: req.body.price,
    pages_i: req.body.pages_i
  }
  console.log(data);
  models.Book.findById(req.params.id)
  .then(function(link){
    link.update(data)
    .then(function(newLink){
        res.json(newLink);
    });
  });
});

app.listen(9999, function() {
    console.log("Running....");
});
