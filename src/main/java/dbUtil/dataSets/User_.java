package dbUtil.dataSets;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for entity {@link User}
 * 
 * @author Alexey Kopylov
 *
 */
@StaticMetamodel(User.class)
public class User_ {

	public static volatile SingularAttribute<User, Long> id;

	public static volatile SingularAttribute<User, String> login;

	public static volatile SingularAttribute<User, String> password;

	public static volatile SingularAttribute<User, String> firstName;

	public static volatile SingularAttribute<User, String> lastName;

	public static volatile SingularAttribute<User, String> middleName;

	public static volatile SingularAttribute<User, String> email;

	public static volatile SingularAttribute<User, String> phone;

	public static volatile SingularAttribute<User, String> title;

	public static volatile SetAttribute<User, Division> divisions;

	public static volatile SingularAttribute<User, Role> role;
	
}
