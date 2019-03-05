angular.module('BagualApp', ['ui.router', 'ngResource', 'ngclipboard', 'ngMap', 'ngStomp'])
    .config(['$stateProvider', '$locationProvider', '$urlRouterProvider', function($stateProvider, $locationProvider){

        $locationProvider.html5Mode(true);

        $stateProvider.state('404', {
            url: '*path',
            templateUrl: 'views/404.html'
        });

        $stateProvider.state('home', {
            name: 'main',
            url: '/',
            component: 'home'
        });
        $stateProvider.state('detailsLink', {
            name: 'detailsLink',
            url: '/{alias}\+',
            component: 'detailsLink'
        });
    }])
    .factory('ShorteningService', ['$resource', function($resource) {
        return $resource('/api/shortening');
    }])
    .factory('ReportService', ['$resource', function($resource) {
        return $resource('/api/details/:alias/:shortcut', {alias:'@alias', shortcut: '@shortcut'});
    }])
    .factory('SummaryService', ['$resource', function($resource) {
        return $resource('/api/details/:alias', {alias:'@alias'});
    }])
    .factory('GeolocationService', ['NgMap', '$stomp', '$log',function(NgMap, $stomp, $log) {

        var markerClusterer = {
            clearMarkers: function(){},
            addMarkers: function(){},
            fitMapToMarkers: function(){}
        };

        NgMap.getMap().then(function(map) {
            markerClusterer = new MarkerClusterer(map, [], {maximumZoom: false, averageCenter: true, imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
        });

        var clear = function() {
            markerClusterer.clearMarkers();
        };

        var buildMarker = function(lat, lon) {
            var latLng = new google.maps.LatLng(lat, lon);
            return new google.maps.Marker({
                position:latLng,
                animation: google.maps.Animation.DROP
            });
        };

        var buildMarkers = function(locations) {
            var dynMarkers = [];
            for (var i=0; i<locations.length; i++) {
                for(var j=0; j<locations[i].total; j++) {
                    dynMarkers.push(buildMarker(locations[i].coordinates.lat, locations[i].coordinates.lon));
                }
            }
            return dynMarkers;
        };

        var addMarker = function(dynMarker) {
            NgMap.getMap().then(function(map) {
                markerClusterer.addMarker(dynMarker);
            });
            centralize();
        };

        var addMarkers = function(dynMarkers) {
            NgMap.getMap().then(function(map) {
                markerClusterer.addMarkers(dynMarkers);
            });
            centralize();
        };

        var centralize = function() {
            NgMap.getMap().then(function(map) {
                markerClusterer.fitMapToMarkers();
            });
        };

        var channel = function(){
            $stomp.setDebug(function (args) {
                $log.debug(args)
            });
            $stomp.connect('/socket', {})
                .then(function(frame){
                    $stomp.subscribe('/topic/notification', function (payload, headers, res) {
                        addMarker(buildMarker(payload.lat, payload.lon));
                    });
                });

        };

        return {
            clear: clear,
            buildMarkers: buildMarkers,
            addMarkers: addMarkers,
            channel: channel
        }

    }])
    .component('home', {
        templateUrl: 'views/main.html',
        controller: ['SummaryService', '$state', 'GeolocationService', SummaryController]
    })
    .component('menuView', {
        templateUrl: 'views/menu.html',
        controller: ['$state', 'ShorteningService', function($state, ShorteningService) {
            var model = this;
            model.submit = function() {
                var regex = /(https?:\/\/bagu\.al\/)([0-9a-zA-Z]+)$/;

                if(regex.exec(model.url) == null) {
                    ShorteningService.save({
                        url: model.url
                    }, function(shortUrl){
                        model.shortUrl = shortUrl;
                        $state.go('detailsLink', {alias: regex.exec(model.shortUrl.content)[2]});
                        model.url = "";
                    }, function() {
                        $state.go('404', {path: ''});
                    });
                } else {
                    $state.go('detailsLink', {alias: regex.exec(model.url)[2]});
                    model.url = "";
                }
            };
        }]
    })
    .component('summary', {
        templateUrl: 'views/summary.html',
        controller: ['SummaryService', '$state', 'GeolocationService', SummaryController]
    })
    .component('detailsLink', {
        templateUrl: 'views/details.html',
        controller: ['ReportService', '$stateParams', '$state', 'GeolocationService', DetailsController]
    });

function DetailsController(ReportService, $stateParams, $state, GeolocationService) {
    var model = this;

    GeolocationService.clear();
    GeolocationService.channel();

    ReportService.get(
        {alias: 'bagu.al', shortcut: $stateParams.alias},
        function(data){
           model.urlDetails = data.content;
           model.urlDetails.created = new Date(model.urlDetails.created).toLocaleString();

           GeolocationService.addMarkers(GeolocationService.buildMarkers(data.content.topLocation));

        }, function(){
            $state.go('404');
        });
    model.shortUrlCopied = function(message) {
        model.shortUrlCopiedMessage = message;
    }
}

function SummaryController(SummaryService, $state, GeolocationService) {
    var model = this;

    GeolocationService.clear();
    GeolocationService.channel();

    SummaryService.get(
        {alias: 'bagu.al'},
        function(data){
            model.urlDetails = data.content;
            model.urlDetails.created = new Date(model.urlDetails.created).toLocaleString();
            GeolocationService.addMarkers(GeolocationService.buildMarkers(data.content.topLocation));

        }, function(){
            $state.go('404');
        });
}