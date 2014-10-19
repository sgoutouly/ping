/**
 * Module contenant le CU accueil de l'application
 */
var modJoueur = angular.module("ping.joueur", []);

/* 
 * Routage de ces contrôleurs
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/joueurs/:licence', {
    				templateUrl: 'partials/joueur.html',
    				controller: 'JoueurCtrl'
  				})
  		}
])

/**
 * Controleur
 */
modJoueur.controller("JoueurCtrl", ["$scope", "$routeParams", "ComposantJoueur", function($scope, $routeParams, ComposantJoueur) {
  $scope.messageWait = "Chargement des données ..."
  $scope.licence = $routeParams.licence;
  ComposantJoueur.consulterJoueur($scope.licence).then(
    function(joueur) {
      $scope.joueur = joueur;
      $scope.messageWait = "";
    }
  );  
  $scope.rechercher = function() {
    $scope.messageWait = "Chargement des données ..."
    ComposantJoueur.consulterJoueur($scope.licence).then(
      function(joueur) {
        $scope.joueur = joueur;
        $scope.messageWait = "";
      }
    ); 
  }  
}]);

/**
 * Service rechercherListe
 */
modJoueur.factory("ComposantJoueur", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
    consulterJoueur: function(licence) {
      var joueur = toolbox_http.get("/joueurs/" + licence);
      return joueur;
    }
   }
}]);