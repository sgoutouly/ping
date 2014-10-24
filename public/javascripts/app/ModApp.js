/**
 * Assemblage des modules controleurs et services
 */
var pingApp = angular.module("ping", [
	"ngRoute", 
	"ngAnimate", 
	"ToolBox.directives", 
  	"ToolBox.services", 
  	"common.menu",
	"common.errors",
	"ping.club", 
	"ping.joueur",
	"ping.equipe"
]);






