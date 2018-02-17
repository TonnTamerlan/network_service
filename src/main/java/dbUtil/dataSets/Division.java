package dbUtil.dataSets;

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
public class Division implements Serializable {

	private static final long serialVersionUID = -2263018369978708510L;

	@NotNull
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@NotNull
	@Column(name = "adress", nullable = false)
	private String adress;

	@Column(name = "phone")
	private String phone;

	@ManyToMany(mappedBy = "divisions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();

	@OneToMany(mappedBy = "division", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Unit> units = new HashSet<>();

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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if (user == null) {
			return;
		}
		if (!this.users.contains(user)) {
			this.users.add(user);
		}
		if (!user.getDivisions().contains(this)) {
			user.addDivision(this);
		}
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
		str.append("Division =\n")
				.append("{\n")
				.append("\t\"id\": ").append(this.getId()).append("\",\n")
				.append("\t\"name\": \"").append(name).append("\",\n")
				.append("\t\"adress\": \"").append(adress).append("\",\n")
				.append("\t\"phone\": \"").append(phone).append("\"\n")
				.append("\t\"users\":\n\t{\n");
		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				str.append("\t\t\"").append(user.getLogin()).append("\"\n");
			}
		}
		str.append("\t}\n").append("}");
		return str.toString();
	}

}
