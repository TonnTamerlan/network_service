package dbUtil.dao;

import java.util.Set;

import dbUtil.DBException;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;

/**
 * Interface for working with repository using entity {@link User}
 * 
 * @author Alexey Kopylov
 * 
 * @version 1.0
 * 
 */


public interface UserDAO extends DAO<User>{
	
	/**
	 * Find and read user from repository by login
	 * 
	 * @param login - the unique name of the user
	 * @return user
	 */
	public User getByLogin(String login) throws DBException;
	
	/**
	 * Return all user logins from the repository
	 * 
	 * @return Set of all user logins or empty Set if the repository
	 * don't contain any users
	 * @throws DBException
	 * 
	 */
	public Set<String> getAllLogins() throws DBException;
	
	/**
	 * Find and get the set of user logins by the specific role
	 * 
	 * @param role is the specific role
	 * @return the set of user logins which have the specific role
	 * or empty set if that users don't exist
	 * @throws DBException 
	 */
	public Set<String> getByRole(Role role) throws DBException;
	
	/**
	 * Get list of users which belong devision
	 * 
	 * @param devisionName - the unique name of the devision
	 * @return list of users
	 */
	public Set<String> getByDivision(String divisionName) throws DBException;
	
	/**
	 * Find and delete user by login
	 * 
	 * @param login - the unique name of the user
	 */
	public boolean deleteByLogin(String login) throws DBException;
	
	/**
	 * Add division in user's information
	 * 
	 * @param login - the unique name of the user
	 * @param division - the division which needs to add
	 */
	public boolean addDivision(String login, Division division) throws DBException;
	
	/**
	 * Check if user with specific login exists
	 * 
	 * @param login
	 * @return true if user with specific login exists otherwise return false
	 * @throws DBException
	 */
	public boolean isLoginExist(String login) throws DBException;
	
}
