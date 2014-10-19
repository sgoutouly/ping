/**
 * Module contenant le CU accueil de l'application
 */
var modAccueil = angular.module("ping.accueil", []);

/* 
 * Routage de ces contrôleurs
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/index', {
    				templateUrl: 'partials/creationListe.html',
    				controller: 'AccueilCtrl'
  				})
  		}
])

/**
 * Controleur du menu de navigation
 */
modAccueil.controller("AccueilCtrl", ["$scope", "$location", function($scope, $location){
	/*$scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'menu';
        return page === currentRoute ? 'item-actif' : '';
    };*/        
}]);