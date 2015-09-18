'use strict';

angular.module('akceventApp')
    .controller('EventController', function ($scope, Event, EventSearch, ParseLinks) {
        $scope.events = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Event.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.events = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
                $('#deleteEventConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEventConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EventSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.events = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.event = {name: null, number: null, eventStatus: null, judgepanelStatus: null, changed: null, startDate: null, endDate: null, entryCount: null, entryLimit: null, entryLimitType: null, id: null};
        };
    });
