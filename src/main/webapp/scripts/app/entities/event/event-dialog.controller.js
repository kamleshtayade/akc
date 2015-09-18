'use strict';

angular.module('akceventApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event', 'Eventsite',
        function($scope, $stateParams, $modalInstance, entity, Event, Eventsite) {

        $scope.event = entity;
        $scope.eventsites = Eventsite.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('akceventApp:eventUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveFinished);
            } else {
                Event.save($scope.event, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
