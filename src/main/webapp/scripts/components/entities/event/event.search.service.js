'use strict';

angular.module('akceventApp')
    .factory('EventSearch', function ($resource) {
        return $resource('api/_search/events/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
