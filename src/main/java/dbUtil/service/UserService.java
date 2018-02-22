package dbUtil.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import dbUtil.DBException;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Division_;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;
import dbUtil.dataSets.User_;

/**
 * Implementation interface UserDAO for working with table "users" in database
 * 
 * @author Alexey Kopylov
 * 
 * @version 1.0-alfa
 *
 */
public class UserService implements UserDAO {

	final private SessionFactory SESSION_FACTORY;
	
	public UserService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public boolean add(User user) throws DBException {
		boolean result = false;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.persist(user);
			session.getTransaction().commit();
			result = true;
		} catch (PersistenceException e) {
			// TODO: add logging "The user with login: " + user.getLogin() + " is already exists!"
			throw new DBException("The user with login: " + user.getLogin() + " is already exists!", e);
		} catch (Exception e) {
			// TODO: add logging
			throw new DBException("Cannot add User with login: " + user.getLogin(), e);
		}
		return result;
	}

	@Override
	public User getById(long id) throws DBException {
		User user = null;
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			user = session.get(User.class, id);
			if (user != null) {
				user.getDivisions().size(); //for attaching the set of divisions
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging
			throw new DBException("Cannot read the user with id: " + id, e);
		}
		return user;
	}

	@Override
	public User getByLogin(String login) throws DBException{
		User user = null;
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot);
			Predicate predicate = builder.equal(userRoot.get(User_.login), login);
			criteriaQuery.where(predicate);
			user = session.createQuery(criteriaQuery).getSingleResult();
			user.getDivisions().size(); //for attaching the set of divisions
			session.getTransaction().commit();
		} catch (NoResultException e) {
			// TODO: add logging in UserService.getByLogin()
			user = null;
		} catch (Exception e) {
			// TODO: add logging in UserService.getByLogin()
			throw new DBException("Cannot read the user with login: " + login, e);
		}
		return user;
	}

	@Override
	public Set<String> getAllLogins() throws DBException{
		Set<String> userSet = null;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			userSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
		}catch (Exception e) {
			// TODO: add logging in UserService.getAllLogins()
			throw new DBException("Cannot read logins of all users", e);
		}
		return userSet;
	}
	
	@Override
	public Set<String> getByRole(Role role) throws DBException {
		Set<String> userSet = null;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			Predicate predicate = builder.equal(userRoot.get(User_.role), role);
			criteriaQuery.where(predicate);
			userSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging in UserService.getByRole()
			throw new DBException("Cannot read users with role: " + role.name(), e);
		}
		return userSet;
	}

	@Override
	public Set<String> getByDivision(String divisionName) throws DBException{
		Set<String> userSet = null;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), divisionName));
			Division division = session.createQuery(criteriaQuery).getSingleResult();
			if (division != null) {
				userSet = division.getUsers().stream().map(User::getLogin).collect(Collectors.toSet());
			}
			session.getTransaction().commit();
		} catch (NoResultException e) {
			// TODO: add logging
			userSet = new HashSet<String>();
		} catch (Exception e) {
			// TODO: add logging in UserService.getByDivision()
			throw new DBException("Cannot read users with divisions: " + divisionName, e);
		}
		return userSet;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			User user = session.byId(User.class).load(id);
			if(user != null) {
				session.remove(user);
			}
			transaction.commit();
			result = true;
		} catch (Exception e) {
			// TODO Add logging in UserService.deleteByID()
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new DBException("Cannot delet the user with id=" + id, e);
		}
		return result;
	}

	@Override
	public boolean deleteByLogin(String login) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<User> deleteQuery = builder.createCriteriaDelete(User.class);
			Root<User> userRoot = deleteQuery.from(User.class);
			deleteQuery.where(builder.equal(userRoot.get(User_.login), login));
			int numberDelettedRows = session.createQuery(deleteQuery).executeUpdate();
			transaction.commit();
			if(numberDelettedRows != 0) {
				result = true;
			}
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			
			// TODO: add logging in UserService.deleteByLogin()
			throw new DBException("Cannot delete user with login: " + login, e);
		}
		return result;
	}

	@Override
	public boolean delete(User user) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			if (session.get(User.class, user.getId()) != null) {
				session.clear();
				session.remove(user);
				result = true;
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			// TODO: add logging in UserService.delete()
			throw new DBException("Cannot delete user with login: " + user.getLogin(), e);
		}
		return result;
	}

	@Override
	public boolean update(User user) throws DBException {
		boolean result = false;
		Transaction transaction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transaction = session.beginTransaction();
			session.update(user);
			//session.saveOrUpdate(user);
			transaction.commit();
			result= true;
		} catch (Exception e) {
			// TODO: add logging in UserService.update()
			throw new DBException("Cannot update an user with login: " + user.getLogin(), e);
		}
		return result;
	}

	@Override
	public boolean addDivision(String login, Division division) throws DBException {
		boolean result = false;
		Transaction transanction = null;
		try (Session session = SESSION_FACTORY.openSession()) {
			transanction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot);
			Predicate predicate = builder.equal(userRoot.get(User_.login), login);
			criteriaQuery.where(predicate);
			User user = session.createQuery(criteriaQuery).getSingleResult();
			user.addDivision(division);
			session.update(user);
			transanction.commit();
			result = true;
		} catch (NoResultException e) {
			// TODO: add logging in UserService.addDivision()
		} catch (Exception e) {
			// TODO: add logging to UserService.addDevision
			if (transanction != null && transanction.isActive()) {
				transanction.rollback();
			}
		}
		return result;
	}

}
