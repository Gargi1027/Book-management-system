'use strict';
module.exports = function(sequelize, DataTypes) {
  var Book = sequelize.define('Book', {
    cat: DataTypes.STRING,
    name: DataTypes.STRING,
    author: DataTypes.STRING,
    series_t: DataTypes.STRING,
    sequence_i: DataTypes.INTEGER,
    genre: DataTypes.STRING,
    inStock: DataTypes.BOOLEAN,
    price: DataTypes.FLOAT,
    pages_i: DataTypes.INTEGER
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return Book;
};
