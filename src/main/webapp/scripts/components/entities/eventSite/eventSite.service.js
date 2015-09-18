'use strict';

angular.module('akceventApp')
    .factory('EventSite', function ($resource, DateUtils) {
        return $resource('api/eventSites/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.changed = DateUtils.convertLocaleDateFromServer(data.changed);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.changed = DateUtils.convertLocaleDateToServer(data.changed);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.changed = DateUtils.convertLocaleDateToServer(data.changed);
                    return angular.toJson(data);
                }
            }
        });
    });
