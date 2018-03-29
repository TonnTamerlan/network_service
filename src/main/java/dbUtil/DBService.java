package dbUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dbUtil.dao.DivisionDAO;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dao.UserDAO;
import dbUtil.service.DivisionService;
import dbUtil.service.EquipmentService;
import dbUtil.service.UserService;


public class DBService {
	
	final static Logger LOGGER = LogManager.getLogger(DBService.class.getName());
	
	final private static DBService dbService = new DBService();
	private SessionFactory sessionFactory = null;
	private String CFG_RESOURCE_NAME = "hibernate.cfg.xml";
	
	
	private DBService() {
		start();
		LOGGER.debug("Data base service created and started");
	}

	public static DBService getInstance() {
		return dbService;
	}
	
	public void stop() {
		if(isWorked()) {
			sessionFactory.close();
		}
		LOGGER.debug("Data base service is stopped");
	}

	public boolean isWorked() {
		return sessionFactory != null && sessionFactory.isOpen();
	}

	public SessionFactory start() {
		if(sessionFactory == null || sessionFactory.isClosed()) {
			Configuration cfg = new Configuration().configure(CFG_RESOURCE_NAME);
			sessionFactory = cfg.buildSessionFactory();
		}
		LOGGER.debug("Data base service is started");
		return sessionFactory;
	}
	
	public UserDAO getUserService() {
		return new UserService(start());
	}
	
	public DivisionDAO getDivisionService() {
		return new DivisionService(start());
	}
	
	public EquipmentDAO getEquipmentService() {
		return new EquipmentService(start());
	}

}
