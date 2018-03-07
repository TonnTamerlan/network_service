package dbUtil.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@DisplayName("Updating the equipment")
	void testUpdate() throws TestAbortedException, DBException {
		String prefixDivisionName = "EquipmentServiceUpdate_";
		String prefixEquipmentName = "Update_";
		
		
		//Testing updating null. Method must throw DBException
		Equipment nullEquipment = null;
		Throwable exception = assertThrows(DBException.class, () -> equipmentService.update(nullEquipment));
		assertEquals("Cannot update the equipment " + nullEquipment, exception.getMessage());
		
		Division oneDivision = DivisionServiceTest.createExampleDivision(prefixDivisionName + 1);
		assumeTrue(divisionService.add(oneDivision));
		
		//Testing updating the equipment which doesn't exists in the repository
		//Method must throw DBException
		Equipment oneEquipment = createExampleEquipment(prefixEquipmentName + 1, oneDivision);
		exception = assertThrows(DBException.class, () -> equipmentService.update(oneEquipment));
		assertEquals("Cannot update the equipment " + oneEquipment, exception.getMessage());
		
		// Testing updating the equipment with wrong fields. The method must throw DBException

		//The field devision is null
		assumeTrue(equipmentService.add(oneEquipment));
		oneEquipment.setDivision(null);
		exception = assertThrows(DBException.class, () -> equipmentService.update(oneEquipment));
		assertEquals("Cannot update the equipment " + oneEquipment, exception.getMessage());
		
		//The field devision does not exist in the database
		Division twoDivision = DivisionServiceTest.createExampleDivision(prefixDivisionName + 2);
		oneEquipment.setDivision(twoDivision);
		exception = assertThrows(DBException.class, () -> equipmentService.update(oneEquipment));
		assertEquals("Cannot update the equipment " + oneEquipment, exception.getMessage());
		
		//Testing updating the division in the case when everything is correct
		assumeTrue(divisionService.add(twoDivision));
		assertTrue(equipmentService.update(oneEquipment));
		assertEquals(oneEquipment, equipmentService.getById(oneEquipment.getId()));
		
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
		assertNull(equipmentService.getAllByName(nullName ));
		
		//Testing getting the list of equipment by wrong name
		String wrongName = "Wrong name";
		assertNull(equipmentService.getAllByName(wrongName));
		
		//Testing getting the list of equipment when everything is correct
		Division oneDiv = DivisionServiceTest.createExampleDivision("EquipmentServiceGetAllByName_" + 1);
		Division twoDiv = DivisionServiceTest.createExampleDivision("EquipmentServiceGetAllByName_" + 2);
		Division threeDiv = DivisionServiceTest.createExampleDivision("EquipmentServiceGetAllByName_" + 3);
		assumeTrue(divisionService.add(oneDiv));
		assumeTrue(divisionService.add(twoDiv));
		assumeTrue(divisionService.add(threeDiv));
		int countEquip = 100;
		Equipment equip = null;
		String nameEquipOne = "GetAllByName_1";
		String nameEquipTwo = "GetAllByName_2";
		String nameEquipThree = "GetAllByName_3";
		String nameEquipFour = "GetAllByName_4";
		String nameEquipFive = "GetAllByName_5";
		Map<String, List<Equipment>> mapOfEquip = new HashMap<>();
		
		// Filling the repository
		try {
			for (int i = 0; i < countEquip; i++) {
				if (i % 2 == 0) {
					equip = createExampleEquipment(nameEquipOne, oneDiv);
					equipmentService.add(equip);
					if (mapOfEquip.containsKey(nameEquipOne)) {
						mapOfEquip.get(nameEquipOne).add(equip);
					} else {
						mapOfEquip.put(nameEquipOne, new ArrayList<Equipment>());
						mapOfEquip.get(nameEquipOne).add(equip);
					}
				} else if (i % 3 == 0) {
					equip = createExampleEquipment(nameEquipTwo, twoDiv);
					equipmentService.add(equip);
					if (mapOfEquip.containsKey(nameEquipTwo)) {
						mapOfEquip.get(nameEquipTwo).add(equip);
					} else {
						mapOfEquip.put(nameEquipTwo, new ArrayList<Equipment>());
						mapOfEquip.get(nameEquipTwo).add(equip);
					}
				} else if (i % 5 == 0) {
					equip = createExampleEquipment(nameEquipThree, twoDiv);
					equipmentService.add(equip);
					if (mapOfEquip.containsKey(nameEquipThree)) {
						mapOfEquip.get(nameEquipThree).add(equip);
					} else {
						mapOfEquip.put(nameEquipThree, new ArrayList<Equipment>());
						mapOfEquip.get(nameEquipThree).add(equip);
					}
				} else if (i % 7 == 0) {
					equip = createExampleEquipment(nameEquipFour, threeDiv);
					equipmentService.add(equip);
					if (mapOfEquip.containsKey(nameEquipFour)) {
						mapOfEquip.get(nameEquipFour).add(equip);
					} else {
						mapOfEquip.put(nameEquipFour, new ArrayList<Equipment>());
						mapOfEquip.get(nameEquipFour).add(equip);
					}
				} else {
					equip = createExampleEquipment(nameEquipFive, oneDiv);
					equipmentService.add(equip);
					if (mapOfEquip.containsKey(nameEquipFive)) {
						mapOfEquip.get(nameEquipFive).add(equip);
					} else {
						mapOfEquip.put(nameEquipFive, new ArrayList<Equipment>());
						mapOfEquip.get(nameEquipFive).add(equip);
					}
				}
			}
		} catch (Exception e) {
			assumeNoException("Canor add the equipment for testing", e);
		}
		//Executing tests
		List<Equipment> actualList = equipmentService.getAllByName(nameEquipOne);
		List<Equipment> expectedList = mapOfEquip.get(nameEquipOne);
		assertTrue(isSameLists(expectedList, actualList));
		
		actualList = equipmentService.getAllByName(nameEquipTwo);
		expectedList = mapOfEquip.get(nameEquipTwo);
		assertTrue(isSameLists(expectedList, actualList));
		
		actualList = equipmentService.getAllByName(nameEquipThree);
		expectedList = mapOfEquip.get(nameEquipThree);
		assertTrue(isSameLists(expectedList, actualList));
		
		actualList = equipmentService.getAllByName(nameEquipFour);
		expectedList = mapOfEquip.get(nameEquipFour);
		assertTrue(isSameLists(expectedList, actualList));
		
		actualList = equipmentService.getAllByName(nameEquipFive);
		expectedList = mapOfEquip.get(nameEquipFive);
		assertTrue(isSameLists(expectedList, actualList));
	}

	private <T> boolean isSameLists(List<T> expectedList, List<T> actualList) {
		if (expectedList == null && actualList == null) {
			return true;
		} else if (expectedList == null || actualList == null) {
			return false;
		} else {
			return expectedList.size() == actualList.size() && expectedList.containsAll(actualList);
		}
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
