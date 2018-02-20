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


public interface UserDAO {
	
	/**
	 * add user to repository
	 * 
	 * @param user
	 * @throws DBException 
	 */
	public boolean add(User user) throws DBException;
	
	/**
	 * Find and read user from repository by id
	 * 
	 * @param id is primary key
	 * 
	 * @return user
	 */
	public User getByID(long id) throws DBException;
	
	/**
	 * Find and read user from repository by login
	 * 
	 * @param login - the unique name of the user
	 * @return user
	 */
	public User getByLogin(String login) throws DBException;
	
	/**
	 * Return logins of all users from repository
	 * 
	 * @return List of all of users
	 */
	public Set<String> getAllLogins() throws DBException;
	
	/**
	 * Find and get users by the specific role
	 * 
	 * @param role is the specific role
	 * @return set of users which have the specific role
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
	 * Find and delete user by id
	 * 
	 * @param id is primary key
	 */
	public boolean deleteByID(long id) throws DBException;
	
	/**
	 * Find and delete user by login
	 * 
	 * @param login - the unique name of the user
	 */
	public boolean deleteByLogin(String login) throws DBException;
	
	/**
	 * Find and delete user
	 * 
	 * @param user
	 */
	public boolean delete(User user) throws DBException;
	
	/**
	 * Update user's information
	 * 
	 * @param user
	 */
	public boolean update(User user) throws DBException;
	
	/**
	 * Add division in user's information
	 * 
	 * @param login - the unique name of the user
	 * @param division - the division which needs to add
	 */
	public boolean addDivision(String login, Division division) throws DBException;
	
}
