package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Class contains information about devision of company
 * 
 * @author Alexey Kopylov
 *
 *@version 1.0
 */

@Entity
@Table(name = "divisions")
public class Division implements Serializable {

	private static final long serialVersionUID = -2263018369978708510L;
	
	@Id
	@Column(name = "division_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long division_id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "adress", nullable = false)
	private String adress;
	
	@Column(name = "phone")
	private String phone;
	
	@ManyToMany(mappedBy = "divisions")
	private Set<User> users = new HashSet<>();
	
	@OneToMany(mappedBy = "division", fetch = FetchType.LAZY)
	private Set<Unit> units = new HashSet<>();
	
	public Division() {
	}

	public long getDivision_id() {
		return division_id;
	}

	public void setDivision_id(long division_id) {
		this.division_id = division_id;
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
		result = prime * result + (int) (division_id ^ (division_id >>> 32));
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
		if (adress == null) {
			if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (division_id != other.division_id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
