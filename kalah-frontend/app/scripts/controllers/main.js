'use strict';

/**
 * @ngdoc function
 * @name kalahFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the kalahFrontendApp
 */
angular.module('kalahFrontendApp')
  .controller('MainCtrl', function ($scope, $http, API_URL) {

//    var API_URL = "http://docker-machine:8080/kalah/"

    $scope.create = function() {
      $http.post(API_URL, {}, {}).then(function(response) {
        if (response.status === 201) {
          $scope.game = response.data;
          console.log(response.data);
          $scope.player = "NORTH";
        }
      });
    }

    $scope.join = function(gameId) {
      $http.post(API_URL + gameId + "/join", {}, {}).then(function(response) {
        console.log(response);
        if (response.status === 200) {
          $scope.game = response.data;
          console.log(response.data);
          $scope.player = "SOUTH";
        }
      });
    }

    $scope.refresh = function() {
      $http.get(API_URL + $scope.game.id, {}, {}).then(function(response) {
        if (response.status === 200) {
          $scope.game = response.data;
          console.log(response.data);
        }
      });
    }

    $scope.sow = function(pitIndex) {
      console.log('pitIndex: '+ pitIndex);
      $http.post(API_URL + $scope.game.id + "/sow", {'player': $scope.player, 'pitIndex': pitIndex}, {})
      .then(function(response) {
        console.log(response);
        if (response.status === 200) {
          $scope.game = response.data;
          console.log(response.data);
        }
      });
    }


    $scope.emptyRange = function(lower, upper) {
      var range = [];
      for (var i = 0; i < $scope.game.pitsPerPlayer - 2; i++) {
        range.push(i);
      }
      return range;
    };

    $scope.northIndexes = function() {
      var range = [];
      for (var i = $scope.game.pitsPerPlayer - 1; i >= 0 ; i--) {
        range.push(i);
      }
      return range;
    }

    $scope.southIndexes = function() {
      var range = [];
      for (var i = $scope.game.pitsPerPlayer + 1; i < 2 * $scope.game.pitsPerPlayer + 1; i++) {
        range.push(i);
      }
      return range;
    }

  });
