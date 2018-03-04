package dbUtil.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Class contains information about user
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = -541666259107919334L;
	
	@NotNull
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Column(name = "login", unique = true, updatable = true, nullable = false)
	private String login;

	@NotNull
	@Column(name = "password", unique = false, updatable = true, nullable = false)
	private String password;

	@NotNull
	@Column(name = "first_name", updatable = true, nullable = false)
	private String firstName;

	@NotNull
	@Column(name = "last_name", updatable = true, nullable = false)
	private String lastName;

	@Column(name = "middle_name", updatable = true, nullable = true)
	private String middleName;

	@Email
	@NotNull
	@Column(name = "email", updatable = true, nullable = false)
	private String email;

	@NotNull
	@Column(name = "phone", updatable = true, nullable = false)
	private String phone;

	@NotNull
	@Column(name = "title", updatable = true, nullable = false)
	private String title;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_division", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "division_id") })
	private Set<Division> divisions = new HashSet<>();

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void addDivision(Division div) {
		if (div == null) {
			return;
		} 
		this.divisions.add(div);
		div.getUsers().add(this);
	}

	@Enumerated(EnumType.STRING)
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
		return true;
	}

	@Override
	public String toString() {

		StringBuilder str = new StringBuilder();
		str.append("{")
				.append("id=").append(id).append(", ")
				.append("login=").append(login).append(", ")
				.append("password=").append(password).append(", ")
				.append("firstName=").append(firstName).append(", ")
				.append("lastName=").append(lastName).append(", ")
				.append("title=").append(title).append(", ")
				.append("phone=").append(phone).append(", ")
				.append("role=").append(role)
				.append("}");

		return str.toString();
	}

}
