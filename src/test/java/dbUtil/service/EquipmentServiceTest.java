package dbUtil.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Collections;

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
import org.opentest4j.TestAbortedException;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Division_;
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
	@DisplayName("Adding the equipment")
	void testAdd() throws TestAbortedException, DBException {
	
		// Testing adding null. The method must throw IllegalArgumentException
		Equipment nullEquipment = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> equipmentService.add(nullEquipment));
		assertEquals("The eqipment is null", exception.getMessage());

		// Testing adding the equipment with the division=null.
		// The method must throw IllegalArgumentException
		Equipment oneEquipment = createExampleEquipment("Add_" + 1, null);
		exception = assertThrows(IllegalArgumentException.class, () -> equipmentService.add(oneEquipment));
		assertEquals("The eqipment " + oneEquipment + " doesn't contains division", exception.getMessage());
		
		//Testing adding the equipment with the division which doesn't exist in the repository
		Division oneDivision = DivisionServiceTest.createExampleDivision("EquipmentServiceAdd_" + 1);
		oneEquipment.setDivision(oneDivision);
		exception = assertThrows(IllegalArgumentException.class, () -> equipmentService.add(oneEquipment));
		assertEquals("The equipment " + oneEquipment + " already exists or some its fields are wrong", exception.getMessage());
		
		//Testing adding the equipment with correct fields and which doesn't exists in the repository
		assumeTrue(divisionService.add(oneDivision));
		assertTrue(equipmentService.add(oneEquipment));
		
	}

	@Test
	@DisplayName("Getting the equipment by id")
	void testGetById() throws DBException {
		
		//Testing getting the equipment by id that doesn't exist
		assertNull(equipmentService.getById(0));
		
		//Testing getting the equipment
		Division oneDivision = DivisionServiceTest.createExampleDivision("EquipmentServiceGetById_" + 1);
		Equipment oneEquipment = createExampleEquipment("GetById_" + 1, oneDivision);
		assumeTrue(divisionService.add(oneDivision));
		assertEquals(oneEquipment, equipmentService.getById(oneEquipment.getId()));

	}

	@Test
	@DisplayName("Deleting the equipment by the entity")
	void testDelete() throws DBException {
		
		//Testing deleting null
		Equipment nullEquipment = null;
		assertFalse(equipmentService.delete(nullEquipment));
		
		//Testing deleting the equipment which doesn't exist in repository
		Division oneDivision = DivisionServiceTest.createExampleDivision("EquipmentServiceDelete_" + 1);
		Equipment oneEquipment = createExampleEquipment("Delete_" + 1, oneDivision);
		assertFalse(equipmentService.delete(oneEquipment));
		
		//Testing deleting the equipment which exist in the repository
		assumeTrue(divisionService.add(oneDivision));
		assumeTrue(equipmentService.add(oneEquipment));
		assertTrue(equipmentService.delete(oneEquipment));
		assertNull(equipmentService.getById(oneEquipment.getId()));
		
	}

	@Test
	@DisplayName("Deleting the equipment by id")
	void testDeleteById() throws DBException {
		
		//Testing deleting the division by id which doesn't exist
		assertFalse(equipmentService.deleteById(0));
		
		//Testing deleting the equipment by id in the case when the repository has the equipment with such id
		Division oneDivision = DivisionServiceTest.createExampleDivision("EquipmentServiceDeleteById_" + 1);
		Equipment oneEquipment = createExampleEquipment("DeleteById", oneDivision);
		assumeTrue(divisionService.add(oneDivision));
		assumeTrue(equipmentService.add(oneEquipment));
		assertTrue(equipmentService.deleteById(oneEquipment.getId()));
		assertNull(equipmentService.getById(oneEquipment.getId()));
		
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
	@DisplayName("Get the list of all equipment by name")
	void testGetAllByName() throws DBException {
		
		//Testing getting the list of equipments by name null
		String nullName = null;
		assertEquals(Collections.emptyList(), equipmentService.getAllByName(nullName ));
		
	}
	
	@SuppressWarnings("unchecked")
	private static void deleteAllEquipments() {
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
