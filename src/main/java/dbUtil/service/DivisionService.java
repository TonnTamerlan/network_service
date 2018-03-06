package dbUtil.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
import dbUtil.dao.DivisionDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Division_;

/**
 * Implementation interface DivisionDAO for working with table "divisions"
 * 
 * @author Alexey Kopylov
 * 
 * @version 1.0.-alfa
 *
 */
public class DivisionService implements DivisionDAO {

	private static final Logger LOGGER = LogManager.getLogger(DivisionService.class);
	
	final private SessionFactory SESSION_FACTORY;
	
	public DivisionService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public boolean add(Division div) throws DBException {
		LOGGER.debug("Try to add the division {} in the repository", div);
		boolean result = false;
		if (div == null) {
			String errorMesage = "The division is null";
			LOGGER.debug(errorMesage);
			throw new IllegalArgumentException(errorMesage);
		}
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.persist(div);
			session.getTransaction().commit();
			LOGGER.debug("The division {} has added", div);
			result = true;
		} catch (PersistenceException e) {
			String errorMessage = "The division " + div + " already exists or some its fields are wrong";
			LOGGER.debug(errorMessage, e);
			throw new IllegalArgumentException(errorMessage, e);
		} catch (Exception e) {
			String errorMessage = "Cannot add the devision " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public Division getById(long id) throws DBException {
		LOGGER.debug("Try to get a division by id={}", id);
		Division division = null;
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			division = session.get(Division.class, id);
			if (division != null) {
				division.getUsers().size(); //for attaching the set of users
				division.getEquipment().size(); //for attaching the set of equipments
				division.getSlaveDivisions().size(); //for attaching the set of slaveDivision
				session.getTransaction().commit();
				LOGGER.debug("The division {} has got", division);
			} else {
				session.getTransaction().commit();
				LOGGER.debug("The division with id={} didn't find", id);
			}
			
		} catch (Exception e) {
			String errorMessage = "Cannot read the devision with id=" + id + " by ID";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return division;
	}

	@Override
	public boolean delete(Division div) throws DBException {
		LOGGER.debug("Try to delete the division {}", div);
		boolean result = false;
		
		if (div == null) {
			LOGGER.debug("Cannot delete the division null");
			return result;
		}
		
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			
			if (session.get(Division.class, div.getId()) != null) {
				session.clear();
				div.getUsers().forEach(user->user.getDivisions().remove(div));
				div.getUsers().stream()
							.peek(user->LOGGER.debug("Delete the division from the user {}", user))
							.forEach(user->session.update(user));
				div.getEquipment().stream()
							.peek(equip->LOGGER.debug("Delete the equipment {} which was installed in the division", equip))
							.forEach(equip->session.delete(equip));
				session.delete(div);
				transaction.commit();
				LOGGER.debug("The division {} has deleted", div);
				result = true;
			} else {
				transaction.commit();
				LOGGER.debug("The division {} didn't find", div);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
			} catch (Exception ex) {
				LOGGER.error("Cannot rollback transanction when the division " + div + " was trying to delete", ex);
			}
			String errorMessage = "Cannot delete the division " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		LOGGER.debug("Try to delete the division by id={}", id);
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			Division division = session.get(Division.class, id);
			if(division != null) {
				division.getUsers().forEach(user->user.getDivisions().remove(division));
				division.getUsers().stream()
							.peek(user->LOGGER.debug("Delete the division {} from user {}", division, user))
							.forEach(user->session.update(user));;
				division.getEquipment().stream()
							.peek(equip->LOGGER.debug("Delete the equipment {} which was installed in the division {}", equip, division))
							.forEach(equip->session.delete(equip));
				session.remove(division);
				transaction.commit();
				LOGGER.debug("The division {} has deleted", division);
				result = true;
			} else {
				transaction.commit();
				LOGGER.debug("The division with id={} didn't find", id);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
			} catch (Exception ex) {
				LOGGER.error("Cannot rollback transanction when the division with id=" + id + " was trying to delete", ex);
			}
			String errorMessage = "Cannot delete the division with id=" + id + " by ID";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public boolean update(Division div) throws DBException {
		LOGGER.debug("Try to update the division {}", div);
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			session.update(div);
			transaction.commit();
			LOGGER.debug("The division {} has updated", div);
			result= true;
		} catch (Exception e) {
			String errorMessage = "Cannot update the division " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

	@Override
	public Set<String> getAllNames() throws DBException {
		LOGGER.debug("Try to get all the division names");
		Set<String> divisionSet = Collections.emptySet();
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot.get(Division_.name));
			divisionSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
			LOGGER.debug("Was got the next set of division names: {}", divisionSet.toString());
		} catch (Exception e) {
			String errorMessage = "Cannot get names of all divisions";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return divisionSet;
	}

	@Override
	public Division getByName(String name) throws DBException {
		LOGGER.debug("Try to get the divisin by name=\"{}\"", name);
		Division division = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			division = session.createQuery(criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), name))).getSingleResult();
			division.getEquipment().size(); //for attaching the set of equipments
			division.getUsers().size(); //for attaching the set of users
			division.getSlaveDivisions(); //for attaching the set of slaveDevisions
			session.getTransaction().commit();
			LOGGER.debug("The division {} has got", division);
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the division \"{}\" by name, because it does not exist", name);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the division \"" + name + "\" by name";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return division;
	}

	@Override
	public boolean deleteByName(String name) throws DBException {
		LOGGER.debug("Try to delete the division by name=\"{}\"", name);
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			Division division = session
					.createQuery(criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), name)))
					.getSingleResult();
			division.getUsers().forEach(act -> act.getDivisions().remove(division));
			division.getUsers().forEach(act -> session.update(act));
			division.getEquipment().forEach(equip -> session.delete(equip));
			session.delete(division);
			transaction.commit();
			LOGGER.debug("The division {} has deleted", division);
			result = true;
		} catch (NoResultException e) {
			LOGGER.debug("Cannot delete the division \"{}\" by name, because it doesn't exist");
			LOGGER.catching(Level.DEBUG,e);
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			String errorMessage = "Cannot delete the devision \"" + name + "\" by name";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return result;
	}

}
