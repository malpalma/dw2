package mp.dw.db.param;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "DW_PARAM", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"type", "description"})})
public class Param {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 2)
	private String type;
	
	@Column(length = 50)
	private String code;
	
	@Column(nullable = false, length = 100)
	private String description;
	
	private Float value;
	
	
	
	public Param() {}
	
	public Param(String type, String description, Float value) {
		this.type = type;
		this.description = description;
		this.value = value;
	}

	public Param(String type, String code, String description) {
		this.type = type;
		this.code = code;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	
}
