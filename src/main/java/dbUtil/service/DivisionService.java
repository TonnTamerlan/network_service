package dbUtil.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Division_;
import dbUtil.dataSets.Equipment;
import dbUtil.dataSets.User;

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
		LOGGER.info("Try to add the division {} in the repository", div);
		boolean result = false;
		if (div == null) {
			String errorMesage = "The division is null";
			LOGGER.debug(errorMesage);
			throw new IllegalArgumentException(errorMesage);
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			session.save(div);
			for (User user : div.getUsers()) {
				if (user.getId() != 0) {
					session.update(user);
				} else {
					String errorMessage = "The user " + user + " doesn't exist in the repository";
					LOGGER.debug(errorMessage);
					throw new IllegalArgumentException(errorMessage);
				}
			}
			transaction.commit();
			LOGGER.info("The division {} has added", div);
			result = true;
		} catch (PersistenceException | IllegalArgumentException e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction", ignored);
			}
			String errorMessage = "The division " + div + " already exists or some its fields are wrong";
			LOGGER.error(errorMessage, e);
			throw new IllegalArgumentException(errorMessage, e);
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot add the devision " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return result;
	}

	@Override
	public Division getById(long id) throws DBException {
		LOGGER.info("Try to get a division by id={}", id);
		Division division = null;
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			division = session.get(Division.class, id);
			if (division != null) {
				division.getUsers().size(); //for attaching the set of users
				division.getEquipment().size(); //for attaching the set of equipments
				division.getSlaveDivisions().size(); //for attaching the set of slaveDivision
				session.getTransaction().commit();
				LOGGER.info("The division {} has got", division);
			} else {
				session.getTransaction().commit();
				LOGGER.info("The division with id={} didn't find", id);
			}
			
		} catch (Exception e) {
			String errorMessage = "Cannot read the devision with id=" + id + " by ID";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}  finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return division;
	}

	@Override
	public boolean delete(Division div) throws DBException {
		LOGGER.info("Try to delete the division {}", div);
		boolean result = false;
		if (div == null) {
			LOGGER.debug("Cannot delete the division null");
			return result;
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			
			if (session.get(Division.class, div.getId()) != null) {
				session.clear();
				div.getUsers().forEach(user->user.getDivisions().remove(div));
				for (User user : div.getUsers()) {
					session.update(user);
					LOGGER.info("Delete the division from the user {}", user);
				}
				div.getEquipment().forEach(equip->LOGGER.info("Delete the equipment {} which was installed in the division", equip));
				session.delete(div);
				transaction.commit();
				LOGGER.info("The division {} has deleted", div);
				result = true;
			} else {
				transaction.commit();
				LOGGER.info("The division {} didn't find", div);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transanction", ignored);
			}
			String errorMessage = "Cannot delete the division " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return result;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		LOGGER.info("Try to delete the division by id={}", id);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			Division division = session.get(Division.class, id);
			if(division != null) {
				division.getUsers().forEach(user->user.getDivisions().remove(division));
				for (User user : division.getUsers()) {
					session.update(user);
					LOGGER.info("Delete the division from the user {}", user);
				}
				division.getEquipment().forEach(equip->LOGGER.info("Delete the equipment {} which was installed in the division", equip));
				session.remove(division);
				transaction.commit();
				LOGGER.info("The division {} has deleted", division);
				result = true;
			} else {
				transaction.commit();
				LOGGER.info("The division with id={} didn't find", id);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transanction", ignored);
			}
			String errorMessage = "Cannot delete the division with id=" + id + " by ID";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return result;
	}

	@Override
	public boolean update(Division div) throws DBException {
		LOGGER.info("Try to update the division {}", div);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			
			//for deleting equipments and users if necessarily
			Division nonUpdatingDivision = session.get(Division.class, div.getId());
			List<Equipment> oldEquipmentList = nonUpdatingDivision.getEquipment();
			Set<User> oldUserSet = nonUpdatingDivision.getUsers();
			//for attaching the list, because using lazy initialization
			oldEquipmentList.size();
			oldUserSet.size();
			session.detach(nonUpdatingDivision);
			if (!isSameLists(div.getEquipment(), oldEquipmentList)) {
				for (Equipment equip : oldEquipmentList) {
					if (!div.getEquipment().contains(equip)) {
						session.remove(equip);
						LOGGER.info("The equipment {} has deleted", equip);
					}
				}
			}
			if (!div.getUsers().equals(oldUserSet)) {
				for (User user : oldUserSet) {
					if(!div.getUsers().contains(user)) {
						session.get(User.class, user.getId()).deleteDivision(div);
					}
				}
				for (User user : div.getUsers()) {
					if(!oldUserSet.contains(user)) {
						session.get(User.class, user.getId()).addDivision(div);
					}
				}
				session.replicate(div, ReplicationMode.OVERWRITE);
			}
			session.update(div);
			transaction.commit();
			LOGGER.info("The division {} has updated", div);
			result= true;
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transanction", ignored);
			}
			String errorMessage = "Cannot update the division " + div;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return result;
	}

	@Override
	public Set<String> getAllNames() throws DBException {
		LOGGER.debug("Try to get all the division names");
		Set<String> divisionSet = Collections.emptySet();
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Sesion is open");
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot.get(Division_.name));
			divisionSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
			LOGGER.info("Was got the next set of division names: {}", divisionSet.toString());
		} catch (Exception e) {
			String errorMessage = "Cannot get names of all divisions";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return divisionSet;
	}

	@Override
	public Division getByName(String name) throws DBException {
		LOGGER.info("Try to get the divisin by name=\"{}\"", name);
		Division division = null;
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
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
			LOGGER.info("The division {} has got", division);
		} catch (NoResultException e) {
			LOGGER.info("Cannot get the division \"" + name + "\" by name, because it does not exist", e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the division \"" + name + "\" by name";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return division;
	}

	@Override
	public boolean deleteByName(String name) throws DBException {
		LOGGER.info("Try to delete the division by name=\"{}\"", name);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Sesion is open");
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			Division division = session
					.createQuery(criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), name)))
					.getSingleResult();
			division.getUsers().forEach(act -> act.getDivisions().remove(division));
			for (User user : division.getUsers()) {
				session.update(user);
				LOGGER.info("The user {} has updated", user);
			}
			division.getEquipment().forEach(equip -> LOGGER.info("The equipment {} has deleted", equip));
			session.delete(division);
			transaction.commit();
			LOGGER.info("The division {} has deleted", division);
			result = true;
		} catch (NoResultException e) {
			LOGGER.info("Cannot delete the division \"" + name + "\" by name, because it doesn't exist", e);
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transanction", ignored);
			}
			String errorMessage = "Cannot delete the devision \"" + name + "\" by name";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		} finally {
			try {
				session.close();
				LOGGER.trace("Session is close");
			} catch (Exception ignored) {
				LOGGER.error("Cannot close the session", ignored);
			}
		}
		return result;
	}
	
	private <T> boolean isSameLists(List<T> expectedList, List<T> actualList) {
		if (expectedList == null && actualList == null) {
			return true;
		} else if (expectedList == null || actualList == null) {
			return false;
		}
		return expectedList.size() == actualList.size() && expectedList.containsAll(actualList);
	}

}
