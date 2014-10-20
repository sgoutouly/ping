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
    				templateUrl: '/assets/partials/joueur.html',
    				controller: 'JoueurCtrl'
  				}).
          when('/clubs/:numero/joueurs', {
            templateUrl: '/assets/partials/joueurs.html',
            controller: 'JoueurCtrl'
          }).
          when('/rechJoueurs', {
            templateUrl: '/assets/partials/joueurs.html',
            controller: 'JoueurCtrl'
          }).
          otherwise({
            templateUrl: '/assets/partials/joueur.html',
            controller: 'JoueurCtrl'
          });
  		}
])

/**
 * Le Controleur
 */
modJoueur.controller("JoueurCtrl", ["$scope", "$routeParams", "ComposantJoueur", function($scope, $routeParams, ComposantJoueur) {

  $scope.messageWait = "";
  $scope.licence = $routeParams.licence;
  $scope.nom = $routeParams.nom;
  $scope.prenom = $routeParams.prenom;

  if ($scope.licence !== undefined) {
    $scope.messageWait = "Chargement des données ..."
    ComposantJoueur.consulterJoueur($scope.licence).then(
      function(joueur) {
        $scope.joueur = joueur;
        $scope.messageWait = "";
      }
    );  
  }
  else if($scope.nom !== undefined) {
    $scope.messageWait = "Chargement des données ..."
    ComposantJoueur.rechercherJoueurs($scope.nom).then(
      function(data) {
        $scope.joueurs = data.joueurs;
        $scope.messageWait = "";
      }
    ); 
  }

  $scope.rechercher = function() {
    if ($scope.licence !== undefined) { 
      location.hash = "#/joueurs/" + $scope.licence; 
    }
    else if ($scope.nom !== undefined) { 
      location.hash = "#/rechJoueurs?nom=" + $scope.nom + "&prenom=" + ($scope.prenom !== undefined ? $scope.prenom : ""); 
    }
  }

  $scope.consulter = function(licence) {
    location.hash = "#/joueurs/" + licence; 
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
    ,
    rechercherJoueurs: function(nom, prenom) {
      var joueurs = toolbox_http.get("/joueurs?n=" + nom + "&p=" + (prenom !== undefined ? prenom : ""));
      return joueurs;
    }
  }
}]);