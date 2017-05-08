'use strict';

/**
 * @ngdoc overview
 * @name kalahFrontendApp
 * @description
 * # kalahFrontendApp
 *
 * Main module of the application.
 */
angular
  .module('kalahFrontendApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  .provider('API_URL', function() {
    this.$get = function() {
      return "http://docker-machine:8080/kalah/";
    }
  });
