angular.module('interview.controllers.main', ["ui.router"])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("main", {
                url: "/main",
                templateUrl: "src/views/main.html",
                controller: "MainCtrl"
            });
        $urlRouterProvider.otherwise("/main");
    })
    .controller('MainCtrl', function ($scope, $state) {
        $scope.goBooks = function () {
            $state.go("books");
        };
        $scope.message = "Hello World";
    });