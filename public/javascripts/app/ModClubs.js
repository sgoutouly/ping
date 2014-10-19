/**
 * Module contenant le CU accueil de l'application
 */
var modClubs = angular.module("ping.clubs", []);

/* 
 * Routage de ces contrôleurs
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/clubs', {
    				templateUrl: 'partials/clubs.html',
    				controller: 'ClubsCtrl'
  				})
  		}
])

/**
 * Controleur du menu de navigation
 */
modClubs.controller("ClubsCtrl", ["$scope", "$routeParams", "ComposantClubs", function($scope, $routeParams, ComposantClubs) {
	$scope.clubs = ComposantClub.rechercherClub($routeParams.numero).clubs;    
}]);


/**
 * Service rechercherListe
 * Enregistre la lise en cours de saisie
 */
modClubs.factory("ComposantClubs", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
    rechercherClub: function() {
      return toolbox_http.get("/clubs");
    }
   }
}]);