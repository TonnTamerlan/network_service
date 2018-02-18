package dbUtil.service;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import dbUtil.DBException;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;
import dbUtil.dataSets.User_;

/**
 * Implementation interface UserDAO for working with table "users" in database
 * 
 * @author Alexey Kopylov
 *
 */
public class UserService implements UserDAO {

	final private SessionFactory SESSION_FACTORY;
	
	public UserService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public void add(User user) throws DBException {
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.persist(user);
			session.getTransaction().commit();
		} catch (PersistenceException e) {
			// TODO: add logging
			throw e;
		} catch (Exception e) {
			// TODO: add logging
			// TODO: add processing case when the user already exists
			throw new DBException("Cannot add User with login: " + user.getLogin(), e);
		}
	}

	@Override
	public User getByID(long id) throws DBException{
		User user = new User();
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.load(user, id);
			user.getDivisions().size(); //for attaching the set of divisions
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
	public List<String> getAllLogins() throws DBException{
		List<String> userList = null;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			userList = session.createQuery(criteriaQuery).getResultList();
			session.getTransaction().commit();
		}catch (Exception e) {
			// TODO: add logging in UserService.getAllLogins()
			throw new DBException("Cannot read logins of all users", e);
		}
		return userList;
	}
	
	@Override
	public List<String> getByRole(Role role) throws DBException {
		List<String> userList = null;
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			Predicate predicate = builder.equal(userRoot.get(User_.role), role);
			criteriaQuery.where(predicate);
			userList = session.createQuery(criteriaQuery).getResultList();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging in UserService.getByRole()
			throw new DBException("Cannot read users with role: " + role.name(), e);
		}
		return userList;
	}

	@Override
	public List<String> getByDevision(String devisionName) throws DBException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByID(long id) throws DBException{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByLogin(String login) throws DBException{
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(User user) throws DBException{
		// TODO Auto-generated method stub

	}

	@Override
	public void update(User user) throws DBException{
		// TODO Auto-generated method stub

	}

	@Override
	public void addDivision(String login, Division division) throws DBException{
		// TODO Auto-generated method stub

	}

}
