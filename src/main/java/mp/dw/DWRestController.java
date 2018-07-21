package mp.dw;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mp.dw.db.contr.Contractor;
import mp.dw.db.doc.Attachment;
import mp.dw.db.doc.DocItem;
import mp.dw.db.doc.DocStage;
import mp.dw.db.doc.DocSum;
import mp.dw.db.doc.Document;
import mp.dw.db.param.Param;
import mp.dw.db.user.UserE;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DWRestController {

	@Autowired
	private DWService dwService;
	
	@Autowired
	private DWUserSession userSession;
	
	protected final Logger logger = LoggerFactory.getLogger(DWRestController.class);
	
	@GetMapping(value = "/authenticate")
	public String authenticate(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			if(!this.userSession.getAuthenticated()) {
				this.userSession.updateOnAuthentication();
			}
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /authenticate; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /authenticate; " + e1.getMessage());
			}
		}
		return this.userSession.toJson();
	}
	
	@GetMapping(value = "/userSession")
	public String getSession(HttpServletRequest req, HttpServletResponse resp) {
		return this.userSession.toJson();
	}
	
//	============================== DOCUMENT ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getDocuments")
	public List<Document> getDocuments(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getAllDocuments();
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getDocuments; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getDocuments; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getDocument/{id}")
	public Document getDocumentById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getDocumentById(id);
		} catch (Exception e) {
			logger.error("exception in: GET Mapping, /getDocument/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());;
				return null;
			} catch (Exception e1) {
				logger.error("exception in: GET Mapping, /getDocument/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getDocStatus/{id}")
	public char[] getDocStatusById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getDocStatusById(id).toCharArray();
		} catch (Exception e) {
			logger.error("exception in: GET Mapping, /getDocument/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());;
				return null;
			} catch (Exception e1) {
				logger.error("exception in: GET Mapping, /getDocument/{id}; " + e1.getMessage());
				return null;
			}
		}
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getDocUser/{id}")
	public char[] getDocUserById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getDocUserById(id).toCharArray();
		} catch (Exception e) {
			logger.error("exception in: GET Mapping, /getDocument/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());;
				return null;
			} catch (Exception e1) {
				logger.error("exception in: GET Mapping, /getDocument/{id}; " + e1.getMessage());
				return null;
			}
		}
	}

	@PreAuthorize("hasAuthority('EDIT')")
	@PostMapping(value = "/saveDocument")
	public void saveDocument(@RequestBody Document doc, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.saveDocument(doc);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveDocument; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveDocument; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@DeleteMapping(value = "/deleteDocument/{id}")
	public void deleteDocumentById(@PathVariable long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteDocById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteDocument/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteDocument/{id}; " + e1.getMessage());
			}
		}
	}

//	============================== DOCSTAGE ==============================
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getStages/{docId}")
	public List<DocStage> getStagesByDocId(@PathVariable Long docId, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getStagesByDocId(docId);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getItems/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getItems/{docId}; " + e1.getMessage());
				return null;
			}
		}
	}

	// to start document workflow user must have EDIT or ACCEPT authority, user with ACCEPT authority can accept in the first stage
	// user only with EDIT authority can send document to another user with ACCEPT auth.
	@PreAuthorize("hasAnyAuthority('EDIT', 'ACCEPT')")
	@PostMapping(value = "/initDocWF/{docId}")
	public void initDocWF(@RequestBody DocStage stage, @PathVariable long docId, @RequestParam(name = "nextStageUser", required = false) String nextStageUser,
			HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.saveStage(stage, docId, nextStageUser);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /sendDoc/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /sendDoc/{docId}; " + e1.getMessage());
			}
		}
	}

	// user with ACCEPT auth. can accept, reject or close documents
	@PreAuthorize("hasAuthority('ACCEPT')")
	@PostMapping(value = "/saveStage/{docId}")
	public void saveStage(@RequestBody DocStage stage, @PathVariable long docId, @RequestParam(name = "nextStageUser", required = false) String nextStageUser,
			HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.saveStage(stage, docId, nextStageUser);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /sendDoc/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /sendDoc/{docId}; " + e1.getMessage());
			}
		}
	}

//	============================== DOCITEM ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getItems/{docId}")
	public List<DocItem> getItemsByDocId(@PathVariable Long docId, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getItemsByDocId(docId);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getItems/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getItems/{docId}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getItem/{id}")
	public DocItem getItemById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getItemById(id);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getItems/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getItems/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@PostMapping(value = "/saveItem/{docId}")
	public void saveItem(@RequestBody DocItem item, @PathVariable Long docId, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.addOrUpdateItem(item, docId);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveItem/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveItem/{docId}; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@DeleteMapping(value = "/deleteItem/{id}")
	public void deleteItemById(@PathVariable long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteItemById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteItem/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteItem/{id}; " + e1.getMessage());
			}
		}
	}
	
//	============================== SUM ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getSums/{docId}")
	public List<DocSum> getSumsByDocId(@PathVariable Long docId, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getSumsByDocId(docId);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getSums/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getSums/{docId}; " + e1.getMessage());
				return null;
			}
		}
	}

//	============================== PARAM ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getParams/{type}")
	public List<Param> getParamsByType(@PathVariable String type, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			return dwService.getParamsByType(type);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getParams/{type}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getParams/{type}; " + e1.getMessage());
				return null;
			}
		}
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getParam/{id}")
	public Param getParamById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getParamById(id);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getParam/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getParam/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@PostMapping(value = "/saveParam")
	public void saveParam(@RequestBody Param param, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.saveParam(param);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveParam; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveParam; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@DeleteMapping(value = "/deleteParam/{id}")
	public void deleteParamById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteParamById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteParam/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteParam/{id}; " + e1.getMessage());
			}
		}
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getParamByDescr/{type}")
	public Param getParamByTypeAndDescr(@PathVariable String type, @RequestParam(name = "descr") String descr, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			return dwService.getParamByTypeAndDescr(type, descr);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getParam/{type}/{descr}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getParam/{type}/{descr}; " + e1.getMessage());
				return null;
			}
		}
	}
	
//	============================== USER ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getUsers")
	public List<UserE> getUsers(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getAllUsers();
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getUsers; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getUsers; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getUsers/{perm}")
	public List<UserE> getUsersWithPerm(@PathVariable String perm, @RequestParam(name = "without") String without, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getUsersWithPerm(perm, without);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getUsers/{perm}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getUsers/{perm}; " + e1.getMessage());
				return null;
			}
		}
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getUser/{id}")
	public UserE getUserById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getUserById(id);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getUser/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getUser/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(value = "/saveUser")
	public void saveUser(@RequestBody UserE user, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.saveUser(user);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveUser; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveUser; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping(value = "/setNewPassword/{id}")
	public void saveUser(@RequestBody String pass, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.setNewPassword(id, pass);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveUser; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveUser; " + e1.getMessage());
			}
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(value = "/deleteUser/{id}")
	public void deleteUserById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteUserById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteUser/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteUser/{id}; " + e1.getMessage());
			}
		}
	}
	
//	============================== CONTRACTOR ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getContractors")
	public List<Contractor> getContractors(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getAllContractors();
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getContractors; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getContractors; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getContractor/{id}")
	public Contractor getContractorById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setCharacterEncoding("UTF-8");
			return dwService.getContractorById(id);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getContractor/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getContractor/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@PostMapping(value = "/saveContractor")
	public void saveContractor(@RequestBody Contractor contr, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.saveContractor(contr);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveContractor; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveContractor; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@DeleteMapping(value = "/deleteContractor/{id}")
	public void deleteContractorById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteContractorById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteContractor/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteContractor/{id}; " + e1.getMessage());
			}
		}
	}

//	============================== ATTACHMENT ==============================

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getAttachments/{docId}")
	public List<Attachment> getAttachmentsByDocId(@PathVariable Long docId, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			return dwService.getAttachmentsByDocId(docId);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getAttachment/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getAttachment/{docId}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/getAttach/{id}")
	public Attachment getAttachmentById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			return dwService.getAttachmentById(id);
		} catch(Exception e) {
			logger.error("exception in: GET Mapping, /getAttach/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
				return null;
			} catch(Exception e1) {
				logger.error("exception in: GET Mapping, /getAttach/{id}; " + e1.getMessage());
				return null;
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@PostMapping(value = "/saveAttach/{docId}")
	public void saveAttachment(@RequestBody String file, @PathVariable Long docId, 
			@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "descr") String descr, @RequestParam(name = "fileName") String fileName, @RequestParam(name = "fileType") String fileType,
			HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			dwService.addOrUpdateAttach(DatatypeConverter.parseBase64Binary(file), docId, id, descr, fileName, fileType);
		} catch(Exception e) {
			logger.error("exception in: POST Mapping, /saveAttach/{docId}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: POST Mapping, /saveAttach/{docId}; " + e1.getMessage());
			}
		}
	}
	
	@PreAuthorize("hasAuthority('EDIT')")
	@DeleteMapping(value = "/deleteAttach/{id}")
	public void deteteAttachmentById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp) {
		try {
			dwService.deleteAttachmentById(id);
		} catch(Exception e) {
			logger.error("exception in: DELETE Mapping, /deleteAttach/{id}; " + e.getMessage());
			try {
				resp.sendError(500, e.getMessage());
			} catch(Exception e1) {
				logger.error("exception in: DELETE Mapping, /deleteAttach/{id}; " + e1.getMessage());
			}
		}
	}
}
