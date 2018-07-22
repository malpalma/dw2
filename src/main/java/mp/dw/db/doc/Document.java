package mp.dw.db.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "DW_DOC")
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private Date invDt;

	@Column(nullable = false, length = 50)
	private String invNo;
	
	private Long sellersId;
	
	@Column(nullable = false)
	private String sellersName;
	
	@Column(nullable = false)
	private String sellersAddress;
	
	@ColumnDefault("''")
	@Column(length = 30)
	private String sellersRegNumber;
	
	@Column(nullable = false)
	private Date saleDt;

	private Date dueDt;
	
	@Column(length = 100)
	private String paymentMethod;
	
	private String bankAccNo;

	private String sellersContactDetails;

	private Float gross;
	
	//NO SETTER FOR @Version FIELD!
	@Version
	@ColumnDefault("0")
	private int version;
	
//	BUG! https://hibernate.atlassian.net/browse/HHH-7668
//	@ColumnDefault("'nowy'")
//	@ManyToOne
//	@Fetch(FetchMode.JOIN)
//	@JoinColumn(referencedColumnName = "status", nullable = false)
//	private Status status;
	
	@ColumnDefault("'nowy'")
	@Column(nullable = false, length = 50)
	private String status;
	
	// "user" is restricted in postgresql
	@Column(length = 50)
	private String usern;
	
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Attachment> attachments = new ArrayList<Attachment>();
	
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<DocItem> items = new ArrayList<DocItem>();

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<DocSum> sums = new ArrayList<DocSum>();
	
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<DocStage> stages = new ArrayList<DocStage>();

	
	
	public Document() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public Long getSellersId() {
		return sellersId;
	}

	public void setSellersId(Long sellersId) {
		this.sellersId = sellersId;
	}

	public Date getInvDt() {
		return invDt;
	}

	public void setInvDt(Date invDt) {
		this.invDt = invDt;
	}

	public Date getDueDt() {
		return dueDt;
	}

	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
	}

	public Date getSaleDt() {
		return saleDt;
	}

	public void setSaleDt(Date saleDt) {
		this.saleDt = saleDt;
	}

	public String getSellersName() {
		return sellersName;
	}

	public void setSellersName(String sellersName) {
		this.sellersName = sellersName;
	}

	public String getSellersAddress() {
		return sellersAddress;
	}

	public void setSellersAddress(String sellersAddress) {
		this.sellersAddress = sellersAddress;
	}

	public String getSellersContactDetails() {
		return sellersContactDetails;
	}

	public void setSellersContactDetails(String sellersContactDetails) {
		this.sellersContactDetails = sellersContactDetails;
	}

	public String getSellersRegNumber() {
		return sellersRegNumber;
	}

	public void setSellersRegNumber(String sellersRegNumber) {
		this.sellersRegNumber = sellersRegNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public Float getGross() {
		return gross;
	}

	public void setGross(Float gross) {
		this.gross = gross;
	}

	public int getVersion() {
		return version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsern() {
		return usern;
	}

	public void setUsern(String usern) {
		this.usern = usern;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void addAttachment(Attachment attach) {
		attachments.add(attach);
		attach.setDocument(this);
	}
	
	public void removeAttachment(Attachment attach) {
		attach.setDocument(null);
		attachments.remove(attach);
	}
	
	public void addItem(DocItem item) {
		items.add(item);
		item.setDocument(this);
	}
	
	public void removeItem(DocItem item) {
		item.setDocument(null);
		items.remove(item);
	}
	
	public void addSum(DocSum sum) {
		sums.add(sum);
		sum.setDocument(this);
	}
	
	public void removeSum(DocSum sum) {
		sum.setDocument(null);
		sums.remove(sum);
	}
	
	public void removeAllSums() {
		sums.clear();
	}
	
	public void addStage(DocStage stage) {
		stages.add(stage);
		stage.setDocument(this);
	}
	
	public void removeStage(DocStage stage) {
		stage.setDocument(null);
		stages.remove(stage);
	}
}
