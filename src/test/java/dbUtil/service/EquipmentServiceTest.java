package dbUtil.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;

class EquipmentServiceTest {

	private static SessionFactory sessionFactory = null;
	private static EquipmentDAO equipmentService = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		equipmentService = new EquipmentService(sessionFactory);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		if (sessionFactory.isOpen()) {
			sessionFactory.close();
		}
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
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
	void testGetByNameAndDivision() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByDivision() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllByName() {
		fail("Not yet implemented");
	}
	
	private static Equipment createExampleEquipment(String name, Division div) {
		Equipment equip = new Equipment();
		equip.setName(name);
		equip.setIp(name + "_ip");
		equip.setDivision(div);
		return equip;
	}

}
