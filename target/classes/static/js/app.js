var dwApp = angular.module("dwApp", ["authModule"]);

dwApp.controller("mainCtrl", ["authService", "$window", function(authService, $window) {
	var lS = this;
	
	lS.authService = authService;
	
	lS.authService.getSession().then(function(response) {
		lS.authService.setSession(response.data);
		if(lS.authService.session.name != "null") {
			lS.authService.authenticated = true;
		}
	}).
	catch(function(response) {
		alert("Wystąpił błąd przy próbie pobrania sesji");
	});
	
	lS.login = function() {
		$window.location.href = "/login";
	}
}]);

dwApp.filter("trusted", ["$sce", function($sce) {
	return function(url) {
		return $sce.trustAsResourceUrl(url);
	}
}]);

dwApp.directive("ngFileSelect", function() {
	return {
		link: function($scope, el) {
			el.bind("change", function(e) {
				var file = (e.srcElement || e.target).files[0];
				$scope.getFile(file);
				el.val(null);
			})
		}
	}
});

dwApp.factory("sorting", function() {
	var sorting = {};
	
	sorting.sortType = "";
	sorting.sortReverse = false;
	
	sorting.changeSorting = function(col) {
		if(sorting.sortType == col) {
			sorting.sortReverse = !sorting.sortReverse;
		} else {
			sorting.sortType = col;
		}
	};
	
	sorting.displaySortType = function(col) {
		if(sorting.sortType == col) {
			if(sorting.sortReverse) {
				return "down";
			} else {
				return "up";
			}
		} else {
			return "none";
		}
	};
	
	return sorting;
});

dwApp.factory("fileReader", ["$q", function($q) {
	var onLoad = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.resolve(reader.result);
			})
		} 
	};
	
	var onError = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.reject(reader.result);
			})
		}
	};
	
	var getReader = function(deferred, scope) {
		var reader = new FileReader();
		reader.onload = onLoad(reader, deferred, scope);
		reader.onerror = onError(reader, deferred, scope);
		return reader;
	};
	
	var readAsDataURL = function(file, scope) {
		var deferred = $q.defer();
		var reader = getReader(deferred, scope);
		reader.readAsDataURL(file);
		return deferred.promise;
	};
	
	var readAsArrayBuffer = function(file, scope) {
		var deferred = $q.defer();
		var reader = getReader(deferred, scope);
		reader.readAsArrayBuffer(file);
		return deferred.promise;
	};
	
	return {
		readAsDataUrl: readAsDataURL,
		readAsArrayBuffer: readAsArrayBuffer
	}
}]);
