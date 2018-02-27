package dbUtil.dataSets;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for entity {@link Division}
 * 
 * @author Alexey Kopylov
 *
 */
@StaticMetamodel(Division.class)
public class Division_ {

	public static volatile SingularAttribute<Division, Long> id;
	
	public static volatile SingularAttribute<Division, String> name;

	public static volatile SingularAttribute<Division, String> adress;

	public static volatile SingularAttribute<Division, String> phone;
	
	public static volatile SingularAttribute<Division, String> photos;
	
	public static volatile SingularAttribute<Division, String> sources;
	
	public static volatile SingularAttribute<Division, Division> masterDivision;
	
	public static volatile SetAttribute<Division, Division> slaveDivisions;

	public static volatile SetAttribute<Division, User> users;

	public static volatile ListAttribute<Division, Equipment> equipment;
	
}
