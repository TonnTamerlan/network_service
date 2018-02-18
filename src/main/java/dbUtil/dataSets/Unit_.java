package dbUtil.dataSets;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel for entity {@link Unit}
 * 
 * @author Alexey Kopylov
 *
 */
@StaticMetamodel(Unit.class)
public class Unit_ {

	public static volatile SingularAttribute<Unit, Long> id; 
	
	public static volatile SingularAttribute<Unit, String> name;
	
	public static volatile SingularAttribute<Unit, String> adress;

	public static volatile SingularAttribute<Unit, String> photos;

	public static volatile SingularAttribute<Unit, String> sources;

	public static volatile SingularAttribute<Unit, Division> division;

	public static volatile SetAttribute<Unit, Equipment> equipments;
	
}
