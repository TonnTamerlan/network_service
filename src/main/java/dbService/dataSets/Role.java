package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Class contains information about role of user
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */

@Entity
@Table(name = "roles")
public class Role extends Model implements Serializable {

	private static final long serialVersionUID = 1531665300196850154L;

	@NotNull
	@Column(name = "name", nullable = false, unique = true, updatable = false)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();

	public Role() {
	}

	public Role(long id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (this.getId() ^ (this.getId() >>> 32));
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
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (this.getId() != other.getId())
			return false;
		return true;
	}

}
