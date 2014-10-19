/**
 * Module contenant le CU Joueur de l'application
 */
var modJoueur = angular.module("ping.joueur", []);

/* 
 * Routage
 */	
pingApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
  				when('/joueurs/:licence', {
    				templateUrl: 'partials/joueur.html',
    				controller: 'JoueurCtrl'
  				}).
          when('/joueurs/', {
            templateUrl: 'partials/joueur.html',
            controller: 'JoueurCtrl'
          })
  		}
])

/**
 * Le Controleur
 */
modJoueur.controller("JoueurCtrl", ["$scope", "$routeParams", "ComposantJoueur", function($scope, $routeParams, ComposantJoueur) {
  $scope.messageWait = "";
  $scope.licence = $routeParams.licence;
  
  if ($scope.licence !== undefined) {
    $scope.messageWait = "Chargement des données ..."
    ComposantJoueur.consulterJoueur($scope.licence).then(
      function(joueur) {
        $scope.joueur = joueur;
        $scope.messageWait = "";
      }
    );  
  }
  
  $scope.rechercher = function() {
    if ($scope.licence !== undefined) { location.hash = "#/joueurs/" + $scope.licence; }
  }  
}]);

/**
 * La collection de services
 */
modJoueur.factory("ComposantJoueur", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
    consulterJoueur: function(licence) {
      var joueur = toolbox_http.get("/joueurs/" + licence);
      return joueur;
    }
   }
}]);