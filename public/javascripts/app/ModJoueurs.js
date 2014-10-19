/**
 * Module contenant le CU accueil de l'application
 */
var modJoueurs = angular.module("ping.joueurs", []);

/* 
 * Routage de ces contrôleurs
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/clubs/:numero/joueurs', {
    				templateUrl: 'partials/joueurs.html',
    				controller: 'JoueursCtrl'
  				})
  		}
])

/**
 * Controleur du menu de navigation
 */
modClubs.controller("JoueursCtrl", ["$scope", "$routeParams", "ComposantJoueurs", function($scope, $routeParams, ComposantJoueurs) {
  $scope.joueurs = ComposantJoueurs.rechercherJoueurs($routeParams.numero).joueurs;
}]);

/**
 * Service rechercherListe
 * Enregistre la lise en cours de saisie
 */
modClubs.factory("ComposantJoueurs", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
    rechercherJoueurs: function(numero) {
      return toolbox_http.get("/clubs/" + numero + "/joueurs");
    }
   }
}]);