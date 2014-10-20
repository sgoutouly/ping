/**
 * Module contenant le CU accueil de l'application
 */
var modClub = angular.module("ping.club", []);

/* 
 * Routage de ces contrôleurs
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/clubs/:numero', {
    				templateUrl: '/assets/partials/club.html',
    				controller: 'ClubCtrl'
  				})
  		}
])

/**
 * Controleur du menu de navigation
 */
modClub.controller("ClubCtrl", ["$scope", "$routeParams", "ComposantClub", function($scope, $routeParams, ComposantClub) {
  $scope.club = ComposantClub.consulterClub($routeParams.numero);
}]);

/**
 * Service rechercherListe
 * Enregistre la lise en cours de saisie
 */
modClub.factory("ComposantClub", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
    consulterClub: function(numero) {
      return toolbox_http.get("/clubs/" + numero);
    }
   }
}]);