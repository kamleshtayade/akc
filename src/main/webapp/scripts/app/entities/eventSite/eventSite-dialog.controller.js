'use strict';

angular.module('akceventApp').controller('EventSiteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'EventSite',
        function($scope, $stateParams, $modalInstance, entity, EventSite) {

        $scope.eventSite = entity;
        $scope.load = function(id) {
            EventSite.get({id : id}, function(result) {
                $scope.eventSite = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('akceventApp:eventSiteUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.eventSite.id != null) {
                EventSite.update($scope.eventSite, onSaveFinished);
            } else {
                EventSite.save($scope.eventSite, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
