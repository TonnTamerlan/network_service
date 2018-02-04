package dbService.dataSets;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class contains information about user
 * 
 * @author Alexey Kopylov
 *
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
	
	@Column(name = "division", updatable = true, nullable = false)
	private String division;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@Column(name = "email", updatable = true, nullable = false)
	private String email;
	
	@Column(name = "phone", updatable = true, nullable = false)
	private String phone;
	
	@Column(name = "title", updatable = true, nullable = false)
	private String title;

	
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


	public String getDivision() {
		return division;
	}


	public void setDivision(String division) {
		this.division = division;
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


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
	
	
}
