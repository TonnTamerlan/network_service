package dbUtil.dao;

import java.util.List;

import dbUtil.DBException;
import dbUtil.dataSets.Equipment;

/**
 * Interface for working with repository using entity {@link Equipment}
 * 
 * @author Alexy kopylov
 *
 */
public interface EquipmentDAO extends DAO<Equipment> {

	/**
	 * Find and get all equipments which belong the division and which name is equipName
	 * 
	 * @param equipName is equipment's name
	 * @param divName is division's name which equipments belong
	 * @return list of equipments or null if the division with name "divName" doesn't exist
	 */
	public List<Equipment> getByNameAndDivision(String equipName, String divName)  throws DBException;
	
	/**
	 * Get all equipments of the division which name is nameDivision and belongs division which name is nameMasterDevision
	 * 
	 * @param nameDivision is division's name
	 * @param nameMasterDivision is name of division which the division belongs
	 * @return list of all the division's equipments or null if the division with name "nameDivision" doesn't exist
	 */
	public List<Equipment> getByDivision(String nameDivision)  throws DBException;
	
	
	/**
	 * Find and read all of the equipment which name is equipName
	 * 
	 * @param name is name of equipments
	 * @return list of equipments or null if the equipment does not exist
	 */
	public List<Equipment> getAllByName(String name)  throws DBException;
	
}
