'use strict';

angular.module('akceventApp')
    .controller('EventSiteController', function ($scope, EventSite, EventSiteSearch, ParseLinks) {
        $scope.eventSites = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            EventSite.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.eventSites = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            EventSite.get({id: id}, function(result) {
                $scope.eventSite = result;
                $('#deleteEventSiteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            EventSite.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEventSiteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EventSiteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.eventSites = result;
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
            $scope.eventSite = {name: null, number: null, changed: null, id: null};
        };
    });
