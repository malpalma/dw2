package mp.dw.db.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DW_USER")
public class UserE {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable=false, length=50, unique=true)
	private String name;
	
//	length must be at least 60 (bcrypt)
	@Column(length=100)
	private String pass;
	
	private Boolean active;
	
	private Boolean canEdit;
	
	private Boolean canAccept;
	
	private Boolean isAdmin;
	
	
	
	
	public UserE() {}
	
	public UserE(String name, String pass) {
		this.name = name;
		this.pass = pass;
		this.active = true;
		this.canEdit = false;
		this.canAccept = false;
		this.isAdmin = false;
	}
	
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
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Boolean getCanAccept() {
		return canAccept;
	}

	public void setCanAccept(Boolean canAccept) {
		this.canAccept = canAccept;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
