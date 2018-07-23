dwApp.controller("DocsCtrl", ["reqGen", "authService", "fileReader", "$scope", "sorting", function(reqGen, authService, fileReader, $scope, sorting) {
	var lS = this;
	
	lS.authService = authService;
	lS.sorting = sorting;

	lS.docList = undefined;
	lS.itemList = undefined;
	lS.sumList = undefined;
	lS.pmList = undefined;
	lS.utList = undefined;
	lS.trList = undefined;
	lS.contrList = undefined;
	lS.attachList = undefined;
	lS.userList = undefined;
	lS.stageList = undefined;

	lS.error = false;
	lS.responseMsg = "";
	lS.nextStageUser = undefined;
	lS.action = undefined;
	
	lS.doc = {
		id: undefined,
		invDt: undefined,
		invNo: undefined,
		sellersId: undefined,
		sellersName: undefined,
		sellsersAddress: undefined,
		sellersRegNumber: undefined,
		saleDt: undefined,
		dueDt: undefined,
		paymentMethod: undefined,
		bankAccNo: undefined,
		sellersContactDetails: undefined,
		gross: 0.00,
		status: undefined,
		usern: undefined
	};
	
	lS.selectedPM = {
		id: undefined,
		description: undefined,
		value: undefined
	};
	
	lS.selectedContr = {
		id: undefined,
		name: undefined,
		address: undefined,
		regNumber: undefined,
		contactDetails: undefined
	}
	
	lS.item = {
		id: undefined,
		description: undefined,
		quantity: 0.00,
		unitType: undefined,
		pricePerUnit: 0.00,
		discount: 0.00,
		price: 0.00,
		taxDescr: undefined,
		taxRate: 0.00
	};
	
	lS.selectedUT = {
		id: undefined,
		code: undefined,
		description: undefined
	};
	
	lS.selectedTR = {
		id: undefined,
		description: undefined,
		value: 0.00
	};
	
	lS.sum = {
		id: undefined,
		taxDescr: undefined,
		price: 0.00,
		taxValue: 0.00,
		gross: 0.00
	};
	
	lS.attach = {
		id: undefined,
		description: undefined,
		fileName: undefined,
		fileType: undefined,
		file: undefined
	}
	
	lS.stage = {
		usern: undefined,
		status: undefined,
		action: undefined,
		date: undefined,
		comments: undefined
	}
	
//==================================================================== DOCUMENT ====================================================================
	
	lS.getDocuments = function() {
		lS.resetView();
		lS.showDocuments = true;
		lS.docList = undefined;
		reqGen.getDocuments().then(function(response) {
			lS.docList = response.data;
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
		
	lS.editDoc = function(id) {
		lS.resetView();
		lS.showDocuments = true;
		lS.showDocEditForm = true;
		lS.resetDocFormFields();
		lS.resetDocRelatedLists();
		lS.error = true;
		reqGen.getParamsByType("pm").then(function(response) {
			lS.pmList = response.data;
			reqGen.getContrs().then(function(response) {
				lS.contrList = response.data;
				if(id == "new") {
					lS.editNew = true;
					lS.doc.status = "nowy";
				} else {
					lS.editNew = false;
		 			reqGen.getDocumentById(id).then(function(response) {
		 				lS.fillDocFields(response.data);
		 				lS.error = false;
		 				lS.getSumsByDocId(lS.doc.id);
		 				if(lS.doc.paymentMethod != undefined) {
			 				reqGen.getParamByTypeAndDescr("pm", lS.doc.paymentMethod).then(function(response) {
			 					lS.selectedPM = response.data;
			 				}).
			 				catch(function(response) {
			 					lS.responseMsg = reqGen.getResponseMsg(response);
				 				lS.error = true;
				 				alert(lS.responseMsg);
			 				})
		 				}
		 			}).
		 			catch(function(response) {
						lS.responseMsg = reqGen.getResponseMsg(response);
						alert(lS.responseMsg);
		 			})
				}
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.displayDocDetails = function(id) {
		lS.resetView();
		lS.showDocuments = true;
		lS.showDocDetails = true;
		lS.resetDocFormFields();
		lS.resetDocRelatedLists();
		reqGen.getDocumentById(id).then(function(response) {
			lS.fillDocFields(response.data);
			lS.getSumsByDocId(lS.doc.id);
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		});
	};
	
	lS.fillDocFields = function(data) {
		lS.doc = data;
		if(data.invDt == null) {
			lS.doc.invDt = undefined;
		} else {
			lS.doc.invDt = new Date(data.invDt);
		}
		if(data.saleDt == null) {
			lS.doc.saleDt = undefined;
		} else {
			lS.doc.saleDt = new Date(data.saleDt);
		}
		if(data.dueDt == null) {
			lS.doc.dueDt = undefined;
		} else {
			lS.doc.dueDt = new Date(data.dueDt);
		}
	}
	
	lS.saveDoc = function() {
		lS.responseMsg = "";
		reqGen.saveDocument(lS.doc).then(function(response) {
			lS.getDocuments();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteDoc = function(id) {
		if(confirm("Potwierdz usunięcie dokumentu")) {
			lS.responseMsg = "";
			reqGen.deleteDocById(id).then(function(response) {
				lS.getDocuments();
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
		
	lS.verifyDoc = function() {
		if(lS.doc.invNo != undefined && lS.doc.invNo != "" && 
				angular.isDate(lS.doc.invDt) && angular.isDate(lS.doc.saleDt) && 
				lS.doc.sellersName != undefined && lS.doc.sellersName != "" && 
				lS.doc.sellersAddress != undefined && lS.doc.sellersAddress != "") {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};
		
	lS.updateContr = function() {
		lS.responseMsg = "";
		if(lS.selectedContr.id != null) {
			reqGen.getContrById(lS.selectedContr.id).then(function(response) {
				lS.selectedContr = response.data;
				if(lS.selectedContr.id != lS.doc.sellersId) {
					lS.doc.sellersName = lS.selectedContr.name;
					lS.doc.sellersAddress = lS.selectedContr.address;
					lS.doc.sellersRegNumber = lS.selectedContr.regNumber;
					lS.doc.sellersContactDetails = lS.selectedContr.contactDetails;
					lS.verifyDoc();
				}
			}).
			catch(function(response) {
				lS.resetSelectedContr();
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.updatePM = function() {
		lS.responseMsg = "";
		if(lS.selectedPM.id != null) {
			reqGen.getParamById(lS.selectedPM.id).then(function(response) {
				lS.selectedPM = response.data;
				if(lS.selectedPM.description != lS.doc.paymentMethod) {
					lS.doc.paymentMethod = lS.selectedPM.description;
					lS.doc.dueDt = new Date(lS.doc.invDt);
					lS.doc.dueDt.setDate(lS.doc.dueDt.getDate() + lS.selectedPM.value);
					lS.verifyDoc();
				}
			}).
			catch(function(response) {
				lS.resetSelectedPM();
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
		
	lS.resetDocFormFields = function() {
		lS.doc.id = undefined;
		lS.doc.invDt = undefined;
		lS.doc.invNo = undefined;
		lS.doc.sellersId = undefined;
		lS.doc.sellersName = undefined;
		lS.doc.sellersAddress = undefined;
		lS.doc.sellersRegNumber = undefined;
		lS.doc.saleDt = undefined;
		lS.doc.dueDt = undefined;
		lS.doc.paymentMethod = undefined;
		lS.doc.bankAccNo = undefined;
		lS.doc.sellersContactDetails = undefined;
		lS.doc.gross = 0.00;
		lS.doc.status = undefined,
		lS.doc.usern = undefined,
		lS.resetSelectedPM();
		lS.resetSelectedContr();
	};
	
	lS.resetSelectedPM = function() {
		lS.selectedPM.id = undefined;
		lS.selectedPM.description = undefined;
		lS.selectedPM.value = undefined;
	};
	
	lS.resetSelectedContr = function() {
		lS.selectedContr.name = undefined;
		lS.selectedContr.address = undefined;
		lS.selectedContr.regNumber = undefined;
		lS.selectedContr.contactDetails = undefined;
	};
	
//==================================================================== DOCITEM ====================================================================

	lS.getItemsByDocId = function(docId) {
		lS.resetView();
		lS.showDocItems = true;
		lS.itemList = undefined;
		lS.resetItemRelatedLists();
		lS.doc.id = docId;
		reqGen.getDocStatusById(lS.doc.id).then(function(response) {
			lS.doc.status = response.data;
			reqGen.getItemsByDocId(lS.doc.id).then(function(response) {
				lS.itemList = response.data;
				lS.getSumsByDocId(lS.doc.id);
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}).
		catch(function(response) {
			lS.doc.status = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.editItem = function(id) {
		lS.resetView();
		lS.showDocItems = true;
		lS.showItemEditForm = true;
		lS.resetItemFormFields();
		lS.resetItemRelatedLists();
		lS.error = true;
		reqGen.getParamsByType("ut").then(function(response) {
			lS.utList = response.data;
			reqGen.getParamsByType("tr").then(function(response) {
				lS.trList = response.data;
				lS.getSumsByDocId(lS.doc.id);
				if(id == "new") {
					lS.editNew = true;
				} else {
					lS.editNew = false;
		 			reqGen.getItemById(id).then(function(response) {
		 				lS.item = response.data;
		 				lS.error = false;
		 				if(lS.item.unitType != undefined) {
			 				reqGen.getParamByTypeAndDescr("ut", lS.item.unitType).then(function(response) {
			 					lS.selectedUT = response.data;
			 				}).
			 				catch(function(response) {
			 					lS.responseMsg = reqGen.getResponseMsg(response);
			 					lS.error = true;
			 					alert(lS.responseMsg);
			 				})
		 				}
		 				if(lS.item.taxDescr != undefined) {
		 					reqGen.getParamByTypeAndDescr("tr", lS.item.taxDescr).then(function(response) {
		 						lS.selectedTR = response.data;
		 					}).
		 					catch(function(response) {
		 						lS.responseMsg = reqGen.getResponseMsg(response);
		 						lS.error = true;
		 						alert(lS.responseMsg);
		 					})
		 				}
		 			}).
		 			catch(function(response) {
						lS.responseMsg = reqGen.getResponseMsg(response);
						alert(lS.responseMsg);
		 			})
				}
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};

	lS.displayItemDetails = function(id) {
		lS.resetView();
		lS.showDocItems = true;
		lS.showItemDetails = true;
		lS.resetItemFormFields();
		lS.resetItemRelatedLists();
		reqGen.getItemById(id).then(function(response) {
			lS.item = response.data;
			lS.getSumsByDocId(lS.doc.id);
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};

	lS.saveItem = function() {
		lS.responseMsg = "";
		reqGen.saveItem(lS.item, lS.doc.id).then(function(response) {
			lS.getItemsByDocId(lS.doc.id);
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteItem = function(id) {
		if(confirm("Potwierdz usunięcie pozycji dokumentu")) {
			lS.responseMsg = "";
			reqGen.deleteItemById(id).then(function(response) {
				lS.getItemsByDocId(lS.doc.id);
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	}

	lS.verifyItem = function() {
		lS.compute();
		if(lS.item.description != undefined && lS.item.description != "" && 
				lS.item.quantity>0 && lS.item.unitType != undefined && lS.item.uniType != "") {
			lS.error = false;
		} else {
			lS.error = true;
		}
	};
	
	lS.compute = function() {
		if(lS.item.quantity != undefined && lS.item.pricePerUnit != undefined) {
			lS.item.quantity = Math.round(lS.item.quantity * 1e6) / 1e6;
			lS.item.pricePerUnit = Math.round(lS.item.pricePerUnit * 1e2) / 1e2;
			lS.item.price = lS.item.quantity * lS.item.pricePerUnit;
			lS.item.price = Math.round(lS.item.price * 1e2) / 1e2;
			if(lS.item.discount != undefined) {
				lS.item.discount = Math.round(lS.item.discount * 1e2) / 1e2;
				lS.item.price -= lS.item.discount;
			}
		}
	};

	lS.updateUT = function() {
		lS.responseMsg = "";
		if(lS.selectedUT.id != null) {
			reqGen.getParamById(lS.selectedUT.id).then(function(response) {
				lS.selectedUT = response.data;
				if(lS.selectedUT.description != lS.item.unitType) {
					lS.item.unitType = lS.selectedUT.description;
					lS.verifyItem();
				}
			}).
			catch(function(response) {
				lS.resetSelectedUT();
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};

	lS.updateTR = function() {
		lS.responseMsg = "";
		if(lS.selectedTR.id != null) {
			reqGen.getParamById(lS.selectedTR.id).then(function(response) {
				lS.selectedTR = response.data;
				if(lS.selectedTR.description != lS.item.taxDescr || lS.selectedTR.value != lS.item.taxRate) {
					lS.item.taxDescr = lS.selectedTR.description;
					lS.item.taxRate = lS.selectedTR.value;
					lS.verifyItem();
				}
			}).
			catch(function(response) {
				lS.resetSelectedTR();
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.backToItems = function() {
		lS.getItemsByDocId(lS.doc.id);
	};

	lS.resetItemFormFields = function() {
		lS.item.id = undefined;
		lS.item.description = undefined;
		lS.item.quantity = 0.00;
		lS.item.unitType = undefined;
		lS.item.pricePerUnit = 0.00;
		lS.item.discount = 0.00;
		lS.item.price = 0.00;
		lS.item.taxDescr = undefined;
		lS.item.taxRate = 0.00;
		lS.resetSelectedUT();
		lS.resetSelectedTR();
	};
	
	lS.resetSelectedUT = function() {
		lS.selectedUT.id = undefined;
		lS.selectedUT.code = undefined;
		lS.selectedUT.description = undefined;
	};
	
	lS.resetSelectedTR = function() {
		lS.selectedTR.id = undefined;
		lS.selectedTR.description = undefined;
		lS.selectedTR.value = 0.00;
	};
	
//==================================================================== DOCSUM ====================================================================

	lS.getSumsByDocId = function(docId) {
		lS.responseMsg = "";
		reqGen.getSumsByDocId(docId).then(function(response) {
			lS.sumList = response.data;
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
		
//==================================================================== ATTACHMENT ====================================================================

	$scope.getFile = function(file) {
		fileReader.readAsDataUrl(file, $scope).then(function(response) {
			lS.attach.fileName = file.name;
			var br = response.indexOf(",");
			lS.attach.fileType = response.substr(0, br);
			lS.attach.file = response.substr(br + 1);
			lS.verifyAttach();
		})
	};
	
	lS.getAttachmentsByDocId = function(docId) {
		lS.resetView();
		lS.showDocAttachments = true;
		lS.attachList = undefined;
		lS.resetAttachRelatedLists();
		lS.doc.id = docId;
		reqGen.getDocStatusById(lS.doc.id).then(function(response) {
			lS.doc.status = response.data;
			reqGen.getAttachmentsByDocId(docId).then(function(response) {
				lS.attachList = response.data;
				lS.getSumsByDocId(lS.doc.id);
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}).
		catch(function(response) {
			lS.doc.status = undefined;
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.editAttach = function(id) {
		lS.resetView();
		lS.showDocAttachments = true;
		lS.showAttachEditForm = true;
		lS.resetAttachFormFields();
		lS.resetAttachRelatedLists();
		lS.error = true;
		lS.getSumsByDocId(lS.doc.id);
		if(id == "new") {
			lS.editNew = true;
		} else {
			lS.editNew = false;
			reqGen.getAttachById(id).then(function(response) {
				lS.attach = response.data;
				lS.error = false;
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};
	
	lS.displayAttachDetails = function(id) {
		lS.resetView();
		lS.showDocAttachments = true;
		lS.showAttachDetails = true;
		lS.resetAttachFormFields();
		lS.resetAttachRelatedLists();
		reqGen.getAttachById(id).then(function(response) {
			lS.attach = response.data;
			lS.getSumsByDocId(lS.doc.id);
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.saveAttach = function() {
		lS.responseMsg = "";
		reqGen.saveAttach(lS.attach, lS.doc.id).then(function(response) {
			lS.getAttachmentsByDocId(lS.doc.id);
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.deleteAttach = function(id) {
		if(confirm("Potwierdz usunięcie załącznika")) {
			lS.responseMsg = "";
			reqGen.deleteAttachById(id).then(function(response) {
				lS.getAttachmentsByDocId(lS.doc.id);
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}
	};

	lS.verifyAttach = function() {
		if(lS.attach.description != undefined && lS.attach.description != "" && lS.attach.file != undefined && lS.attach.file != "" && 
				lS.attach.fileName != undefined && lS.attach.fileName != "" && lS.attach.fileType != undefined & lS.attach.fileType != "") {
			var fType = lS.attach.fileType.toLowerCase(); 
			if(fType.search("image/jpeg") == -1 && fType.search("text/plain") == -1 && fType.search("application/pdf") == -1) {
				lS.error = true;
				lS.responseMsg = "Dozwolony format załączników to jpeg, text, pdf."
			} else {
				lS.error = false;
				lS.responseMsg = "";
			}
		} else {
			lS.error = true;
			lS.responseMsg = "";
		}
	}
	
	lS.getIFrameSrc = function() {
		return lS.attach.fileType + "," + lS.attach.file;
	};
	
	lS.backToAttachments = function() {
		lS.getAttachmentsByDocId(lS.doc.id);
	};
	
	lS.resetAttachFormFields = function() {
		lS.attach.id = undefined;
		lS.attach.description = undefined;
		lS.attach.fileName = undefined;
		lS.attach.fileType = undefined;
		lS.attach.file = undefined;
		file = undefined;
	};
	
	//==================================================================== DOCSTAGE ====================================================================

	lS.getStagesByDocId = function(docId) {
		lS.resetView();
		lS.showStages = true;
		lS.stageList = undefined;
		lS.doc.id = docId;
		lS.doc.status = undefined;
		lS.doc.usern = undefined;
		reqGen.getDocStatusById(lS.doc.id).then(function(response) {
			lS.doc.status = response.data;
//			console.log("getStagesByDocId, doc.status:");
//			console.log(lS.doc.status);
			reqGen.getDocUserById(lS.doc.id).then(function(response) {
				lS.doc.usern = response.data;
//				console.log("getStagesByDocId, doc.usern:");
//				console.log(lS.doc.usern);
				reqGen.getStagesByDocId(docId).then(function(response) {
					lS.stageList = response.data;
				}).
				catch(function(response) {
					lS.responseMsg = reqGen.getResponseMsg(response);
					alert(lS.responseMsg);
				})
			}).
			catch(function(response) {
				lS.responseMsg = reqGen.getResponseMsg(response);
				alert(lS.responseMsg);
			})
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};

	lS.initAction = function(action) {
		lS.responseMsg = "";
		lS.action = action;
		lS.showStageEditForm = true;
		lS.nextStageUser = undefined;
		lS.resetStageFormFields();
		reqGen.getUsersWithPerm("canAccept", lS.authService.session.name).then(function(response) {
			lS.userList = response.data;
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.saveStage = function() {
		lS.responseMsg = "";
		lS.stage.usern = lS.authService.session.name;
		lS.stage.status = lS.doc.status;
		if(lS.action == "send") {
			lS.stage.action = "przekazanie"
		} else if(lS.action == "accept") {
			lS.stage.action = "akceptacja"
		} else if(lS.action == "reject") {
			lS.stage.action = "odrzucenie"
		} else {
			lS.stage.action = "zamknięcie"
		}
		lS.stage.date = new Date();
		reqGen.saveStage(lS.stage, lS.doc.id, lS.nextStageUser).then(function(response) {
			lS.getDocuments();
		}).
		catch(function(response) {
			lS.responseMsg = reqGen.getResponseMsg(response);
			alert(lS.responseMsg);
		})
	};
	
	lS.cancelAction = function() {
		lS.getStagesByDocId(lS.doc.id);
	};
	
	lS.resetStageFormFields = function() {
		lS.stage.usern = undefined,
		lS.stage.status = undefined,
		lS.stage.action = undefined,
		lS.stage.date = undefined,
		lS.stage.comments = undefined
	}
	
	lS.displayWFButtons = function() {
// "true" - it's not boolean, it's json from DWUserSession
		return ((lS.authService.session.canAccept == "true" || lS.authService.session.canEdit == "true") && lS.doc.status != "zaakceptowany" && lS.doc.status != "zamknięty" &&
				lS.showStageEditForm == false);
	}
	
	lS.displayButtonsForUserWithAcceptPerm = function() {
		return ((lS.authService.session.name == "super" || lS.doc.status == "nowy" || lS.authService.session.name == lS.doc.usern) && lS.authService.session.canAccept == "true");
	}
	
	//==================================================================== COMMON ====================================================================

	lS.backToDocuments = function() {
		lS.getDocuments();
	}

	lS.resetView = function() {
		lS.showDocuments = false;
		lS.showDocItems = false;
		lS.showDocAttachments = false;
		lS.showStages = false;
		lS.showDocEditForm = false;
		lS.showDocDetails = false;
		lS.showItemEditForm = false;
		lS.showItemDetails = false;
		lS.showAttachEditForm = false;
		lS.showAttachDetails = false;
		lS.showStageEditForm = false;
		lS.editNew = false;
		lS.responseMsg = "";
		lS.error = false;
	};
	
	lS.resetDocRelatedLists = function() {
		lS.sumList = undefined;
		lS.pmList = undefined;
		lS.contrList = undefined;
	};
	
	lS.resetItemRelatedLists = function() {
		lS.sumList = undefined;
		lS.utList = undefined;
		lS.trList = undefined;
	}

	lS.resetAttachRelatedLists = function() {
		lS.sumList = undefined;
	}
	
	lS.sorting.sortType = "invDt";
	lS.sorting.sortReverse = false;
	lS.getDocuments();
}])
