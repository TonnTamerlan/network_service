package dbUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBService {
	private SessionFactory sessionFactory = null;
	private String CFG_RESOURCE_NAME = "hibernate.cfg.xml";
	
	
	//TODO add and configure normal logging
	private static Logger logger = Logger.getLogger(DBService.class.getName());
	
	
	public DBService() {
		this.start();
	}

	@SuppressWarnings("unused")
	@Deprecated
	private SessionFactory createSessionFactory(Configuration cfg) {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
				.configure("hibernate.cfg.xml")
				.build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		return metaData.getSessionFactoryBuilder().build();
	}

	public void stop() {
		if(this.isWorked()) {
			sessionFactory.close();
		}
		logger.log(Level.INFO, "Data base service is closed");
	}

	public boolean isWorked() {
		return sessionFactory != null && sessionFactory.isOpen();
	}

	public void start() {
		if(sessionFactory == null || sessionFactory.isClosed()) {
			Configuration cfg = new Configuration().configure(CFG_RESOURCE_NAME);
			sessionFactory = cfg.buildSessionFactory();
		}
		logger.log(Level.INFO, "Data base service is started");
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
