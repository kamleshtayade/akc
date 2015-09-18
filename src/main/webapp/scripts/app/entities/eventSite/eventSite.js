'use strict';

angular.module('akceventApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventSite', {
                parent: 'entity',
                url: '/eventSites',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'akceventApp.eventSite.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventSite/eventSites.html',
                        controller: 'EventSiteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('eventSite');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('eventSite.detail', {
                parent: 'entity',
                url: '/eventSite/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'akceventApp.eventSite.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventSite/eventSite-detail.html',
                        controller: 'EventSiteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('eventSite');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'EventSite', function($stateParams, EventSite) {
                        return EventSite.get({id : $stateParams.id});
                    }]
                }
            })
            .state('eventSite.new', {
                parent: 'eventSite',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/eventSite/eventSite-dialog.html',
                        controller: 'EventSiteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, number: null, changed: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('eventSite', null, { reload: true });
                    }, function() {
                        $state.go('eventSite');
                    })
                }]
            })
            .state('eventSite.edit', {
                parent: 'eventSite',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/eventSite/eventSite-dialog.html',
                        controller: 'EventSiteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EventSite', function(EventSite) {
                                return EventSite.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('eventSite', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
