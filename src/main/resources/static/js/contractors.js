dwApp.controller("ContrsCtrl", ["reqGen", "authService", "sorting", function(reqGen, authService, sorting) {
	var lS = this;
	
	lS.authService = authService;
	lS.sorting = sorting;
	
	lS.contrList = undefined;
	lS.contr = {
				id: undefined,
				name: undefined,
				address: undefined,
				regNumber: undefined,
				contactDetails: undefined
	};
	
	lS.error = false;
	lS.responseMsg = "";
	
	lS.showContrEditForm = false;
	lS.editNew = false;
	
	lS.getContrs = function() {
		lS.responseMsg = "";
		reqGen.getContrs().then(function(response) {
			lS.contrList = response.data;
		}).
		catch(function(response) {
			lS.contrList = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.editContr = function(id) {
		lS.showContrEditForm = true;
		lS.resetContrFormFields();
		lS.error = true;
		lS.responseMsg = "";
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getContrById(id).then(function(response) {
				lS.contr = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.verifyContr = function() {
		if(lS.contr.name != undefined && lS.contr.address != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};
	
	lS.saveContr = function() {
		lS.responseMsg = "";
		reqGen.saveContr(lS.contr).then(function(response) {
			lS.showContrEditForm = false;
			lS.editNew = false;
			lS.getContrs();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteContr = function(id) {
		if(confirm("Potwierdz usunięcie kontrahenta")) {
			lS.responseMsg = "";
			reqGen.deleteContrById(id).then(function(response) {
				lS.getContrs();
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.cancelEditContr = function() {
		lS.responseMsg = "";
		lS.error = false;
		lS.showContrEditForm = false;
		lS.editNew = false;
	};
	
	lS.resetContrFormFields = function() {
		lS.contr.id = undefined;
		lS.contr.name = undefined;
		lS.contr.address = undefined;
		lS.contr.regNumber = undefined;
		lS.contr.contactDetails = undefined;
	};
	
	lS.resetContrFormFields();
	lS.sorting.sortType = "name";
	lS.sorting.sortReverse = false;
	lS.getContrs();
}])