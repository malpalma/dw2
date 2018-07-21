package mp.dw;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import mp.dw.db.contr.Contractor;
import mp.dw.db.contr.ContractorDAOImpl;
import mp.dw.db.doc.Attachment;
import mp.dw.db.doc.AttachmentDAOImpl;
import mp.dw.db.doc.DocItem;
import mp.dw.db.doc.DocItemDAOImpl;
import mp.dw.db.doc.DocStage;
import mp.dw.db.doc.DocStageDAOImpl;
import mp.dw.db.doc.DocSum;
import mp.dw.db.doc.DocSumDAOImpl;
import mp.dw.db.doc.Document;
import mp.dw.db.doc.DocumentDAOImpl;
import mp.dw.db.param.Param;
import mp.dw.db.param.ParamDAOImpl;
import mp.dw.db.user.UserDAOImpl;
import mp.dw.db.user.UserE;

@Service
@EnableTransactionManagement
public class DWService {

	@PersistenceUnit
	private EntityManagerFactory entManFactory;
	
	@Autowired
	private DocumentDAOImpl docDao;
	
	@Autowired
	private DocItemDAOImpl docItemDao;
	
	@Autowired
	private DocSumDAOImpl docSumDao;
	
	@Autowired
	private ParamDAOImpl paramDao;
	
	@Autowired
	private UserDAOImpl userDao;
	
	@Autowired
	private ContractorDAOImpl contrDao;
	
	@Autowired
	private AttachmentDAOImpl attachDao;
	
	@Autowired
	private DocStageDAOImpl stageDao;
	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
//	============================== DOCUMENT ==============================

	public List<Document> getAllDocuments() {
		return (List<Document>) docDao.getAll();
	}
	
	public Document getDocumentById(Long id) {
		return docDao.getById(id);
	}
	
	public String getDocStatusById(Long id) {
		return docDao.getStatusById(id);
	}

	public String getDocUserById(Long id) {
		return docDao.getUserById(id);
	}
	
	@Transactional
	public void saveDocument(Document d) {
		if(d.getId() == null) {
			docDao.insert(d);
		} else {
			docDao.update(d);
		}
	}
	
	@Transactional
	public void deleteDocById(Long id) {
		docDao.deleteById(id);
	}
	
//	============================== DOCSTAGE ==============================
	
	public List<DocStage> getStagesByDocId(Long docId) {
		return (List<DocStage>) stageDao.getByDocId(docId);
	}

	@Transactional
	public void saveStage(DocStage stage, Long docId, String nextStageUser) throws Exception {
		Document doc = this.getDocumentById(docId);
		if(doc != null) {
			//stage.status must be == doc.status and stage.user must be == doc.user, unless doc.status == "nowy" (starting document workflow)
			if((stage.getStatus().equals(doc.getStatus())) && (doc.getStatus().equals("nowy") || stage.getUser().equals(doc.getUser()))) {
				if(stage.getAction().equals("przekazanie")) {
					doc.setStatus("w akceptacji");
					doc.setUser(nextStageUser);
				} else if(stage.getAction().equals("akceptacja")) {
					doc.setStatus("zaakceptowany");
					doc.setUser("");
				} else if(stage.getAction().equals("odrzucenie")) {
					doc.setStatus("odrzucony");
					doc.setUser(nextStageUser);
				} else {
					doc.setStatus("zamknięty");
					doc.setUser("");
				}
				doc.addStage(stage);
				docDao.update(doc);
			} else {
				throw new Exception("Niezgodny aktualny status dokumentu.");
			}
		} else {
			throw new Exception("Nie znaleziono dokumentu o id = " + docId.toString());
		}
	}
	
//	============================== DOCITEM ==============================

	public List<DocItem> getItemsByDocId(Long docId) {
		return (List<DocItem>) docItemDao.getByDocId(docId);
	}
	
	public DocItem getItemById(Long id) {
		return docItemDao.getById(id);
	}
	
	public List<Object[]> getGroupedItemsByDocId(Long docId) {
		return (List<Object[]>) docItemDao.getGroupedByDocId(docId);
	}
	
	@Transactional
	public void addOrUpdateItem(DocItem item, Long docId) throws Exception {
		Document doc = this.getDocumentById(docId);
		if(doc != null) {
			if(item.getId() == null) {
				doc.addItem(item);
				this.saveDocument(doc);
			} else {
				this.saveItem(item);
			}
			this.updateSumsByDocId(docId, doc);
		} else {
			throw new Exception("Nie znaleziono dokumentu o id = " + docId.toString());
		}
	}
	
	@Transactional
	public void saveItem(DocItem i) {
		if(i.getId() == null) {
			docItemDao.insert(i);
		} else {
			docItemDao.update(i);
		}
	}
	
	@Transactional
	public void deleteItemById(Long id) throws Exception {
		DocItem item = this.getItemById(id);
		if(item != null) {
			Document doc = item.getDocument();
			if(doc != null) {
				doc.removeItem(item);
				docItemDao.deleteById(id);
				this.updateSumsByDocId(doc.getId(), doc);
			} else {
				throw new Exception("Nie znaleziono dokumentu.");
			}
		} else {
			throw new Exception("Nie znaleziono pozycji dokumentu.");
		}
	}

	@Transactional
	private void updateSumsByDocId(Long docId, Document doc) {
		doc.removeAllSums();
		docSumDao.deleteByDocId(docId);
		List<Object[]> groupedItems = docItemDao.getGroupedByDocId(docId);
		Float grossSum = 0f;
		for(Object[] obj: groupedItems) {
			DocSum sum = new DocSum();
			sum.setDocument(doc);
			sum.setTaxDescr(obj[0].toString());
			Float price = ((Number) obj[1]).floatValue();
			price = Precision.round(price, 2);
			sum.setPrice(price);
			Float tr = ((Number) obj[2]).floatValue();
			Float taxVal = sum.getPrice() * tr;
			taxVal = Precision.round(taxVal, 2);
			sum.setTaxValue(taxVal);
			sum.setGross(sum.getPrice() + sum.getTaxValue());
			doc.addSum(sum);
			grossSum += sum.getGross();
		};
		doc.setGross(grossSum);
		docDao.update(doc);
	}
	
//	============================== SUM ==============================
	
	public List<DocSum> getSumsByDocId(Long docId) {
		return (List<DocSum>) docSumDao.getByDocId(docId);
	}
	
	@Transactional
	public void saveSum(DocSum s) {
		if(s.getId() == null) {
			docSumDao.insert(s);
		} else {
			docSumDao.update(s);
		}
	}
	
	@Transactional
	public void deleteSumsByDocId(Long docId) {
		docSumDao.deleteByDocId(docId);
	}
	
//	============================== PARAM ==============================
	
	public List<Param> getParamsByType(String type) {
		return (List<Param>) paramDao.getByType(type);
	}

	public Param getParamById(Long id) {
		return paramDao.getById(id);
	}
	
	public Param getParamByTypeAndDescr(String type, String descr) {
		return paramDao.getByTypeAndDescr(type, descr);
	}
	
	@Transactional
	public void saveParam(Param p) {
		if(p.getId() == null) {
			paramDao.insert(p);
		} else {
			paramDao.update(p);
		}
	}

	@Transactional
	public void deleteParamById(Long id) {
		paramDao.deleteById(id);
	}
	
//	============================== USER ==============================
	
	public List<UserE> getAllUsers() {
		return (List<UserE>) userDao.getAll();
	}
	
	public List<UserE> getUsersWithPerm(String perm, String without) {
		return (List<UserE>) userDao.getUsersWithPerm(perm, without);
	}

	public UserE getUserById(Long id) {
		return userDao.getById(id);
	}
	
	@Transactional
	public void saveUser(UserE u) {
		u.setPass(bCryptPasswordEncoder.encode(u.getPass()));
		if(u.getId() == null) {
			userDao.insert(u);
		} else {
			userDao.update(u);
		}
	}
	
	@Transactional
	public void setNewPassword(Long id, String pass) throws Exception {
		UserE user = this.getUserById(id);
		if(user != null) {
			user.setPass(bCryptPasswordEncoder.encode(pass));
			userDao.update(user);
		} else {
			throw new Exception("Nie znaleziono użytkownika o id = " + id.toString());
		}
	}
	
	@Transactional
	public void deleteUserById(Long id) {
		userDao.deleteById(id);
	}
	
	public UserE getUserByName(String name) {
		System.out.println("getUserByName");
		return userDao.getByName(name);
	}
	
//	============================== CONTRACTOR ==============================
	
	public List<Contractor> getAllContractors() {
		return (List<Contractor>) contrDao.getAll();
	}
	
	public Contractor getContractorById(Long id) {
		return contrDao.getById(id);
	}
	
	@Transactional
	public void saveContractor(Contractor c) {
		if(c.getId() == null) {
			contrDao.insert(c);
		} else {
			contrDao.update(c);
		}
	}
	
	@Transactional
	public void deleteContractorById(Long id) {
		contrDao.deleteById(id);
	}

//	============================== ATTACHMENT ==============================

	public List<Attachment> getAttachmentsByDocId(Long docId) {
		Document doc = docDao.getById(docId);
		return (List<Attachment>) attachDao.getByDoc(doc);
	}

	public Attachment getAttachmentById(Long id) {
		return attachDao.getById(id);
	}

	@Transactional
	public void addOrUpdateAttach(byte[] f, Long docId, Long id, String descr, String fileName, String fileType) throws Exception {
		Attachment attach;
		if(id == null) {
			attach = new Attachment();
		} else {
			attach = this.getAttachmentById(id);
		}
		attach.setDescription(descr);
		attach.setFileName(fileName);
		attach.setFileType(fileType);
		attach.setFile(f);
		if(attach.getId() == null) {
			Document doc = this.getDocumentById(docId);
			if(doc != null) {
				doc.addAttachment(attach);
				this.saveDocument(doc);
			} else {
				throw new Exception("Nie znaleziono dokumentu o id = " + docId.toString());
			}
		} else {
			this.saveAttachment(attach);
		}
	}
	
	@Transactional
	public void saveAttachment(Attachment a) {
		if(a.getId() == null) {
			attachDao.insert(a);
		} else {
			attachDao.update(a);
		}
	}
	
	@Transactional
	public void deleteAttachmentById(Long id) throws Exception {
		Attachment attach = attachDao.getById(id);
		if(attach != null) {
			Document doc = attach.getDocument();
			if(doc != null) {
				doc.removeAttachment(attach);
				attachDao.deleteById(id);
			} else {
				throw new Exception("Nie znaleziono dokumentu.");
			}
		} else {
			throw new Exception("Nie znaleziono załącznika o id = " + id.toString());
		}
	}
}
