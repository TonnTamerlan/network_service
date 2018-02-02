package dbService;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import dbService.dataSets.UserDataSet;

public class DBServiceImpl implements DBService {
	private SessionFactory sessionFactory = null;
	private String CFG_RESOURCE_NAME = StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME;

	public DBServiceImpl() {
		Configuration cfg = new Configuration().configure(CFG_RESOURCE_NAME);
		cfg.addAnnotatedClass(UserDataSet.class);
		sessionFactory = createSessionFactory(cfg);
	}

	private SessionFactory createSessionFactory(Configuration cfg) {
		/*
		 * StandardServiceRegistry registry = new
		 * StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		 * return cfg.buildSessionFactory(registry);
		 */
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
				.configure("hibernate.cfg.xml")
				.build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		return metaData.getSessionFactoryBuilder().build();
	}

}
