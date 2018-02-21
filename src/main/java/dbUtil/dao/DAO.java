package dbUtil.dao;

import dbUtil.DBException;

public interface DAO<T> {

	/**
	 * Add an entity to the repository
	 * 
	 * @param div is the division
	 * @throws DBException 
	 */
	boolean add(T div) throws DBException;

	/**
	 * Find and get an entity from the repository by id
	 * 
	 * @param id is the primary key
	 * 
	 * @return division or null
	 */
	T getById(long id) throws DBException;

	/**
	 * Delete an entity from the repository
	 * 
	 * @param div
	 */
	boolean delete(T div) throws DBException;

	/**
	 * Delete an entity by id from the repository
	 * 
	 * @param id is the primary key
	 */
	boolean deleteById(long id) throws DBException;

	/**
	 * Update entity in the repository
	 * 
	 * @param div
	 */
	boolean update(T div) throws DBException;

}