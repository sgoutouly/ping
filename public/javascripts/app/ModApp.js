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
  "ping.accueil",
	"ping.clubs",
	"ping.club",
	"ping.joueurs",
	"ping.joueur"
]);






