package dbUtil.service;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import dbUtil.DBException;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;

public class EquipmentService implements EquipmentDAO {
	
	private static final Logger LOGGER = LogManager.getLogger(EquipmentService.class);
	
	private final SessionFactory SESSION_FACTORY;
	
	public EquipmentService(final SessionFactory sesionFactory) {
		SESSION_FACTORY = sesionFactory;
	}
	
	@Override
	public boolean add(Equipment equip) throws DBException {
		LOGGER.debug("Try to add the equipment \"{}\" in repository", equip.getName());
		boolean result = false;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			session.persist(equip);
			session.getTransaction().commit();
			LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} has added",
					equip.getName(), equip.getIp(), equip.getId());
			result = true;
		} catch (PersistenceException e) {
			LOGGER.debug("The equipment \"{}\" with ip=\"{}\" already exists!", equip.getName(), equip.getIp());
			LOGGER.catching(Level.DEBUG, e);
		}
		catch (Exception e) {
			LOGGER.error("Cannot add the equipment \"" + equip.getName() + "\" with ip=\"" + equip.getIp() + "\"", e);
			throw new DBException("Cannot add the equipment \"" + equip.getName() + "\" with ip=\"" + equip.getIp() + "\"", e);
		}
		return result;
	}

	@Override
	public Equipment getById(long id) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Equipment entity) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Equipment entity) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Equipment> getAll() throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Equipment> getByNameAndDivision(String equipName, Division div) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Equipment> getByNameAndDivision(String equipName, String divName) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Equipment> getByDivision(String nameDivision, String nameMasterDivision) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Equipment> getAllByName(String equipName) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}


}
