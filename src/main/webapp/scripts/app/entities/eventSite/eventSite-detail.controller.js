'use strict';

angular.module('akceventApp')
    .controller('EventSiteDetailController', function ($scope, $rootScope, $stateParams, entity, EventSite) {
        $scope.eventSite = entity;
        $scope.load = function (id) {
            EventSite.get({id: id}, function(result) {
                $scope.eventSite = result;
            });
        };
        $rootScope.$on('akceventApp:eventSiteUpdate', function(event, result) {
            $scope.eventSite = result;
        });
    });
