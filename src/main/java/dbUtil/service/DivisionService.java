package dbUtil.service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

	
	final private SessionFactory SESSION_FACTORY;
	
	public DivisionService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public boolean add(Division div) throws DBException {
		boolean result = false;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.persist(div);
			session.getTransaction().commit();
			result = true;
		} catch (PersistenceException e) {
			// TODO: add logging in DivisionService.add()
			throw new DBException("The division with name " + div.getName() + " is already exists", e);
		} catch (Exception e) {
			// TODO: add logging in DivisionService.add()
			throw new DBException("Cannot add the devision with name: " + div.getName(), e);
		}
		return result;
	}

	@Override
	public Division getById(long id) throws DBException {
		Division division = null;
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			division = session.get(Division.class, id);
			if (division != null) {
				division.getUsers().size(); //for attaching the set of users
				division.getEquipment().size(); //for attaching the set of equipments
				division.getSlaveDivisions().size(); //for attaching the set of slaveDivision
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging in DivisionService.getById()
			throw new DBException("Cannot read the devision with id: " + id, e);
		}
		return division;
	}

	@Override
	public boolean delete(Division div) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			if (session.get(Division.class, div.getId()) != null) {
				session.clear();
				div.getUsers().forEach(user->user.getDivisions().remove(div));
				div.getUsers().forEach(user->session.update(user));
				div.getEquipment().forEach(equip->session.delete(equip));
				session.delete(div);
				result = true;
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			// TODO: add logging in DivisionService.delete()
			throw new DBException("Cannot delete the division with name: " + div.getName(), e);
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
