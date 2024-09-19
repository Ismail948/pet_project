package Application;

import java.util.Objects;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "adoptions")
public class Adoptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_photo", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] petPhoto;

    @Column(name = "lane", nullable = false)
    private String lane;

    @Column(name = "pet_type", nullable = false)
    private String petType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Column(name = "status", nullable = false)
    private Boolean status; // true for available, false for adopted

    @Column(name = "posted_by", nullable = false)
    private String postedBy; // Username or ID of the person who posted the adoption

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // Phone number of the person who posted the adoption

    @Column(name = "post_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate; // Automatically saves the current date and time

    // Default constructor
    public Adoptions() {
        this.postDate = new Date(); // Set current date and time
    }

  
    public Adoptions(Long id, byte[] petPhoto, String lane, String petType, String description, String city,
			String state, String pincode, Boolean status, String postedBy, String phoneNumber, Date postDate) {
		super();
		this.id = id;
		this.petPhoto = petPhoto;
		this.lane = lane;
		this.petType = petType;
		this.description = description;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.status = status;
		this.postedBy = postedBy;
		this.phoneNumber = phoneNumber;
		this.postDate = postDate;
	}


	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(byte[] petPhoto) {
        this.petPhoto = petPhoto;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getPostDate() {
        return postDate;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adoptions)) return false;
        Adoptions adoptions = (Adoptions) o;
        return Objects.equals(id, adoptions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override toString
    @Override
    public String toString() {
        return "Adoptions{" +
                "id=" + id +
                ", petPhoto='" + petPhoto + '\'' +
                ", lane='" + lane + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                ", status=" + status +
                ", postedBy='" + postedBy + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", postDate=" + postDate +
                '}';
    }
}
