dwApp.controller("TRCtrl", ["reqGen", "authService", function(reqGen, authService) {
	var lS = this;
	
	lS.authService = authService;
	
	lS.trList = undefined;
	lS.param = {
			id: undefined,
			type: "tr",
			code: undefined,
			description: undefined,
			value: undefined
	};
	
	lS.error = false;
	lS.responseMsg = "";
	lS.showEditForm = false;
	lS.editNew = false;
	

	lS.getTaxRates = function() {
		lS.responseMsg = "";
		reqGen.getParamsByType(lS.param.type).then(function(response) {
			lS.trList = response.data;
		}).
		catch(function(response) {
			lS.trList = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.editTaxRate = function(id) {
		lS.showEditForm = true;
		lS.resetFormFields();
		lS.error = true;
		lS.responseMsg = "";
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getParamById(id).then(function(response) {
				lS.param = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.verifyTaxRate = function() {
		if(lS.param.description != undefined && lS.param.value != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};

	lS.saveTaxRate = function() {
		lS.responseMsg = "";
		reqGen.saveParam(lS.param).then(function(response) {
			lS.showEditForm = false;
			lS.editNew = false;
			lS.getTaxRates();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteTaxRate = function(id, descr, value) {
		lS.responseMsg = "";
		var conf = undefined;
		reqGen.existItemsByTR(descr, value).then(function(response) {
			if(response.data == true) {
				conf = confirm("Zarejestrowano pozycje dokumentów z tą stawką, czy na pewno usunąć? (dane na dokumentach pozostaną nie zmienione)");
			} else {
				conf = confirm("Potwierdz usunięcie stawki podatku") 
			}
		}).then(function(response) {
			reqGen.deleteParamById(id).then(function(response) {
				lS.getTaxRates();
			})
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.cancelEditTaxRate = function() {
		lS.responseMsg = "";
		lS.error = false;
		lS.showEditForm = false;
		lS.editNew = false;
	}
	
	lS.resetFormFields = function() {
		lS.param.id = undefined;
		lS.param.type = "tr";
		lS.param.code = undefined;
		lS.param.description = undefined;
		lS.param.value = undefined;
	};
	
	lS.resetFormFields();
	lS.getTaxRates();
}]);



dwApp.controller("PMCtrl", ["reqGen", "authService", function(reqGen, authService) {
	var lS = this;
	
	lS.authService = authService;

	lS.pmList = undefined;
	lS.param = {
			id: undefined,
			type: "pm",
			code: undefined,
			description: undefined,
			value: undefined
	};
	
	lS.error = false;
	lS.responseMsg = "";
	lS.showEditForm = false;
	lS.editNew = false;

	lS.getPaymentMethods = function() {
		lS.responseMsg = "";
		reqGen.getParamsByType(lS.param.type).then(function(response) {
			lS.pmList = response.data;
		}).
		catch(function(response) {
			lS.pmList = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		});
	};
	
	lS.editPaymentMethod = function(id) {
		lS.showEditForm = true;
		lS.resetFormFields();
		lS.error = true;
		lS.responseMsg = "";
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getParamById(id).then(function(response) {
				lS.param = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.verifyPaymentMethod = function() {
		if(lS.param.description != undefined && lS.param.value != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};

	lS.savePaymentMethod = function() {
		lS.responseMsg = "";
		reqGen.saveParam(lS.param).then(function(response) {
			lS.showEditForm = false;
			lS.editNew = false;
			lS.getPaymentMethods();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deletePaymentMethod = function(id, descr) {
		lS.responseMsg = "";
		var conf = undefined;
		reqGen.existDocsByPM(descr).then(function(response) {
			if(response.data == true) {
				conf = confirm("Zarejestrowano dokumenty z tą metodą płatności, czy na pewno usunąć? (dane na dokumentach pozostaną nie zmienione)");
			} else {
				conf = confirm("Potwierdz usunięcie metody płatności") 
			}
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		}).then(function(response) {
			if(conf) {
				reqGen.deleteParamById(id).then(function(response) {
					lS.getPaymentMethods();
				})
			}
		})
	};

	lS.cancelEditPaymentMethod = function() {
		lS.responseMsg = "";
		lS.error = false;
		lS.showEditForm = false;
		lS.editNew = false;
	}

	lS.resetFormFields = function() {
		lS.param.id = undefined;
		lS.param.type = "pm";
		lS.param.code = undefined;
		lS.param.description = undefined;
		lS.param.value = undefined;
	};

	lS.getPaymentMethods();
}]);

dwApp.controller("UTCtrl", ["reqGen", "authService", function(reqGen, authService) {
	var lS = this;
	
	lS.authService = authService;

	lS.utList = undefined;
	lS.param = {
			id: undefined,
			type: "ut",
			code: undefined,
			description: undefined,
			value: undefined
	};
	
	lS.error = false;
	lS.responseMsg = "";
	lS.showEditForm = false;
	lS.editNew = false;

	lS.getUnitTypes = function() {
		lS.responseMsg = "";
		reqGen.getParamsByType(lS.param.type).then(function(response) {
			lS.utList = response.data;
		}).
		catch(function(response) {
			lS.utList = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.editUnitType = function(id) {
		lS.showEditForm = true;
		lS.resetFormFields();
		lS.error = true;
		lS.responseMsg = "";
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getParamById(id).then(function(response) {
				lS.param = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.verifyUnitType = function() {
		if(lS.param.code != undefined && lS.param.description != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};

	lS.saveUnitType = function() {
		lS.responseMsg = "";
		reqGen.saveParam(lS.param).then(function(response) {
			lS.showEditForm = false;
			lS.editNew = false;
			lS.getUnitTypes();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteUnitType = function(id, descr) {
		lS.responseMsg = "";
		var conf = undefined;
		reqGen.existItemsByUT(descr).then(function(response) {
			if(response.data == true) {
				conf = confirm("Zarejestrowano pozycje dokumentów z tą jednostką, czy na pewno usunąć? (dane na dokumentach pozostaną nie zmienione)");
			} else {
				conf = confirm("Potwierdz usunięcie jednostki miary") 
			}
		}).then(function(response) {
			reqGen.deleteParamById(id).then(function(response) {
				lS.getUnitTypes();
			})
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};

	lS.cancelEditUnitType = function() {
		lS.responseMsg = "";
		lS.error = false;
		lS.showEditForm = false;
		lS.editNew = false;
	}

	lS.resetFormFields = function() {
		lS.param.id = undefined;
		lS.param.type = "ut";
		lS.param.code = undefined;
		lS.param.description = undefined;
		lS.param.value = undefined;
	};

	lS.resetFormFields();
	lS.getUnitTypes();
}]);

