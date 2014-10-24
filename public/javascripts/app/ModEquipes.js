/**
 * Module contenant le CU Joueur de l'application
 */
var modEquipe = angular.module("ping.equipe", []);

/*
 * Routage
 */
pingApp.config(['$routeProvider',
		function($routeProvider) {
        $routeProvider.// route des équipes de ce club
          when('/clubs/:club/equipes',  {
               templateUrl: '/assets/partials/equipes.html',
               controller: 'EquipeCtrl'
          }).
          when('/classement/equipe',  {
               templateUrl: '/assets/partials/classement.html',
               controller: 'EquipeCtrl'
          })
   		}
]);

/**
 * Le Controleur
 */
modEquipe.controller("EquipeCtrl", ["$scope", "$routeParams", "ComposantEquipe", function($scope, $routeParams, ComposantEquipe) {

  $scope.equipes = undefined;
  $scope.classement = undefined;

  $scope.messageWait = "";
  $scope.club = $routeParams.club;
  $scope.division = $routeParams.division;
  $scope.poule = $routeParams.poule;

  /* Actions exécutées à l'ouverture de la page (au lancement du contrôleur) */

  // Affichage des équipes
  if ($scope.club !== undefined) {
      $scope.messageWait = "Chargement des équipes ..."
      ComposantEquipe.rechercherEquipes($scope.club).then(
        function(data) {
          $scope.equipes = data.equipes;
          $scope.messageWait = "";
        }
      );
  } //Consultation du classement
  else if ($scope.division !== undefined && $scope.poule !== undefined) {
      $scope.messageWait = "Chargement des classements ..."
      ComposantEquipe.consulterClassement($scope.division, $scope.poule).then(
        function(data) {
          $scope.classement = data.classement;
          $scope.messageWait = "";
        }
      );
  };

  /* Action lancée depuis l'IHM */

   // classement d'une équipe
  $scope.classement = function(division, poule) {
    location.hash = "#/classement/equipe?division=" + division + "&poule=" + poule;
  };


}]);

/**
 * La collection de services
 */
modEquipe.factory("ComposantEquipe", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
        // Recherche par club
        rechercherEquipes: function(club) {
          var equipes = toolbox_http.get("/clubs/" + club + "/equipes");
          return equipes;
        }
        ,
        // classement
        consulterClassement: function(division, poule) {
          var equipes = toolbox_http.get("/classements/equipe?division=" + division + "&poule=" + poule);
          return equipes;
        }
    }
}]);