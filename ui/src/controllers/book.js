angular.module('interview.controllers.book', ["ui.router"])
    .config(function ($stateProvider) {
        $stateProvider
            .state("books", {
                url: "/book",
                templateUrl: "src/views/books.html",
                controller: "BooksCtrl"
            });
    })
    .controller('BooksCtrl',function($scope,$state,$http, $window){
        $scope.booksTitle = "Books";
        $scope.bookArr=[];
        $scope.updateBook=[];
        $scope.books=[];
        $http({
          method: 'GET',
          url: 'http://localhost:9999/book'
        }).then(function(success){
            $scope.books = success.data;
            $scope.bookArr.push($scope.books);
            console.log($scope.books);
            console.log($scope.bookArr);
        }), function(error){
            console.log('Error: ' + success);
        };

        $scope.createBook = function() {
          console.log($scope.books);
          console.log($scope.name);
          if($scope.name===""||$scope.name===undefined){
            message="Please name the book!!!"
            $window.alert(message);
            $http({
              method: 'GET',
              url: 'http://localhost:9999/book'
            }).then(function(success){
                $scope.books = success.data;
                $scope.bookArr.push($scope.books);
                console.log($scope.books);
                console.log($scope.bookArr);
            }), function(error){
                console.log('Error: ' + success);
            };
          }
          else{
            $scope.data={cat:$scope.cat, name:$scope.name, author:$scope.author, series_t:$scope.series_t, sequence_i:$scope.sequence_i, genre:$scope.genre, inStock: $scope.inStock, price:$scope.price, pages_i:$scope.pages_i};
            console.log($scope.data);
            document.getElementById('createForm').reset();
            $http.post('http://localhost:9999/book', $scope.data)
            .then(function(success){
                $http({
                  method: 'GET',
                  url: 'http://localhost:9999/book'
                }).then(function(success){
                    $scope.books = success.data;
                    $scope.bookArr.push($scope.books);
                    console.log($scope.books);
                    console.log($scope.bookArr);
                    window.location.reload();
                }), function(error){
                    console.log('Error: ' + success);
                };
            }), function(error){
                console.log('Error: ' + success);
            };
          }
        };

          $scope.updateBook = function(id) {
          $scope.updateBook={id:id, cat: this.ucat, name: this.uname, author: this.uauthor, series_t:this.useries_t, sequence_i:this.usequence_i, genre:this.ugenre, price:this.uprice, pages_i:this.upages_i, inStock:this.uinstock};
          console.log($scope.updateBook);
          document.getElementById('createForm').reset();
            $http.patch('http://localhost:9999/book/'+id, $scope.updateBook)
            .then(function(success){
                $scope.data = success;
                console.log(success);
                console.log($scope.updateBook);
                $http({
                  method: 'GET',
                  url: 'http://localhost:9999/book'
                }).then(function(success){
                    $scope.books = success.data;
                    $scope.bookArr.push($scope.books);
                    console.log($scope.books);
                    console.log($scope.bookArr);
                    window.location.reload();
                }), function(error){
                    console.log('Error: ' + success);
                };
            }), function(error){
                console.log('Error: ' + success);
              };
        };

        $scope.delBook = function(id) {
          console.log(id);
        $http({
          method: 'GET',
          url: 'http://localhost:9999/book/'+id
        })
        .then(function(success){
            $http({
              method: 'GET',
              url: 'http://localhost:9999/book'
            }).then(function(success){
                $scope.books = success.data;
                $scope.bookArr.pop($scope.books);
                window.location.reload();
                console.log($scope.books);
                console.log($scope.bookArr);
            }), function(error){
                console.log('Error: ' + success);
            };
        }), function(error){
            console.log('Error: ' + success);
        };
   };
});
