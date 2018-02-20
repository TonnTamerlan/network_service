package dbUtil.dao;

import java.util.Set;

import dbUtil.DBException;
import dbUtil.dataSets.Division;

/**
 * Interface for working with entity {@link Division}
 * 
 * @author Alexey Kopylov
 *
 */
public interface DivisionDAO extends DAO<Division> {
	
	/**
	 * Return all of entities from the repository
	 * 
	 * @return set of all of divisions
	 */
	Set<Division> getAll() throws DBException;
	
	/**
	 * Find and get a division from repository by name
	 * 
	 * @param name
	 * 
	 * @return division
	 */
	public Division getByName(String name)  throws DBException;
	
	/**
	 * Get all of divisions with specific name
	 * 
	 * @param equipName is specific name
	 * 
	 * @return set of division
	 */
	public Set<Division> getByEquipmentName(String equipName)  throws DBException;
	
	/**
	 * Delete a division by name
	 * 
	 * @param name is specific unique name
	 */
	public boolean deleteByName(String name)  throws DBException;
	
}
