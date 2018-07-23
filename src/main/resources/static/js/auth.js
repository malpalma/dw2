var authModule = angular.module("authModule", []);

authModule.config(["$httpProvider", function mainController($httpProvider) {
	$httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
}]);

authModule.factory("authService", ["$http", "$window", function($http, $window) {
	var authService = {};
	
	authService.error = false;
	authService.session = {};
	authService.credentials = {};
	authService.authenticated = false;
	
	authService.responseMsg = "";

	authService.authenticate = function() {
		authService.responseMsg = "";
		var headers = authService.credentials.username ? {authorization : "Basic " + btoa(authService.credentials.username + ":" + authService.credentials.password)} : {};
		$http.get("/authenticate", {headers : headers}).then(function(response) {
			if(response.data.name != "null") {
				authService.setSession(response.data);
				authService.error = false;
				authService.authenticated = true;
				$window.location.href = "/";
			} else {
				authService.error = true; 
				authService.responseMsg = "Nie udało się zalogować";
				alert(authService.responseMsg);
			}
		}).
		catch(function(response) {
			authService.error = true;
			authService.responseMsg = "Wystąpił błąd przy próbie zalogowania";
			alert(authService.responseMsg);
		})
	}
	
	authService.logout = function() {
		authService.responseMsg = "";
		$http.post("logout", {}).then(function() {
			$window.location.href = "/";
			authService.authenticated = false;
			authService.getSession().then(function(response) {
				authService.setSession(response.data);
			}).
			catch(function(response) {
				authService.responseMsg = "Błąd pobrania sesji po wylogowaniu";
				alert(authService.responseMsg);
			})
		}).
		catch(function(response) {
			authService.responseMsg = "Wystąpił błąd przy próbie wylogowania";
		})
	}

	authService.getSession = function() {
		return $http.get("/userSession");
	}
	
	authService.setSession = function(data) {
		authService.session = data;
	}
	
	return authService;
}]);

authModule.controller("authCtrl", ["$window", "$http", "authService", function($window, $http, authService) {
	var lS = this;
	
	lS.authService = authService;
	
	lS.authService.credentials.username = "super";
	lS.authService.credentials.password = "superPwd";
	lS.error = false;

	lS.verify = function() {
		if(lS.authService.credentials.username != undefined && lS.authService.credentials.password != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};
	
}])
