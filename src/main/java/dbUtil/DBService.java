package dbUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DBService {
	
	final static Logger LOGGER = LogManager.getLogger(DBService.class.getName());
	
	private SessionFactory sessionFactory = null;
	private String CFG_RESOURCE_NAME = "hibernate.cfg.xml";
	
	public DBService() {
		this.start();
		LOGGER.debug("Data base service created and started");
	}

	public void stop() {
		if(this.isWorked()) {
			sessionFactory.close();
		}
		LOGGER.debug("Data base service is stopped");
	}

	public boolean isWorked() {
		return sessionFactory != null && sessionFactory.isOpen();
	}

	public void start() {
		if(sessionFactory == null || sessionFactory.isClosed()) {
			Configuration cfg = new Configuration().configure(CFG_RESOURCE_NAME);
			sessionFactory = cfg.buildSessionFactory();
		}
		LOGGER.debug("Data base service is started");
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
