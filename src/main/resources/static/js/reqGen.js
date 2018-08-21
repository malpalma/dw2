dwApp.factory("reqGen", ["$http", function($http) {
	var reqGen = {};
	
	reqGen.getDocuments = function() {
		var config = {responseType: "json"};
		return $http.get("/getDocuments");
	};
	
	reqGen.getDocumentById = function(id) {
		var config = {params: {}, responseType: "json"};
 		return $http.get("/getDocument/" + id, config);
	};
	
	reqGen.getDocStatusById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.get("/getDocStatus/" + id, config);
	}
	
	reqGen.getDocUserById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.get("/getDocUser/" + id, config);
	}
	
	reqGen.saveDocument = function(doc) {
		var config = {responseType: "text"};
		return $http.post("/saveDocument", doc, config);
	};
	
	reqGen.deleteDocById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteDocument/" + id, config);
	};
	
	reqGen.existDocsByContractorData = function(name, address, regNumber) {
		var config = {params: {name: name, address: address, regNumber: regNumber}, responseType: "text"};
		return $http.get("/existDocsByContractorData", config);
	}
	
	reqGen.existDocsByPM = function(descr) {
		var config = {params: {descr: descr}, responseType: "text"};
		return $http.get("/existDocsByPM", config);
	}

	
	
	reqGen.getStagesByDocId = function(docId) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getStages/" + docId, config);
	};

	reqGen.saveStage = function(stage, docId, nextStageUser) {
		var config = {params : {nextStageUser: nextStageUser}, responseType: "text"};
		if(stage.status == "nowy") {
			return $http.post("/initDocWF/" + docId, stage, config);
		} else {
			return $http.post("/saveStage/" + docId, stage, config);
		}
	};

	
	
	reqGen.getItemsByDocId = function(docId) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getItems/" + docId, config);
	};
	
	reqGen.getItemById = function(id) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getItem/" + id, config);
	};
	
	reqGen.getGroupedItemsByDocId = function(docId) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getGroupedItems/" + docId, config);
	};
	
	reqGen.saveItem = function(item, docId) {
		var config = {params: {}, responseType: "text"};
		return $http.post("/saveItem/" + docId, item, config);
	};
	
	reqGen.deleteItemById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteItem/" + id, config);
	};

	reqGen.existItemsByTR = function(descr, value) {
		var config = {params: {descr: descr, value: value}, responseType: "text"};
		return $http.get("/existItemsByTR", config);
	}

	reqGen.existItemsByUT = function(descr) {
		var config = {params: {descr: descr}, responseType: "text"};
		return $http.get("/existItemsByUT", config);
	}


	
	reqGen.getSumsByDocId = function(docId) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getSums/" + docId, config);
	};
	
	reqGen.saveSum = function(sum, docId) {
		var config = {params: {}, responseType: "text"};
		return $http.post("/saveSum/" + docId, sum, config);
	};
	
	reqGen.deleteSumsByDocId = function(docId) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteSums/" + docId, config);
	};

	
	
	reqGen.getParamsByType = function(type) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getParams/" + type, config);
	};
	
	reqGen.getParamById = function(id) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getParam/" + id, config);
	};
	
	reqGen.getParamByTypeAndDescr = function(type, descr) {
		var config = {params: {descr: descr}, responseType: "json"};
		return $http.get("/getParamByDescr/" + type, config);
	};
	
	reqGen.saveParam = function(param) {
		var config = {responseType: "text"};
		return $http.post("/saveParam", param, config);
	};
	
	reqGen.deleteParamById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteParam/" + id, config);
	};
	
	
	
	reqGen.getUsers = function() {
		var config = {responseType: "json"};
		return $http.get("/getUsers", config);
	};
	
	reqGen.getUserById = function(id) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getUser/" + id, config);
	};
	
	reqGen.saveUser = function(user) {
		var config = {responseType: "text"};
// ... encoded password doesn't look like BCrypt ....
//		user.pass = btoa(user.pass);
		return $http.post("/saveUser", user, config);
	};
	
	reqGen.deleteUserById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteUser/" + id, config);
	};
	
	reqGen.getUsersWithPerm = function(perm, without) {
		var config = {params: {without: without}, responseType: "json"};
		return $http.get("/getUsers/" + perm, config);
	};
	
	reqGen.setNewPassword = function(id, pass) {
		var config = {params: {}, responseType: "text"};
// ... encoded password doesn't look like BCrypt ....
//		var data = btoa(pass);
		var data = pass;
		return $http.post('/setNewPassword/' + id, data, config);
	}
	
	
	
	
	reqGen.getContrs = function() {
		var config = {responseType: "json"};
		return $http.get("/getContractors", config);
	};
	
	reqGen.getContrById = function(id) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getContractor/" + id, config);
	};
	
	reqGen.saveContr = function(contr) {
		var config = {responseType: "text"};
		return $http.post("/saveContractor", contr, config);
	};
	
	reqGen.deleteContrById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteContractor/" + id, config);
	};
	
	
	
	reqGen.getAttachmentsByDocId = function(docId) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getAttachments/" + docId, config);
	};
	
	reqGen.getAttachById = function(id) {
		var config = {params: {}, responseType: "json"};
		return $http.get("/getAttach/" + id, config);
	};
	
	reqGen.saveAttach = function(attach, docId) {
		var params = {id: attach.id, descr: attach.description, fileName: attach.fileName, fileType: attach.fileType};
		var config = {params: params, responseType: "text"};
		return $http.post("/saveAttach/" + docId, attach.file, config);
	};
	
	reqGen.deleteAttachById = function(id) {
		var config = {params: {}, responseType: "text"};
		return $http.delete("/deleteAttach/" + id, config);
	};

	reqGen.getResponseMsg = function(resp) {
		if(resp.data != undefined && resp.data.status != undefined) {
			return "Wystąpił błąd: \n" + 
				resp.data.status + ", " + resp.data.error + ";\n" + 
				"komunikat: " + resp.data.message;
		} else {
			return "Wystąpił błąd";
		}
	};
	
	return reqGen;
}]);