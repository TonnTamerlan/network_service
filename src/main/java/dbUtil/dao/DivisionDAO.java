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
public interface DivisionDAO {
	
	/**
	 * Add a division to repository
	 * 
	 * @param div is the division
	 * @throws DBException 
	 */
	public void add(Division div) throws DBException;
	
	/**
	 * Find and get a division from repository by id
	 * 
	 * @param id is the primary key
	 * 
	 * @return division
	 */
	public Division getById(long id)  throws DBException;
	
	/**
	 * Find and get a division from repository by name
	 * 
	 * @param name
	 * 
	 * @return division
	 */
	public Division getByName(String name)  throws DBException;
	
	/**
	 * Return all of devisions from repository
	 * 
	 * @return set of all of divisions
	 */
	public Set<Division> getAll()  throws DBException;
	
	/**
	 * Get all of divisions with specific name
	 * 
	 * @param equipName is specific name
	 * 
	 * @return set of division
	 */
	public Set<Division> getByEquipmentName(String equipName)  throws DBException;
	
	/**
	 * Delete division
	 * 
	 * @param div
	 */
	public void delete(Division div)  throws DBException;
	
	/**
	 * Delete a division by id
	 * 
	 * @param id is the primary key
	 */
	public void deleteById(long id)  throws DBException;
	
	/**
	 * Delete a division by name
	 * 
	 * @param name is specific unique name
	 */
	public void deleteByName(String name)  throws DBException;
	
	/**
	 * Update division
	 * 
	 * @param div
	 */
	public void update(Division div)  throws DBException;
	
}
