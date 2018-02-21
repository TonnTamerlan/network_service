package dbUtil.service;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
		Division division = null;
		try(Session session = SESSION_FACTORY.openSession()){
			session.beginTransaction();
			division = session.get(Division.class, id);
			if (division != null) {
				division.getUsers().size(); //for attaching the set of users
				division.getUnits().size(); //for attaching the set of units
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: add logging
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
				session.remove(div);
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
