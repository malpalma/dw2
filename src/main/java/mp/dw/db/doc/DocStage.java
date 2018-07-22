package mp.dw.db.doc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "DW_DOCSTAGE")
public class DocStage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private Document document;
	
	// "user" is restricted in postgresql
	@Column(nullable = false, length = 50)
	private String usern;
	
	@Column(nullable = false, length = 50)
	private String status;
	
	@Column(length = 50)
	private String action;
	
	@Column(nullable = false)
	private Date date;
	
	private String comments;
	
	
	
	public DocStage() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getUsern() {
		return usern;
	}

	public void setUsern(String usern) {
		this.usern = usern;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
