'use strict';

angular.module('akceventApp')
    .factory('EventSiteSearch', function ($resource) {
        return $resource('api/_search/eventSites/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
