package dbUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DBService {
	
	final static Logger LOGGER = LogManager.getLogger(DBService.class.getName());
	
	private static SessionFactory sessionFactory = null;
	private static String CFG_RESOURCE_NAME = "hibernate.cfg.xml";
	
	public DBService() {
		start();
		LOGGER.debug("Data base service created and started");
	}

	public static void stop() {
		if(isWorked()) {
			sessionFactory.close();
		}
		LOGGER.debug("Data base service is stopped");
	}

	public static boolean isWorked() {
		return sessionFactory != null && sessionFactory.isOpen();
	}

	public static SessionFactory start() {
		if(sessionFactory == null || sessionFactory.isClosed()) {
			Configuration cfg = new Configuration().configure(CFG_RESOURCE_NAME);
			sessionFactory = cfg.buildSessionFactory();
		}
		LOGGER.debug("Data base service is started");
		return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
