/**
 * Module contenant le CU accueil de l'application
 */
var modClub = angular.module("ping.club", []);

/*
 * Routage
 */
pingApp.config(['$routeProvider',
		function($routeProvider) {
        $routeProvider. // Joueur pour une licence donnée
            when('/clubs/:num', {
                templateUrl: '/assets/partials/club.html',
                controller: 'ClubCtrl'
            }).// route de resultats de recherche multi critères
            when('/criteresRechClub', {
                templateUrl: '/assets/partials/rechClub.html',
                controller: 'ClubCtrl'
            }). // route de resultats de recherche multi critères
            when('/rechClubs', {
                templateUrl: '/assets/partials/clubs.html',
                controller: 'ClubCtrl'
            })
   		}
])

/**
 * Le Controleur
 */
modClub.controller("ClubCtrl", ["$scope", "$routeParams", "ComposantClub", function($scope, $routeParams, ComposantClub) {

  $scope.clubs = undefined;
  $scope.club = undefined;
  $scope.messageWait = "";
  $scope.deptClub = $routeParams.dept;
  $scope.numClub = $routeParams.num;

  /* Actions exécutées àl'ouverture de la page (au lancement du contrôleur) */

  // Affichage du club associé au numero
  if ($scope.numClub !== undefined) {
    $scope.messageWait = "Chargement du club ..."
    ComposantClub.consulterClub($scope.numClub).then(
      function(club) {
        if (club.idclub !== "") {
            $scope.club = club;
        }
        $scope.messageWait = "";
      }
    );
  } // Recherche par dept
  else if($scope.deptClub !== undefined) {
    $scope.messageWait = "Chargement des clubs ..."
    ComposantClub.rechercherClubs($scope.deptClub).then(
      function(data) {
        if(data.clubs.length >= 1) {
            $scope.clubs = data.clubs;
        }
        $scope.messageWait = "";
      }
    );
  };

  /* Action lancée depuis l'IHM */

  // toutes recherches
  $scope.lancerRecherche = function() {
    if ($scope.numClub !== undefined) {
      location.hash = "#/clubs/" + $scope.numClub;
    }
    else if ($scope.deptClub !== undefined) {
      location.hash = "#/rechClubs?dept=" + $scope.deptClub;
    }
  };

  // Consultation via numero
  $scope.consulter = function(numClub) {
    location.hash = "#/clubs/" + numClub;
  };

// Consultation licenciés
  $scope.licencies = function() {
    location.hash = "#/clubs/" + $scope.numClub + "/joueurs";
  };

  // Consultation équipes
  $scope.equipes = function() {
    location.hash = "#/clubs/" + $scope.numClub + "/equipes";
  };

  // Retour à la recherche
  $scope.fermer = function() {
    location.hash = "#/criteresRechClub";
  };

}]);

/**
 * La collection de services
 */
modClub.factory("ComposantClub", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
        // Consulter par numéro
        consulterClub: function(numClub) {
          var club = toolbox_http.get("/clubs/" + numClub);
          return club;
        }
        ,
        // Recherche par nom / prenom
        rechercherClubs: function(deptClub) {
          var clubs = toolbox_http.get("/clubs?d=" + deptClub);
          return clubs;
        }
    }
}]);