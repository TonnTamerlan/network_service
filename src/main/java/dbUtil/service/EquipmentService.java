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
		boolean result = false;
		if (equip == null) {
			String errorMessage = "The eqipment is null";
			LOGGER.debug(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		if (equip.getDivision() == null) {
			String errorMessage = "The eqipment " + equip + " doesn't contains division";
			LOGGER.debug(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			session.save(equip);
			session.getTransaction().commit();
			LOGGER.debug("The equipment {} has added", equip);
			result = true;
		} catch (PersistenceException e) {
			String errorMessage = "The equipment " + equip + " already exists or some its fields are wrong";
			LOGGER.debug(errorMessage, e);
			throw new IllegalArgumentException(errorMessage, e);
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
				LOGGER.debug("The equipment {} has got", equip);
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
		LOGGER.debug("Try to delete the equipment {}", equip);
		boolean result = false;
		
		if (equip == null) {
			LOGGER.debug("Cann't delete the equipment null");
			return result;
		}
		
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			if(session.get(Equipment.class, equip.getId()) != null) {
				session.clear();
				session.delete(equip);
				transaction.commit();
				result = true;
				LOGGER.debug("The equipment {} has deleted", equip);
			} else {
				transaction.commit();
				LOGGER.debug("The equipment {} didn't find", equip);
			}
		} catch (Exception e) {
			String errorMessage = "Cannot delete the equipment " + equip;
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
				LOGGER.debug("The equipment {} has deleted by id", equip);
				result = true;
			} else {
				transanction.commit();
				LOGGER.debug("The equipment with id={} didn' find", id);
			}
		} catch (Exception e) {
			String errorMessage = "Cannot delete the equipment with id=" + id;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public boolean update(Equipment equip) throws DBException {
		LOGGER.debug("Try to update the equipment {}", equip);
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			session.update(equip);
			transaction.commit();
			LOGGER.debug("The equipment {} has updated", equip);
			result = true;
		} catch (Exception e) {
			String errorMessage = "Cannot update the equipment " + equip;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public List<Equipment> getByNameAndDivision(String equipName, String divName) throws DBException {
		LOGGER.debug("Try to get the list of equipment by the equipment name \"{}\" and the division name \"{}\"", 
				equipName, divName);
		if (equipName == null || divName == null) {
			LOGGER.debug("Cannot get the list of equipment with wrong equipment or division names");
			return null;
		}
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
			List<String> listOfEquipmentNames = null;
			if (listEquip.isEmpty()) {
				listEquip = null;
			} else {
				listOfEquipmentNames = listEquip.stream()
						.map(equip -> "id=" + equip.getId() + ", name="
								+ equip.getName())
						.collect(Collectors.toList());
			}
			LOGGER.debug("Was got the next list of equipments \"{}\" by division \"{}\": {}",
					equipName, divName, listOfEquipmentNames);
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the list of equipments which have name \"" + equipName + 
					"\" and belong division \"" + divName + "\", because the division doesn't exist", e);
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
		if (nameDivision == null) {
			LOGGER.debug("The division name is null");
			return null;
		}
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
					nameDivision, listEquip.stream()
						.map(equip->"id=" + equip.getId() + ", name=" + equip.getName())
						.collect(Collectors.toList()));
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the list of equipments by division name \"" + nameDivision
					+ "\", because the division doesn't exist", e);
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
			List<String> listOfEquipmentNames = null;
			if (listEquip.isEmpty()) {
				listEquip = null;
			} else {
				listOfEquipmentNames = listEquip.stream()
						.map(equip -> "id=" + equip.getId() + ", name="
								+ equip.getName() + ", division=" + equip.getDivision().getName())
						.collect(Collectors.toList());
			}
			LOGGER.debug("Was got the next list of equipment: {}", listOfEquipmentNames);
			session.getTransaction().commit();
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the list of equipments by name \"" + name + 
					"\", because the equipment with this name does not exist", e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the lis of equipments with name \"" + name + "\"";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return listEquip;
	}
}
