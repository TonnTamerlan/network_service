package dbUtil.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import dbUtil.DBException;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Division_;
import dbUtil.dataSets.Equipment;
import dbUtil.dataSets.Equipment_;

/**
 * Implementation interface EquipmentDAO for working with table "equipments" in database
 * 
 * @author Alexey Kopylov
 * 
 * @version 1.0-alfa
 *
 */
/**
 * @author Ellena
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
		LOGGER.debug("Try to add the equipment {} in the repository", equip);
		if (equip == null) {
			String errorMesage = "The equipment is null";
			LOGGER.debug(errorMesage);
			throw new IllegalArgumentException(errorMesage);
		}
		boolean result = false;
		try (Session session = SESSION_FACTORY.openSession()) {
			if (equip.getDivision() == null) {
				throw new IllegalArgumentException("The division in the equipment " + equip + " is null");
			}
			session.beginTransaction();
			session.update(equip.getDivision());
			session.persist(equip);
			session.getTransaction().commit();
			LOGGER.debug("The equipment {} has added", equip);
			result = true;
		} catch (IllegalArgumentException e) {
			String errorMesage = "The equipment " + equip + " has wrong field division";
			LOGGER.debug(errorMesage, e);
			throw e;
		} catch (PersistenceException e) {
			String errorMesage = "The equipment " + equip + " already exists or some its fields are wrong";
			LOGGER.debug(errorMesage, e);
			throw new IllegalArgumentException(errorMesage, e);
		}
		catch (Exception e) {
			String errorMessage = "Cannot add the equipment " + equip;
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
				session.clear();
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
	public boolean update(Equipment equip) throws DBException {
		LOGGER.debug("Try to update the equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\"",
				equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			session.update(equip);
			transaction.commit();
			LOGGER.debug("The equipment \"{}\" with ip=\"{}\" and id={} in the division \"{}\" has updated",
					equip.getName(), equip.getIp(), equip.getId(), equip.getDivision().getName());
			result = true;
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			String errorMessage = "Cannot update the equipment \"" + equip.getName() + "\" with ip=\"" + equip.getIp()
					+ "\" and id=" + equip.getId() + " in the division \"" + equip.getDivision().getName() + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public List<Equipment> getByNameAndDivision(String equipName, String divName) throws DBException {
		LOGGER.debug("Try to get the list of equipment by the equipment name \"{}\" and the division name \"{}\"", 
				equipName, divName);
		List<Equipment> listEquip = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), divName));
			Division division = session.createQuery(criteriaQuery).getSingleResult();
			listEquip = division.getEquipment().stream().filter(equip->equip.getName().equals(equipName)).collect(Collectors.toList());
			session.getTransaction().commit();
			LOGGER.debug("Was got the next list of equipments \"{}\" by division \"{}\": {}",
					equipName, divName, listEquip.stream().map(equip->equip.getName()).collect(Collectors.toList()));
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the list of equipments which have name \"{}\" and belong division \"{}\", because the division doesn't exist", equipName, divName);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the list of equipment by the equipment name \"" + equipName
					+ "\" and the division name \"" + divName + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return listEquip;
	}

	@Override
	public List<Equipment> getByDivision(String nameDivision) throws DBException {
		LOGGER.debug("Try to get the list of eguipments by division \"{}\"", nameDivision);
		List<Equipment> listEquip = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), nameDivision));
			Division  div = session.createQuery(criteriaQuery).getSingleResult();
			listEquip = div.getEquipment();
			listEquip.size();
			session.getTransaction().commit();
			LOGGER.debug("Was got the next list of equipments by division \"{}\": {}",
					nameDivision, listEquip.stream().map(equip->equip.getName()).collect(Collectors.toList()));
		} catch (NoResultException e) {
			LOGGER.debug(
					"Cannot get the list of equipments by division name \"{}\", because the division doesn't exist",
					nameDivision);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMeassage = "Cannot get the list of equipments by division name \"" + nameDivision + "\"";
			LOGGER.error(errorMeassage, e);
			throw new DBException(errorMeassage, e);
		}
		return listEquip;
	}

	@Override
	public List<Equipment> getAllByName(String name) throws DBException {
		LOGGER.debug("Try to get the list of equipments by name \"{}\"", name);
		List<Equipment> listEquip = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Equipment> criteriaQuery = builder.createQuery(Equipment.class);
			Root<Equipment> equipRoot = criteriaQuery.from(Equipment.class);
			criteriaQuery.select(equipRoot);
			criteriaQuery.where(builder.equal(equipRoot.get(Equipment_.name), name));
			listEquip = session.createQuery(criteriaQuery).getResultList();
			session.getTransaction().commit();
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the list of equipments by name \"{}\"," + 
								" because the equipment with this name does not exist", name);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the lis of equipments with name \"" + name + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return listEquip;
	}
}
