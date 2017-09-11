'use strict';
module.exports = {
  up: function(queryInterface, Sequelize) {
    return queryInterface.createTable('Books', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      cat: {
        type: Sequelize.STRING
      },
      name: {
        type: Sequelize.STRING
      },
      author: {
        type: Sequelize.STRING
      },
      series_t: {
        type: Sequelize.STRING
      },
      sequence_i: {
        type: Sequelize.INTEGER
      },
      genre: {
        type: Sequelize.STRING
      },
      inStock: {
        type: Sequelize.BOOLEAN
      },
      price: {
        type: Sequelize.FLOAT
      },
      pages_i: {
        type: Sequelize.INTEGER
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  down: function(queryInterface, Sequelize) {
    return queryInterface.dropTable('Books');
  }
};
