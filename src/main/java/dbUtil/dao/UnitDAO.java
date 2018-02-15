package dbUtil.dao;

import java.util.Set;

import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;
import dbUtil.dataSets.Unit;

/**
 * Interface for working with repository using entity {@link Unit}
 * 
 * @author Alexey Kopylov
 *
 */
public interface UnitDAO {
	
	/**
	 * add unit to repository
	 * 
	 * @param user
	 */
	public void add(Unit unit);
	
	
	/**
	 * Find and read unit from repository by id
	 * 
	 * @param id is the primary key
	 * @return unit
	 */
	public Unit getByID(long id);
	
	/**
	 * Find and read unit by unit's name and a division
	 * 
	 * @param nameUnit is unit's name
	 * @param is division which the unit belongs
	 * @return unit
	 */
	public Unit getByNameAndDevision(String nameUnit, Division division);
	
	/**
	 * Find and read unit by unit's name and a division
	 * 
	 * @param nameUnit is unit's name
	 * @param nameDivision is name of division which the unit belongs
	 * @return unit
	 */
	public Unit getByNameAndDevision(String nameUnit, String nameDivision);
	
	/**
	 * Return all of units from repository
	 * 
	 * @return Set of all units
	 */
	public Set<Unit> getAll();
	
	/**
	 * Get all equipments of the Unit which name is nameUnit and belongs division
	 * 
	 * @param nameUnit is unit's name
	 * @param division is division which the unit belongs
	 * @return all the unit's equipments
	 */
	public Set<Equipment> getEquipments(String nameUnit, Division division);
	
	/**
	 * Get all equipments of the Unit which name is nameUnit and belongs division which name is nameDevision
	 * 
	 * @param nameUnit is unit's name
	 * @param nameDivision is name of division which the unit belongs
	 * @return all the unit's equipments
	 */
	public Set<Equipment> getEquipments(String nameUnit, String nameDivision);
	
	/**
	 * Update unit's information
	 * 
	 * @param unit
	 */
	public void update(Unit unit);
	
	/**
	 * Find and delete user
	 * 
	 * @param unit
	 */
	public void delete(Unit unit);
	
	/**
	 * Find and delete user which name is nameUnit and belongs the division
	 * 
	 * @param nameUnit is unit's name
	 * @param division is division which the unit belongs
	 */
	public void delete(String nameUnit, Division division);
	
	/**
	 * Find and delete user which name is nameUnit and belongs division which name is nameDevision
	 * 
	 * @param nameUnit is unit's name
	 * @param nameDivision is name of division which the unit belongs
	 */
	public void delete(String nameUnit, String nameDivision);
	
}
