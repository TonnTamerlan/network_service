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
	 * Return names of all divisions from the repository
	 * 
	 * @return set of names of all divisions
	 */
	Set<String> getAllNames() throws DBException;
	
	/**
	 * Find and get a division from repository by name
	 * 
	 * @param name
	 * 
	 * @return division
	 */
	public Division getByName(String name)  throws DBException;
	
	/**
	 * Delete a division by name
	 * 
	 * @param name is specific unique name
	 */
	public boolean deleteByName(String name)  throws DBException;
	
}
