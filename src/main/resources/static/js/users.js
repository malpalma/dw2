dwApp.controller("UsersCtrl", ["reqGen", "authService", function(reqGen, authService) {
	var lS = this;
	
	lS.authService = authService;
	
	lS.userList = undefined;
	lS.user = {
			id: undefined,
			name: undefined,
			pass: undefined,
			active: false,
			canEdit: false,
			canAccept: false,
			isAdmin: false
	}

	lS.error = false;
	lS.responseMsg = "";

	lS.showEditForm = false;
	lS.editNew = false;
	
	lS.showChPasswordForm = false;
	lS.newPassword = undefined;
	lS.repeatPassword = undefined;
	
	lS.getUsers = function() {
		lS.responseMsg = "";
		lS.showEditForm = false;
		lS.editNew = false;
		lS.showChPasswordForm = false;
		lS.error = false;
		reqGen.getUsers().then(function(response) {
			lS.userList = response.data;
		}).
		catch(function(response) {
			lS.userList = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
		})
	};
	
	lS.editUser = function(id) {
		lS.showEditForm = true;
		lS.showChPasswordForm = false;
		lS.resetFormFields();
		lS.error = true;
		lS.responseMsg = "";
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getUserById(id).then(function(response) {
				lS.user = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
			})
		}
	};
	
	lS.verifyUser = function() {
		if(lS.user.name != undefined && lS.user.pass != undefined) {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};

	lS.saveUser = function() {
		lS.responseMsg = "";
		reqGen.saveUser(lS.user).then(function(response) {
			lS.showEditForm = false;
			lS.editNew = false;
			lS.getUsers();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
		})
	};
	
	lS.deleteUser = function(id) {
		if(confirm("Potwierdz usunięcie użytkownika")) {
			lS.responseMsg = "";
			reqGen.deleteUserById(id).then(function(response) {
				lS.getUsers();
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
			})
		}
	};
	
	lS.cancelEditUser = function() {
		lS.getUsers();
	}
	
	lS.resetFormFields = function() {
		lS.user.id = undefined;
		lS.user.name = undefined;
		lS.user.pass = undefined;
		lS.user.active = false;
		lS.user.canEdit = false;
		lS.user.canAccept = false;
		lS.user.isAdmin = false;
	};
	
	lS.changePassword = function(id) {
		lS.showChPasswordForm = true;
		lS.newPassword = undefined;
		lS.repeatPassword = undefined;
		lS.user.id = id;
	}
	
	lS.setNewPassword = function() {
		reqGen.setNewPassword(lS.user.id, lS.newPassword).then(function(response) {
			lS.getUsers();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
		})
	}
	
	lS.resetFormFields();
	lS.getUsers();
}])