package dbUtil.service;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import dbUtil.DBException;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.User;

public class UserService implements UserDAO {

	final private SessionFactory SESSION_FACTORY;
	
	public UserService(final SessionFactory sessionFactory) {
		this.SESSION_FACTORY = sessionFactory;
	}
	
	@Override
	public void add(User user) throws DBException {
		try (Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging
			throw new DBException("Cannot add User login: " + user.getLogin(), e);
		}
	}

	@Override
	public User getByID(long id) throws DBException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getByLogin(String login) throws DBException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<User> getAll() throws DBException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<User> getUsersFromDevision(String devisionName) throws DBException{
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
