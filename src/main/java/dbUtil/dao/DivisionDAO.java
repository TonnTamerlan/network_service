package dbUtil.dao;

import java.util.Set;

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
	 */
	public void add(Division div);
	
	/**
	 * Find and get a division from repository by id
	 * 
	 * @param id is the primary key
	 * 
	 * @return division
	 */
	public Division getById(long id);
	
	/**
	 * Find and get a division from repository by name
	 * 
	 * @param name
	 * 
	 * @return division
	 */
	public Division getByName(String name);
	
	/**
	 * Return all of devisions from repository
	 * 
	 * @return set of all of divisions
	 */
	public Set<Division> getAll();
	
	/**
	 * Get all of divisions with specific name
	 * 
	 * @param equipName is specific name
	 * 
	 * @return set of division
	 */
	public Set<Division> getByEquipmentName(String equipName);
	
	/**
	 * Delete division
	 * 
	 * @param div
	 */
	public void delete(Division div);
	
	/**
	 * Delete a division by id
	 * 
	 * @param id is the primary key
	 */
	public void deleteById(long id);
	
	/**
	 * Delete a division by name
	 * 
	 * @param name is specific unique name
	 */
	public void deleteByName(String name);
	
	/**
	 * Update division
	 * 
	 * @param div
	 */
	public void update(Division div);
	
}
