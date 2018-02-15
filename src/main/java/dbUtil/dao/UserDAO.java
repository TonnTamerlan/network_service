package dbUtil.dao;

import java.util.Set;

import dbUtil.dataSets.Division;
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
	 */
	public void addUser(User user);
	
	/**
	 * Find and read user from repository by id
	 * 
	 * @param id is primary key
	 * 
	 * @return user
	 */
	public User getUserByID(long id);
	
	/**
	 * Find and read user from repository by login
	 * 
	 * @param login - the unique name of the user
	 * @return user
	 */
	public User getUserByLogin(String login);
	
	/**
	 * Return all of users from repository
	 * 
	 * @return Set of all of users
	 */
	public Set<User> getAllUsers();
	
	/**
	 * Find and delete user by id
	 * 
	 * @param id is primary key
	 */
	public void deleteUserByID(long id);
	
	/**
	 * Find and delete user by login
	 * 
	 * @param login - the unique name of the user
	 */
	public void deleteUserByLogin(String login);
	
	/**
	 * Find and delete user
	 * 
	 * @param user
	 */
	public void deleteUser(User user);
	
	/**
	 * Update user's information
	 * 
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * Get list of divisions which belong user
	 * 
	 * @param login - the unique name of the user
	 * @return list of divisions
	 */
	public Set<Division> getDivisions(String login);
	
	/**
	 * Add division in user's information
	 * 
	 * @param login - the unique name of the user
	 * @param division - the division which needs to add
	 */
	public void addDivision(String login, Division division);
	
}
