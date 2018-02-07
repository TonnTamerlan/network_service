package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Class contains information about user
 * 
 * @author Alexey Kopylov
 *
 *@version 1.0
 */

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = -541666259107919334L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(name = "login", unique = true, updatable = true, nullable = false)
	private String login;

	@Column(name = "password", unique = false, updatable = true, nullable = false)
	private String password;

	@Column(name = "first_name", updatable = true, nullable = false)
	private String firstName;

	@Column(name = "last_name", updatable = true, nullable = false)
	private String lastName;

	@Column(name = "middle_name", updatable = true, nullable = true)
	private String middleName;

	@Column(name = "email", updatable = true, nullable = false)
	private String email;

	@Column(name = "phone", updatable = true, nullable = false)
	private String phone;

	@Column(name = "title", updatable = true, nullable = false)
	private String title;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_division", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "division_id") })
	private Set<Division> divisions = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	protected User() {
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(Set<Division> divisions) {
		this.divisions = divisions;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}
