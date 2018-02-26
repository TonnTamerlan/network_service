package dbUtil.service;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import dbUtil.DBException;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;

/**
 * Implementation interface EquipmentDAO for working with table "equipments" in database
 * 
 * @author Alexey Kopylov
 * 
 * @version 1.0-alfa
 *
 */
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
			String errorMessage = "Cannot add the equipment \"" + equip.getName() + "\" with ip=\"" + equip.getIp() + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public Equipment getById(long id) throws DBException {
		LOGGER.debug("Try to get an equipment with id={}", id);
		Equipment equip = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			equip = session.get(Equipment.class, id);
			if (equip != null) {
				session.getTransaction().commit();
				LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\" has got",
						equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
			} else {
				session.getTransaction().commit();
				LOGGER.debug("The equipment with id={} didn't find", id);				
			}
		} catch (Exception e) {
			String errorMessage = "Cannot read the equipment with id=" + id;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return equip;
	}

	@Override
	public boolean delete(Equipment equip) throws DBException {
		LOGGER.debug("Try to delete the equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\"",
				equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			if(session.get(Equipment.class, equip.getId()) != null) {
				session.delete(equip);
				transaction.commit();
				result = true;
				LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\" has deleted",
						equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
			} else {
				transaction.commit();
				LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\" didn't find",
						equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
			}
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			String errorMessage = "Cannot delete the equipment \"" + equip.getName() + "\" with ip=\"" + equip.getIp()
					+ "\" and id=" + equip.getId() + "\" in the devision \"" + equip.getDivision().getName() + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		LOGGER.debug("Try to delete the equipment by id={}", id);
		boolean result = false;
		Transaction transanction = null;
		try (Session session =  SESSION_FACTORY.openSession()) {
			transanction = session.beginTransaction();
			Equipment equip = session.get(Equipment.class, id);
			if (equip != null) {
				session.remove(equip);
				transanction.commit();
				LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\" has deleted by id",
						equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
				result = true;
			} else {
				transanction.commit();
				LOGGER.debug("The equipment with id={} didn' find", id);
			}
		} catch (Exception e) {
			if (transanction != null && transanction.isActive()) {
				transanction.rollback();
			}
			String errorMessage = "Cannot delete the equipment with id=" + id;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
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
