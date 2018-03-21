package dbUtil.service;

import java.util.Collections;
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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger LOGGER = LogManager.getLogger(UserService.class);

	final private SessionFactory SESSION_FACTORY;

	public UserService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}

	@Override
	public boolean add(User user) throws DBException {
		LOGGER.debug("Try to add the user {} in the repository", user);
		boolean result = false;
		if (user == null) {
			String errorMessage = "The user is null";
			LOGGER.debug(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			for (Division div : user.getDivisions()) {
				if (session.get(Division.class, div.getId()) == null) {
					String errorMessage = "The division " + div + " doesn't exist in the repository";
					LOGGER.debug(errorMessage);
					throw new IllegalArgumentException(errorMessage);
				}
			}
			session.save(user);
			transaction.commit();
			LOGGER.debug("The user {} has added", user);
			result = true;
		} catch (PersistenceException e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction", ignored);
			}
			String errorMessage = "The user" + user + " already exists or some its fields are wrong!";
			LOGGER.error(errorMessage, e);
			throw new IllegalArgumentException(errorMessage, e);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot add the user " + user;
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
	public User getById(long id) throws DBException {
		LOGGER.debug("Try to get a user by id={}", id);
		User user = null;
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			user = session.get(User.class, id);
			if (user != null) {
				user.getDivisions().size(); // for attaching the set of divisions
				session.getTransaction().commit();
				LOGGER.debug("The user \"{}\" with login \"{}\" and id={} has got", user.getLastName(), user.getLogin(),
						user.getId());
			} else {
				session.getTransaction().commit();
				LOGGER.debug("The user with id={} didn't find", id);
			}

		} catch (Exception e) {
			String errorMessage = "Cannot read the user with id=" + id;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return user;
	}

	@Override
	public User getByLogin(String login) throws DBException {
		LOGGER.debug("Try to get the user with login=\"{}\"", login);
		User user = null;
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot);
			Predicate predicate = builder.equal(userRoot.get(User_.login), login);
			criteriaQuery.where(predicate);
			user = session.createQuery(criteriaQuery).getSingleResult();
			user.getDivisions().size(); // for attaching the set of divisions
			session.getTransaction().commit();
			LOGGER.debug("The user \"{}\" with login \"{}\" and id={} has got", user.getLastName(), user.getLogin(),
					user.getId());
		} catch (NoResultException e) {
			LOGGER.debug("Cannot get the user \"{}\" by login, because it does not exist", login);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMessage = "Cannot get the user with login \"" + login + "\" by login";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return user;
	}

	@Override
	public Set<String> getAllLogins() throws DBException {
		LOGGER.debug("Try to get all the user logins");
		Set<String> userSet = Collections.emptySet();
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			userSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
			LOGGER.debug("Was got next user logins: {}", userSet.toString());
		} catch (Exception e) {
			String errorMessage = "Cannot read logins of all users";
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return userSet;
	}

	@Override
	public Set<String> getByRole(Role role) throws DBException {
		LOGGER.debug("Try to get user names by role \"{}\"", role);
		Set<String> userSet = null;
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot.get(User_.login));
			Predicate predicate = builder.equal(userRoot.get(User_.role), role);
			criteriaQuery.where(predicate);
			userSet = new HashSet<String>(session.createQuery(criteriaQuery).getResultList());
			session.getTransaction().commit();
			LOGGER.debug("Was got next users which have role \"{}\": {}", role, userSet.toString());
		} catch (Exception e) {
			String errorMessage = "Cannot get user logins by role: " + role.name();
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return userSet;
	}

	@Override
	public Set<String> getByDivision(String divisionName) throws DBException {
		LOGGER.debug("Try to get user logins by the division name \"{}\"", divisionName);
		Set<String> userSet = Collections.emptySet();
		Session session = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Division> criteriaQuery = builder.createQuery(Division.class);
			Root<Division> divisionRoot = criteriaQuery.from(Division.class);
			criteriaQuery.select(divisionRoot);
			criteriaQuery.where(builder.equal(divisionRoot.get(Division_.name), divisionName));
			Division division = session.createQuery(criteriaQuery).getSingleResult();
			LOGGER.debug("Was got the division \"{}\" with id={}", division.getName(), division.getId());
			userSet = division.getUsers().stream().map(User::getLogin).collect(Collectors.toSet());
			session.getTransaction().commit();
			LOGGER.debug("Was got next users which belong the division \"{}\" with id={}: {}", division.getName(),
					division.getId(), userSet.toString());
		} catch (NoResultException e) {
			LOGGER.debug("Cannot find the division \"{}\"", divisionName);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			String errorMessage = "Cannot get user logins by division: " + divisionName;
			LOGGER.error(errorMessage, e);
			throw new DBException(errorMessage, e);
		}
		return userSet;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		LOGGER.debug("Try to delete the user by id={}", id);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			User user = session.byId(User.class).load(id);
			if (user != null) {
				session.remove(user);
				transaction.commit();
				LOGGER.debug("The user \"{}\" with login \"{}\" anf id={} was deleted", 
						user.getLastName(), user.getLogin(), user.getId());
				result = true;
			} else {
				transaction.commit();
				LOGGER.debug("The user with id={} does not exist", id);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot delete the user with id=" + id;
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
	public boolean deleteByLogin(String login) throws DBException {
		LOGGER.debug("Try to delete a user by login=\"{}\"", login);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<User> deleteQuery = builder.createCriteriaDelete(User.class);
			Root<User> userRoot = deleteQuery.from(User.class);
			deleteQuery.where(builder.equal(userRoot.get(User_.login), login));
			int numberDelettedRows = session.createQuery(deleteQuery).executeUpdate();
			transaction.commit();
			if (numberDelettedRows != 0) {
				LOGGER.debug("The user with login \"{}\" was deleted", login);
				result = true;
			} else {
				LOGGER.debug("The user with login \"{}\" doesn't exist", login);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot delete user with login: " + login;
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
	public boolean delete(User user) throws DBException {
		LOGGER.debug("Try to delete the user {}", user);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			if (session.get(User.class, user.getId()) != null) {
				session.clear();
				session.remove(user);
				transaction.commit();
				LOGGER.debug("The user {} was deleted", user);
				result = true;
			} else {
				transaction.commit();
				LOGGER.debug("The user {} didn't find", user);
			}
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot delete the user " + user;
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
	public boolean update(User user) throws DBException {
		LOGGER.debug("Try to update the user \"{}\" with login \"{}\" and id={}",
				user.getLastName(), user.getLogin(), user.getId());
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			session.update(user);
			transaction.commit();
			LOGGER.debug("The user \"{}\" with login \"{}\" and id={} has updated",
					user.getLastName(), user.getLogin(), user.getId());
			result = true;
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot update the user \"" + user.getLastName() +
					"\" with login \"" + user.getLogin() + "\" and id=" + user.getId();
			LOGGER.error(errorMessage , e);
			throw new DBException(errorMessage , e);
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
	public boolean addDivision(String login, Division division) throws DBException {
		LOGGER.debug("Try to add the division \"{}\" with id={} in the user with login \"{}\"", division.getName(),
				division.getId(), login);
		boolean result = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = SESSION_FACTORY.openSession();
			LOGGER.trace("Session is open");
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> userRoot = criteriaQuery.from(User.class);
			criteriaQuery.select(userRoot);
			Predicate predicate = builder.equal(userRoot.get(User_.login), login);
			criteriaQuery.where(predicate);
			User user = session.createQuery(criteriaQuery).getSingleResult();
			user.addDivision(division);
			transaction.commit();
			result = true;
			LOGGER.debug("The division \"{}\" with id={} was added in the user \"{}\" with login \"{}\" and id={}",
					division.getName(), division.getId(), user.getLastName(), user.getLogin(), user.getId());
		} catch (NoResultException e) {
			LOGGER.debug("The user with login=\"{}\" doesn't exist", login);
			LOGGER.catching(Level.DEBUG, e);
		} catch (Exception e) {
			try {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
					LOGGER.trace("The transaction rollbacked");
				}
			} catch (Exception ignored) {
				LOGGER.error("Cannot rollback transaction in method add", ignored);
			}
			String errorMessage = "Cannot to add the division \"" + division.getName() + "\" with id=" + division.getId()
					+ " to the user with login \"" + login + "\"";
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
}
