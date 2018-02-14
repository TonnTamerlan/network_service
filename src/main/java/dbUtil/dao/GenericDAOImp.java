package dbUtil.dao;

import org.hibernate.Session;

import dbUtil.dataSets.Model;

public class GenericDAOImp<T extends Model> implements GenericDAO<T> {

	private Session session = null;
	private Class<T> type;
	

	public GenericDAOImp(Class<T> type, Session session) {
		this.session = session;
		this.type = type;
	}
	
	@Override
	public long create(T newInstance) {
		return (long) session.save(newInstance);
	}

	@Override
	public T read(long id) {
		return (T) session.get(type, id);
	}

	@Override
	public void update(T transientObject) {
		session.update(transientObject);
	}

	@Override
	public void delete(T persistentObject) {
		session.delete(persistentObject);
	}

}
