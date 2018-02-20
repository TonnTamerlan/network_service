package dbUtil.service;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dataSets.Division;

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
			// TODO: add logging
			throw new DBException("The division with name " + div.getName() + " is already exists", e);
		} catch (Exception e) {
			// TODO: add logging
			// TODO: add processing case when the division already exists
			throw new DBException("Cannot add the devision with name: " + div.getName(), e);
		}
		return result;
	}

	@Override
	public Division getById(long id) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Division div) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(long id) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Division div) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Division> getAll() throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Division getByName(String name) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Division> getByEquipmentName(String equipName) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteByName(String name) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}


}
