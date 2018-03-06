package dbUtil.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;

class EquipmentServiceTest {

	private static SessionFactory sessionFactory = null;
	private static EquipmentDAO equipmentService = null;
	private static DivisionDAO divisionService = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		equipmentService = new EquipmentService(sessionFactory);
		divisionService = new DivisionService(sessionFactory);
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
	@DisplayName("Adding the equipment in the repository")
	void testAdd() throws DBException {
		
		// Testing adding null. The method must throw IllegalArgumenException
		Equipment nullEquip = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> equipmentService.add(nullEquip));
		assertEquals("The equipment is null", exception.getMessage());
		
		//Testing adding the equipment, with field division=null
		Equipment oneEquip = createExampleEquipment("Add_" + 1, null);
		exception = assertThrows(IllegalArgumentException.class, () -> equipmentService.add(oneEquip));
		assertEquals("The division in the equipment " + oneEquip + " is null", exception.getMessage());
		
		//Testing adding the division which doesn't exist in the repository
		Division oneDiv = DivisionServiceTest.createExampleDivision("EquipServiceAdd_" + 1);
		oneEquip.setDivision(oneDiv);
		assumeTrue(divisionService.add(oneDiv));
		assertTrue(equipmentService.add(oneEquip));

	
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
	
	@SuppressWarnings("unchecked")
	private static void deleteAllEquipment() {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Query<Integer> query = session.createQuery("DELETE FROM Equipment");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static Equipment createExampleEquipment(String name, Division div) {
		Equipment equip = new Equipment();
		equip.setName(name);
		equip.setIp(name + "_ip");
		equip.setDivision(div);
		return equip;
	}

}
