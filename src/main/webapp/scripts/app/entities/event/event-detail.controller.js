'use strict';

angular.module('akceventApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event, Eventsite) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        $rootScope.$on('akceventApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
    });
