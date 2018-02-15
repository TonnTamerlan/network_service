package dbUtil.dao;

import java.util.Set;

import dbUtil.dataSets.Equipment;
import dbUtil.dataSets.Unit;

/**
 * Interface for working with repository using entity {@link Equipment}
 * 
 * @author Alexy kopylov
 *
 */
public interface EquipmentDAO {

	/**
	 * Add equipment to repository
	 * 
	 * @param equip
	 */
	public void add(Equipment equip);
	
	/**
	 * Find and read equipment by id
	 * 
	 * @param id is the primary key
	 * @return equipment
	 */
	public Equipment getById(long id);
	
	/**
	 * Find and get all equipments which belong the unit and which name is equipName
	 * 
	 * @param equipName is equipment's name
	 * @param unit is the unit which equipments belong
	 * @return set of equipments
	 */
	public Set<Equipment> getByNameAndUnit(String equipName, Unit unit);
	
	/**
	 * Find and get all equipments which belong the unit and which name is equipName
	 * 
	 * @param equipName is equipment's name
	 * @param unitName is quipmnt's name which equipments belong
	 * @return set of equipments
	 */
	public Set<Equipment> getByNameAndUnit(String equipName, String unitName);
	
	
	/**
	 * Find and read all of the equipment which name is equipName
	 * 
	 * @param equipName is name of equipments
	 * @return set of equipments 
	 */
	public Set<Equipment> getAllByName(String equipName);
	
	/**
	 * Get all of equipments from repository
	 * 
	 * @return set of all equipments
	 */
	public Set<Equipment> getAll();
	
	/**
	 * Update equipments information
	 * 
	 * @param equip
	 */
	public void update(Equipment equip);
	
	/**
	 * Delete the equipment
	 * 
	 * @param equip
	 */
	public void delete(Equipment equip);
	
}
