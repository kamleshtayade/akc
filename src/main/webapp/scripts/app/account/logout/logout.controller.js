'use strict';

angular.module('akceventApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
