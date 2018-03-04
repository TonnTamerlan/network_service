package dbUtil.dataSets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class contains information about devision of company
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */

@Entity
@Table(name = "divisions")
public class Division implements Serializable {

	private static final long serialVersionUID = -2263018369978708510L;

	@NotNull
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull(message="The field name cannot be null")
	@Size(min=20, max=200, message="The field name must be not less than 10 character and not more than 200 characters")
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@NotNull(message="The field adress cannot be null")
	@Size(min=20, max = 300, message="The field adress must be not less than 20 character and not more than 100 characters")
	@Column(name = "adress", nullable = false)
	private String adress;

	@Column(name = "phone")
	private String phone;
	
	@Column(name = "photos")
	private String photos;
	
	@Column(name = "sources")
	private String sources;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "master_division_id")
	private Division masterDivision;
	
	@OneToMany(mappedBy = "masterDivision", fetch = FetchType.LAZY)
	private Set<Division> slaveDivisions = new HashSet<>();
	
	@ManyToMany(mappedBy = "divisions", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();
	
	@OneToMany(mappedBy = "division", fetch = FetchType.LAZY)
	private List<Equipment> equipment = new ArrayList<>();
	
	public Division() {
	}

	public Division(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public Division getMasterDivision() {
		return masterDivision;
	}

	public void setMasterDivision(Division masterDivision) {
		this.masterDivision = masterDivision;
		if(masterDivision != null) {
			masterDivision.getSlaveDivisions().add(this);
		}
	}

	public Set<Division> getSlaveDivisions() {
		return slaveDivisions;
	}

	public void setSlaveDivisions(Set<Division> slaveDivisions) {
		if(slaveDivisions != null) {
			this.slaveDivisions = slaveDivisions;
		} else {
			this.slaveDivisions = new HashSet<Division>();
		}
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		if (users != null) {
			this.users = users;
		} else {
			this.users = new HashSet<User>();
		}
	}
	

	public void addUser(User user) {
		if (user == null) {
			return;
		}
		this.users.add(user);
		user.getDivisions().add(this);
	}

	public List<Equipment> getEquipment() {
		return equipment;
	}

	public void setEquipment(List<Equipment> equipment) {
		if (equipment != null) {
			this.equipment = equipment;
		} else {
			this.equipment = new ArrayList<Equipment>();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Division other = (Division) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("{")
				.append("id=").append(this.getId()).append(", ")
				.append("name=").append(name).append(", ")
				.append("adress=").append(adress).append(", ")
				.append("phone=").append(phone).append(", ")
				.append("masterDivision=").append(masterDivision == null ? "null" : masterDivision.name).append("}");
		return str.toString();
	}


}
