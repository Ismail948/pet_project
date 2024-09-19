package Application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet_type")
public class pettype {
	
	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	
	 public pettype() {
					}
	 
	public pettype(Long id, String type) {
		super();
		this.id = id;
		this.name = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return name;
	}

	public void setType(String type) {
		this.name = type;
	}

	@Column(unique = true)
    private String name;
}
