/**
 * Module contenant le CU Joueur de l'application
 */
var modJoueur = angular.module("ping.joueur", []);

/*
 * Routage
 */
pingApp.config(['$routeProvider',
		function($routeProvider) {
        $routeProvider. // Joueur pour une licence donnée
            when('/joueurs/:licence', {
                templateUrl: '/assets/partials/joueur.html',
                controller: 'JoueurCtrl'
            }). // Les des joueurs pour un club donné
            when('/clubs/:club/joueurs', {
                templateUrl: '/assets/partials/joueurs.html',
                controller: 'JoueurCtrl'
            }). // route de resultats de recherche multi critères
            when('/criteresRechJoueur', {
                templateUrl: '/assets/partials/rechJoueur.html',
                controller: 'JoueurCtrl'
            }). // route de resultats de recherche multi critères
            when('/rechJoueurs', {
                templateUrl: '/assets/partials/joueurs.html',
                controller: 'JoueurCtrl'
            });
   		}
])

/**
 * Le Controleur
 */
modJoueur.controller("JoueurCtrl", ["$scope", "$routeParams", "ComposantJoueur", function($scope, $routeParams, ComposantJoueur) {

  $scope.joueurs = undefined;
  $scope.joueur = undefined;
  $scope.messageWait = "";
  $scope.licence = $routeParams.licence;
  $scope.nom = $routeParams.nom;
  $scope.prenom = $routeParams.prenom;
  $scope.club = $routeParams.club;

  /* Actions exécutées àl'ouverture de la page (au lancement du contrôleur) */

  // Affichage du joueur associé à la licence
  if ($scope.licence !== undefined) {
    $scope.messageWait = "Chargement du joueur ..."
    ComposantJoueur.consulterJoueur($scope.licence).then(
      function(joueur) {
        $scope.joueur = joueur;
        $scope.messageWait = "";
      }
    );
  } // Recherche par nom / prenom
  else if($scope.nom !== undefined) {
    $scope.messageWait = "Chargement des joueurs ..."
    ComposantJoueur.rechercherJoueurs($scope.nom, $scope.prenom).then(
      function(data) {
        $scope.joueurs = data.joueurs;
        $scope.messageWait = "";
      }
    );
  }// Recherche par club
  else if($scope.club !== undefined) {
     $scope.messageWait = "Chargement des joueurs ..."
     ComposantJoueur.rechercherParClub($scope.club).then(
     function(data) {
       $scope.joueurs = data.joueurs;
       $scope.messageWait = "";
     }
   );
  };


  /* Action lancée depuis l'IHM */

  // toutes recherches
  $scope.lancerRecherche = function() {
    if ($scope.licence !== undefined) {
      location.hash = "#/joueurs/" + $scope.licence;
    }
    else if ($scope.nom !== undefined) {
      location.hash = "#/rechJoueurs?nom=" + $scope.nom + "&prenom=" + ($scope.prenom !== undefined ? $scope.prenom : "");
    }
    else if ($scope.club !== undefined) {
      location.hash = "#/clubs/" + $scope.club + "/joueurs";
    }
  };

  // Consultation via numero de licence
  $scope.consulter = function(licence) {
    location.hash = "#/joueurs/" + licence;
  };

  // Joueurs par club
  $scope.joueursParClub = function(club) {
    location.hash = "#/clubs/" + club + "/joueurs";
  };

  // Retour à la recherche
  $scope.fermer = function() {
    location.hash = "#/joueurs/";
  };

}]);

/**
 * La collection de services
 */
modJoueur.factory("ComposantJoueur", ["$q", "toolbox_http", function($q, toolbox_http) {
   return {
        // Consulter par numéro
        consulterJoueur: function(licence) {
          var joueur = toolbox_http.get("/joueurs/" + licence);
          return joueur;
        }
        ,
        // Recherche par nom / prenom
        rechercherJoueurs: function(nom, prenom) {
          var joueurs = toolbox_http.get("/joueurs?n=" + nom + "&p=" + prenom);
          return joueurs;
        }
        ,
        // Recherche par club
        rechercherParClub: function(club) {
          var joueurs = toolbox_http.get("/clubs/" + club + "/joueurs");
          return joueurs;
        }
    }
}]);