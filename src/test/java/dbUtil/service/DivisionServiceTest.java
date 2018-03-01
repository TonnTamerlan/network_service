package dbUtil.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dbUtil.DBException;
import dbUtil.dataSets.Division;


public class DivisionServiceTest {

	private static SessionFactory sessionFactory = null;
	private static DivisionService divisionService = null;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		divisionService = new DivisionService(sessionFactory);
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		sessionFactory.close();
		sessionFactory = null;
		divisionService = null;
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() throws DBException {
		assertTrue(divisionService.add(getDivision("ONE")));
		//fail("Not yet implemented");
	}

	@Test
	public void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllNames() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteByName() {
		fail("Not yet implemented");
	}
	
	private static Division getDivision(String name) {
		Division div = new Division();
		
		div.setAdress("adress_" + name);
		div.setName(name);
		div.setPhone("phone_" + name);
		return div;
	}

}
