package dbUtil.dao;

import java.util.Set;

import dbUtil.DBException;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;

/**
 * Interface for working with repository using entity {@link Equipment}
 * 
 * @author Alexy kopylov
 *
 */
public interface EquipmentDAO extends DAO<Equipment> {

	/**
	 * Return all of entities from the repository
	 * 
	 * @return set of all of divisions
	 */
	Set<Equipment> getAll() throws DBException;
	
	/**
	 * Find and get all equipments which belong the division and which name is equipName
	 * 
	 * @param equipName is equipment's name
	 * @param div is the division which equipments belong
	 * @return set of equipments
	 */
	public Set<Equipment> getByNameAndDivision(String equipName, Division div)  throws DBException;
	
	/**
	 * Find and get all equipments which belong the division and which name is equipName
	 * 
	 * @param equipName is equipment's name
	 * @param divName is division's name which equipments belong
	 * @return set of equipments
	 */
	public Set<Equipment> getByNameAndDivision(String equipName, String divName)  throws DBException;
	
	/**
	 * Get all equipments of the division which name is nameDivision and belongs division which name is nameMasterDevision
	 * 
	 * @param nameDivision is division's name
	 * @param nameMasterDivision is name of division which the division belongs
	 * @return all the division's equipments
	 */
	public Set<Equipment> getByDivision(String nameDivision, String nameMasterDivision)  throws DBException;
	
	
	/**
	 * Find and read all of the equipment which name is equipName
	 * 
	 * @param equipName is name of equipments
	 * @return set of equipments 
	 */
	public Set<Equipment> getAllByName(String equipName)  throws DBException;
	
}
