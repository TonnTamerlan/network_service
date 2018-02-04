package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class contains information about role of user
 * 
 * @author Alexey Kopylov
 *
 */

@Entity
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1531665300196850154L;

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roleId;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy = "role")
	private Set<User> users = new HashSet<>();

	protected Role() {
	}

	
}
