angular.module('BagualApp', ['ui.router', 'ngResource', 'ngclipboard'])
    .config(['$stateProvider', '$locationProvider', '$urlRouterProvider', function($stateProvider, $locationProvider, $urlRouterProvider){

        $locationProvider.html5Mode(true);
        // $urlRouterProvider.otherwise('/not-found');

        $stateProvider.state('404', {
            url: '*path',
            templateUrl: 'views/404.html'
        });

        $stateProvider.state('home', {
            name: 'shortening',
            url: '/',
            component: 'shortening'
        });
        $stateProvider.state('details', {
            name: 'shorteningDetails',
            url: '/{alias}\+',
            component: 'shorteningDetails'
        });
    }])
    .factory('ShorteningService', ['$resource', function($resource) {
        return $resource('/api/shortening');
    }])
    .factory('ReportService', ['$resource', function($resource) {
        return $resource('/api/details/:alias/:shortcut', {alias:'@alias', shortcut: '@shortcut'});
    }])
    .component('shortening', {
        templateUrl: 'views/encurtar.html',
        controller: ['ShorteningService', ShorteningController]
    })
    .component('shorteningDetails', {
        templateUrl: 'views/details.html',
        controller: ['ReportService','$stateParams', '$state', DetailsController]
    });

function ShorteningController(ShorteningService){
    var model = this;

    model.sendUrl = function() {
        ShorteningService.save({
            url: model.url
        }, function(shortUrl){
            model.shortUrl = shortUrl;
        });
    };

    model.shortUrlCopied = function(message) {
        model.shortUrlCopiedMessage = message;
    }

}

function DetailsController(ReportService, $stateParams, $state) {
    var model = this;

    ReportService.get(
        {alias: 'bagu.al', shortcut: $stateParams.alias},
        function(data){
           model.urlDetails = data.content;
           model.urlDetails.created = new Date(model.urlDetails.created).toLocaleString();
        }, function(){
            $state.go('404');
        });
    model.shortUrlCopied = function(message) {
        model.shortUrlCopiedMessage = message;
    }
}