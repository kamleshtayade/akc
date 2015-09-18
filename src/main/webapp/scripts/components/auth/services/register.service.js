'use strict';

angular.module('akceventApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


