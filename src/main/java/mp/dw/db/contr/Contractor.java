package mp.dw.db.contr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "DW_CONTRACTOR")
public class Contractor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;
	
	@Column(length = 30)
	private String regNumber;

	private String contactDetails;
	
	//NO SETTER FOR @Version FIELD!
	@Version
	@ColumnDefault("0")
	private int version;

	
	public Contractor() {}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getRegNumber() {
		return regNumber;
	}


	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}


	public String getContactDetails() {
		return contactDetails;
	}


	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public int getVersion() {
		return version;
	}
}
