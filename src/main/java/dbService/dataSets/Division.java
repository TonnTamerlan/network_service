package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * Class contains information about devision of company
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */

@Entity
@Table(name = "divisions")
public class Division extends Model implements Serializable {

	private static final long serialVersionUID = -2263018369978708510L;

	@NotNull
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@NotNull
	@Column(name = "adress", nullable = false)
	private String adress;

	@Column(name = "phone")
	private String phone;

	@ManyToMany(mappedBy = "divisions", cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();

	@OneToMany(mappedBy = "division", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Unit> units = new HashSet<>();

	public Division() {
		super();
	}

	public Division(long id) {
		super(id);
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
		result = prime * result + (int) (this.getId() ^ (this.getId() >>> 32));
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
		if (this.getId() != other.getId())
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
