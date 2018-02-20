package dbUtil.dao;

import java.util.Set;

import dbUtil.DBException;
import dbUtil.dataSets.Division;
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
	public boolean add(Unit unit)  throws DBException;
	
	
	/**
	 * Find and read unit from repository by id
	 * 
	 * @param id is the primary key
	 * @return unit
	 */
	public Unit getByID(long id)  throws DBException;
	
	/**
	 * Find and read unit by unit's name and a division
	 * 
	 * @param nameUnit is unit's name
	 * @param is division which the unit belongs
	 * @return unit
	 */
	public Unit getByNameAndDevision(String nameUnit, Division division)  throws DBException;
	
	/**
	 * Find and read unit by unit's name and a division
	 * 
	 * @param nameUnit is unit's name
	 * @param nameDivision is name of division which the unit belongs
	 * @return unit
	 */
	public Unit getByNameAndDevision(String nameUnit, String nameDivision)  throws DBException;
	
	/**
	 * Return all of units from repository
	 * 
	 * @return Set of all units
	 */
	public Set<Unit> getAll()  throws DBException;
	
	/**
	 * Update unit's information
	 * 
	 * @param unit
	 */
	public boolean update(Unit unit)  throws DBException;
	
	/**
	 * Find and delete user
	 * 
	 * @param unit
	 */
	public boolean delete(Unit unit)  throws DBException;
	 
	/**
	 * Find and delete user which name is nameUnit and belongs the division
	 * 
	 * @param nameUnit is unit's name
	 * @param division is division which the unit belongs
	 */
	public boolean delete(String nameUnit, Division division)  throws DBException;
	
	/**
	 * Find and delete user which name is nameUnit and belongs division which name is nameDevision
	 * 
	 * @param nameUnit is unit's name
	 * @param nameDivision is name of division which the unit belongs
	 */
	public boolean delete(String nameUnit, String nameDivision)  throws DBException;
	
}
