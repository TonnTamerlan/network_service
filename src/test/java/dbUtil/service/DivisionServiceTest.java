package dbUtil.service;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DivisionServiceTest {
	private SessionFactory sessionFactory = null;
	
	@BeforeAll
	void initAll() {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
	}
	
	@Test
	void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllNames() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByName() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteByName() {
		fail("Not yet implemented");
	}
	
	@AfterAll
	void finish() {
		sessionFactory.close();
	}

}
