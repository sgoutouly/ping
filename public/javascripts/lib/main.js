var pingApp=angular.module("ping",["ngRoute","ngAnimate","ToolBox.directives","ToolBox.services","common.menu","common.errors","ping.club","ping.joueur"]),modClub=angular.module("ping.club",[]);pingApp.config(["$routeProvider",function(a){a.when("/clubs/:numero",{templateUrl:"/assets/partials/club.html",controller:"ClubCtrl"})}]),modClub.controller("ClubCtrl",["$scope","$routeParams","ComposantClub",function(a,b,c){a.club=c.consulterClub(b.numero)}]),modClub.factory("ComposantClub",["$q","toolbox_http",function(a,b){return{consulterClub:function(a){return b.get("/clubs/"+a)}}}]);var modErrors=angular.module("common.errors",[]);pingApp.config(["$routeProvider",function(a){a.when("/404",{templateUrl:"/assets/partials/errors.html",controller:"ErrorCtrl"}).otherwise({redirectTo:"/joueurs/"})}]),pingApp.run(["$rootScope","$location",function(a,b){a.$on("$routeChangeError",function(){b.path("/404").replace()})}]),modErrors.controller("ErrorCtrl",["$scope",function(){}]);var modJoueur=angular.module("ping.joueur",[]);pingApp.config(["$routeProvider",function(a){a.when("/joueurs/:licence",{templateUrl:"/assets/partials/joueur.html",controller:"JoueurCtrl"}).when("/clubs/:numero/joueurs",{templateUrl:"/assets/partials/joueurs.html",controller:"JoueurCtrl"}).when("/rechJoueurs",{templateUrl:"/assets/partials/joueurs.html",controller:"JoueurCtrl"}).otherwise({templateUrl:"/assets/partials/rechJoueur.html",controller:"JoueurCtrl"})}]),modJoueur.controller("JoueurCtrl",["$scope","$routeParams","ComposantJoueur",function(a,b,c){a.messageWait="",a.licence=b.licence,a.nom=b.nom,a.prenom=b.prenom,void 0!==a.licence?(a.messageWait="Chargement des données ...",c.consulterJoueur(a.licence).then(function(b){a.joueur=b,a.messageWait=""})):void 0!==a.nom&&(a.messageWait="Chargement des données ...",c.rechercherJoueurs(a.nom,a.prenom).then(function(b){a.joueurs=b.joueurs,a.messageWait=""})),a.rechercher=function(){void 0!==a.licence?location.hash="#/joueurs/"+a.licence:void 0!==a.nom&&(location.hash="#/rechJoueurs?nom="+a.nom+"&prenom="+(void 0!==a.prenom?a.prenom:""))},a.consulter=function(a){location.hash="#/joueurs/"+a},a.fermer=function(){location.hash="#/joueurs/"}}]),modJoueur.factory("ComposantJoueur",["$q","toolbox_http",function(a,b){return{consulterJoueur:function(a){var c=b.get("/joueurs/"+a);return c},rechercherJoueurs:function(a,c){var d=b.get("/joueurs?n="+a+"&p="+c);return d}}}]);var modMenu=angular.module("common.menu",[]);modMenu.controller("MenuCtrl",["$scope","$location",function(a,b){a.navClass=function(a){var c=b.path().substring(1)||"menu";return a===c?"item-actif":""}}]);var toolBoxServices=angular.module("ToolBox.services",[]);toolBoxServices.factory("toolbox_http",["$http",function(a){function b(a){alert("Erreur : "+a.status+", "+a.data)}return{get:function(c){return a.get(c).then(function(a){return a.data},function(a){b(a)})},put:function(c,d){return a.put(c,d).then(function(a){return a.data},function(a){b(a)})},post:function(c,d){return a.post(c,d).then(function(a){return a.headers()},function(a){b(a)})},"delete":function(c){return a.delete(c).then(function(a){return a.data},function(a){b(a)})}}}]);var toolBoxDirectives=angular.module("ToolBox.directives",[]);toolBoxDirectives.directive("tbMenuLeft",function(){return{link:function(a,b){function c(a){b.removeClass("animate"),k=i(a).X,initialleft=b.hasClass("show")?0:-j,30+initialleft+j>k&&(b.on("touchmove",d),b.on("touchend",e))}function d(a){a.stopImmediatePropagation(),a.preventDefault(),l=i(a).X,initialleft=b.hasClass("show")?0:-j;var c=l+initialleft-k;c>0?c=0:-1*j>c&&(c=-1*j),b.prop("style").webkitTransform="translateX("+c+"px)"}function e(a){var c=l+initialleft-k;c>-1*Math.round(j/2)?(b.prop("style").webkitTransform="",b.addClass("animate"),b.addClass("show"),$shadow.addClass("show"),h()):g(a)}function f(a){a.preventDefault(),b.addClass("animate"),b.toggleClass("show"),$shadow.toggleClass("show"),h()}function g(){b.prop("style").webkitTransform="",b.addClass("animate"),b.removeClass("show"),$shadow.removeClass("show"),h()}function h(){b.off("touchmove"),b.off("touchend")}function i(a){var b,c={},d=a||window.event;return a.touches?b=a.touches:a.originalEvent&&(b=d.originalEvent.changedTouches),b?(c.X=b[0].clientX,c.Y=b[0].clientX):(c.X=d.clientX,c.Y=d.clientX),c}var j=b.prop("offsetWidth"),k=0,l=0;bouton=document.getElementById("menuBouton"),bouton&&angular.element(bouton).on("click",f),b.on("touchstart",c),b.on("click",g),$shadow=angular.element("<div class='menu-shadow'></div>"),$shadow.on("click",g),b.parent().append($shadow)}}});