package dbUtil.dataSets;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for entity {@link Equipment}
 * 
 * @author Alexey Kopylov
 *
 */
@StaticMetamodel(Equipment.class)
public class Equipment_ {

	public static volatile SingularAttribute<Equipment, Long> id;
	
	public static volatile SingularAttribute<Equipment, String> name;

	public static volatile SingularAttribute<Equipment, String> ip;

	public static volatile SingularAttribute<Equipment, String> discription;

	public static volatile SingularAttribute<Equipment, Unit> unit;
	
}
