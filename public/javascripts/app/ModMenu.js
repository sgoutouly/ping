/**
 * Module contenant le menu de l'application
 */
var modMenu = angular.module("common.menu", []);
/**
 * Controleur du menu de navigation
 */
modMenu.controller("MenuCtrl", ["$scope", "$location", function($scope, $location){
	$scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'menu';
        return page === currentRoute ? 'item-actif' : '';
    };        
}]);