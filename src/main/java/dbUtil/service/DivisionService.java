package dbUtil.service;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

	private static final Logger logger = LogManager.getLogger(DivisionService.class);
	
	final private SessionFactory SESSION_FACTORY;
	
	public DivisionService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public boolean add(Division div) throws DBException {
		logger.debug("Start to add the division \"{}\" to repository", div.getName());
		boolean result = false;
		try (Session session = SESSION_FACTORY.openSession()){
			logger.debug("Session opened");
			session.beginTransaction();
			session.persist(div);
			session.getTransaction().commit();
			logger.debug("The division \"{}\" has added", div.getName());
			result = true;
		} catch (PersistenceException e) {
			logger.info("The division \"{}\" is already exists", div.getName());
			throw new DBException("The division with name " + div.getName() + " is already exists", e);
		} catch (Exception e) {
			logger.warn("Cannot add the devision \"" + div.getName() + "\"", e);
			throw new DBException("Cannot add the devision \"" + div.getName() + "\"", e);
		}
		return result;
	}

	@Override
	public Division getById(long id) throws DBException {
		logger.debug("Try to get the division with id={}", id);
		Division division = null;
		try(Session session = SESSION_FACTORY.openSession()){
			logger.debug("Session opened");
			session.beginTransaction();
			division = session.get(Division.class, id);
			if (division != null) {
				division.getUsers().size(); //for attaching the set of users
				division.getEquipment().size(); //for attaching the set of equipments
				division.getSlaveDivisions().size(); //for attaching the set of slaveDivision
			}
			session.getTransaction().commit();
			logger.debug("The division with id={} has got and has name \"{}\"", division.getId(), division.getName());
		} catch (Exception e) {
			logger.warn("Cannot read the devision with id: " + id, e);
			throw new DBException("Cannot read the devision with id: " + id, e);
		}
		return division;
	}

	@Override
	public boolean delete(Division div) throws DBException {
		logger.debug("Try to delete the division \"{}\"", div.getName());
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			logger.debug("Session opened");
			transaction = session.beginTransaction();
			if (session.get(Division.class, div.getId()) != null) {
				session.clear();
				div.getUsers().forEach(user->user.getDivisions().remove(div));
				div.getUsers().stream().peek(user->logger.debug("Delete the division from user\"{}\"", user.getLogin())).forEach(user->session.update(user));
				div.getEquipment().stream().peek(equip->logger.debug("Delete an equipment \"{}\" which was installed in the division", equip.getName())).forEach(equip->session.delete(equip));
				session.delete(div);
				transaction.commit();
				logger.debug("The division \"{}\" has deleted", div.getName());
				result = true;
			}
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			logger.warn("Cannot delete the division \"" + div.getName() + "\"", e);
			throw new DBException("Cannot delete the division \"" + div.getName() + "\"", e);
		}
		return result;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			Division division = session.byId(Division.class).load(id);
			if(division != null) {
				division.getUsers().forEach(act->act.getDivisions().remove(division));
				division.getUsers().forEach(act->session.update(act));
				division.getEquipment().forEach(equip->session.delete(equip));
				session.remove(division);
			}
			transaction.commit();
			result = true;
		} catch (Exception e) {
			// TODO Add logging in UserService.deleteByID()
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new DBException("Cannot delet the division with id=" + id, e);
		}
		return result;
	}

	@Override
	public boolean update(Division div) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			session.update(div);
			//session.saveOrUpdate(div);
			transaction.commit();
			result= true;
		} catch (Exception e) {
			// TODO: add logging in DivisionService.update()
			throw new DBException("Cannot update a division with name: " + div.getName(), e);
		}
		return result;
	}

	@Override
	public Set<String> getAllNames() throws DBException {
		Set<String> divisionSet = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot.get(Division_.name));
			divisionSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging in DivisionService.getAll()
			throw new DBException("Cannot read names of all divisions", e);
		}
		return divisionSet;
	}

	@Override
	public Division getByName(String name) throws DBException {
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
		} catch (Exception e) {
			// TODO: add logging in DevisionService.getByName()
			throw new DBException("Cannot read a division with name: " + name, e);
		}
		return division;
	}

	@Override
	public boolean deleteByName(String name) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			Division division = session.createQuery(criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), name))).getSingleResult();
			if (division != null) {
				division.getUsers().forEach(act->act.getDivisions().remove(division));
				division.getUsers().forEach(act->session.update(act));
				division.getEquipment().forEach(equip->session.delete(equip));
				session.delete(division);
			}
			transaction.commit();
			result = true;
		} catch (NoResultException e) {
			throw new DBException("Cannot delete a devision with name: " + name + ", because the division doesn't exist", e);
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			// TODO: add logging to DivisionService.deleteByName
			throw new DBException("Cannot delete a devision with name: " + name, e);
		}
		return result;
	}

}
