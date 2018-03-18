package dbUtil.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dao.EquipmentDAO;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Equipment;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;


public class DivisionServiceTest {

	private static SessionFactory sessionFactory = null;
	private static DivisionDAO divisionService = null;
	private static EquipmentDAO equipmentService = null;
	private static UserDAO userService = null;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		divisionService = new DivisionService(sessionFactory);
		equipmentService = new EquipmentService(sessionFactory);
		userService = new UserService(sessionFactory);
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		if (sessionFactory.isOpen()) {
			sessionFactory.close();
		}
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	@DisplayName("Adding the devision in the repository")
	public void testAdd() throws DBException {
		String divisionPrefix = "Add_";
		String equipmentPrefix = "DivisionServiceAdd_";
		
		// Testing adding null. The method must throw IllegalArgumenException
		Division nullDivision = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(nullDivision));
		assertEquals("The division is null", exception.getMessage());

		//Testing adding the division with an equipment with wrong parameter
		Division wrongDivision = createExampleDivision(divisionPrefix + "wrong");
		Equipment oneEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, wrongDivision);
		Equipment wrongParameterEquipment = EquipmentServiceTest.createExampleEquipment("Wrong parametr equipment", wrongDivision);
		wrongParameterEquipment.setName(null);
		exception = assertThrows(IllegalArgumentException.class, ()->divisionService.add(wrongDivision));
		assertEquals("The division " + wrongDivision + " already exists or some its fields are wrong", exception.getMessage());
		
		// Testing adding the devision when it doesn't exist in the repository
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		Equipment twoEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		Equipment threeEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		assertTrue(divisionService.add(oneDivision));
		assertEquals(threeEquip, equipmentService.getById(threeEquip.getId()));
		assertEquals(twoEquip, equipmentService.getById(twoEquip.getId()));

		// Testing adding the division when it already exists in the repository. The method
		// must throw IllegalArgumentException
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(oneDivision));
		assertEquals("The division " + oneDivision + " already exists or some its fields are wrong", exception.getMessage());

		// Testing adding the division when it doesn't exist in the repository, but its fields
		// are wrong. The method must throw IllegalArgumentException
		Division two = createExampleDivision(divisionPrefix + 2);
		two.setName(null);
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(two));
		assertEquals("The division " + two + " already exists or some its fields are wrong", exception.getMessage());

		//Testing adding the division with name which already exists in the repository
		two.setName(oneDivision.getName());
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(two));
		assertEquals("The division " + two + " already exists or some its fields are wrong", exception.getMessage());
		
	}

	@Test
	@DisplayName("Getting the division by id")
	public void testGetById() throws DBException {
		String devisionPrefix = "GetById_";
		
		//Testing getting the division by id, that doesn't exist
		assertNull(divisionService.getById(0));
		
		//Testing getting the division
		Division one = createExampleDivision(devisionPrefix + 1);
		assumeTrue(divisionService.add(one));
		assertEquals(one, divisionService.getById(one.getId()));
		
	}

	@Test
	@DisplayName("Deleting the division by the entity")
	public void testDelete() throws DBException {
		String divisionPrefix = "Delete_";
		String equipmentPrefix = "DivisionServiceDelete_";
		String userPrefix = "DivisionServiceDelete_";
		
		//Test deleting null
		Division nullDivision = null;
		assertFalse(divisionService.delete(nullDivision));
		
		// Testing deleting the division which doesn't exist in the repository
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		assertFalse(divisionService.delete(oneDivision));
		
		//Testing deleting the division which exists in the repository
		Equipment oneEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		Equipment twoEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 2, oneDivision);
		Equipment threeEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 3, oneDivision);
		User oneUser = UserServiceTest.createExampleUser(userPrefix + 1, Role.ADMIN);
		oneUser.addDivision(oneDivision);
		User twoUser = UserServiceTest.createExampleUser(userPrefix + 2, Role.USER);
		twoUser.addDivision(oneDivision);
		assumeTrue(divisionService.add(oneDivision));
		assumeTrue(userService.add(oneUser));
		assumeTrue(userService.add(twoUser));
		assertTrue(divisionService.delete(oneDivision));
		assertNull(divisionService.getById(oneDivision.getId()));
		assertNull(equipmentService.getById(oneEquip.getId()));
		assertNull(equipmentService.getById(twoEquip.getId()));
		assertNull(equipmentService.getById(threeEquip.getId()));
		assertEquals(0, userService.getById(oneUser.getId()).getDivisions().size());
		assertEquals(0, userService.getById(twoUser.getId()).getDivisions().size());
		
	}

	@Test
	@DisplayName("Deleting the division by id")
	public void testDeleteById() throws DBException {
		String divisionPrefix = "DeletByID_";
		String userPrefix = "DivisionServiceDeleteById_";
		String equipmentPrefix = "DivisionServiceDeleteByID_";
		
		//Testing deleting the division by id when it doesn't exist
		assertFalse(divisionService.deleteById(0));
		
		//Testing deleting the division when the repository has a division with specific id
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		Equipment oneEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		Equipment twoEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 2, oneDivision);
		Equipment threeEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 3, oneDivision);
		User oneUser = UserServiceTest.createExampleUser(userPrefix + 1, Role.ADMIN);
		oneUser.addDivision(oneDivision);
		User twoUser = UserServiceTest.createExampleUser(userPrefix + 2, Role.USER);
		twoUser.addDivision(oneDivision);
		assumeTrue(divisionService.add(oneDivision));
		assumeTrue(userService.add(oneUser));
		assumeTrue(userService.add(twoUser));
		assertTrue(divisionService.deleteById(oneDivision.getId()));
		assertNull(divisionService.getById(oneDivision.getId()));
		assertNull(equipmentService.getById(oneEquip.getId()));
		assertNull(equipmentService.getById(twoEquip.getId()));
		assertNull(equipmentService.getById(threeEquip.getId()));
		assertEquals(0, userService.getById(oneUser.getId()).getDivisions().size());
		assertEquals(0, userService.getById(twoUser.getId()).getDivisions().size());
		
	}

	@Test
	@DisplayName("Updating the division")
	public void testUpdate() throws DBException {
		String divisionPrefix = "Update_";
		String userPrefix = "DivisionServiceUpdate_";
		String equipmentPrefix = "DivisionServiceUpdate_";
		
		//Testing updating null. The method must throw DBException
		Division nullDivision = null;
		Throwable exception = assertThrows(DBException.class, () -> divisionService.update(nullDivision));
		assertEquals("Cannot update the division " + nullDivision, exception.getMessage());
		
		//Testing updating the division which doesn't exist in the repository. The method must throw DBException
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		exception = assertThrows(DBException.class, () -> divisionService.update(oneDivision));
		assertEquals("Cannot update the division " + oneDivision, exception.getMessage());
		
		//Testing updating the division name
		assumeTrue(divisionService.add(oneDivision));
		oneDivision.setName("UpdatedName_" + oneDivision.getName());
		assertTrue(divisionService.update(oneDivision));
		assertEquals(oneDivision, divisionService.getById(oneDivision.getId()));
		
		//Testing updating the division (add the equipment)
		Equipment oneEquipment = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		assertTrue(divisionService.update(oneDivision));
		assertTrue(equipmentService.getByDivision(oneDivision.getName()).contains(oneEquipment));
		
		//Testing updating the division (delete the equipment)
		oneDivision.getEquipment().remove(oneEquipment);
		assertTrue(divisionService.update(oneDivision));
		assertTrue(!equipmentService.getByDivision(oneDivision.getName()).contains(oneEquipment));
		
		//Testing updating the division (add user)
		User oneUser = UserServiceTest.createExampleUser(userPrefix + 1, Role.USER);
		assumeTrue(userService.add(oneUser));
		oneDivision.addUser(oneUser);
		//TODO end the test
		assertTrue(divisionService.update(oneDivision));
		assertTrue(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		
		
		//Testing updating the division with the name, which already exist in the repository
		Division twoDivision = createExampleDivision(divisionPrefix + 2);
		assumeTrue(divisionService.add(twoDivision));
		twoDivision.setName(oneDivision.getName());
		exception = assertThrows(DBException.class, () -> divisionService.update(twoDivision));
		assertEquals("Cannot update the division " + twoDivision, exception.getMessage());
		
		//Testing updating the division with wrong fields
		oneDivision.setName(null);
		exception = assertThrows(DBException.class, () -> divisionService.update(oneDivision));
		assertEquals("Cannot update the division " + oneDivision, exception.getMessage());
	
	}

	@Test
	@DisplayName("Getting the set of all division names")
	public void testGetAllNames() throws DBException {
		
		//Preparing the repository
		try {
			deleteAllDivision();
		} catch (Exception e) {
			assumeNoException("Cannot delete all devision befor testing method getAllNames", e);
		}
		
		//Testing getting the set of division names when repository is empty
		assertEquals(Collections.emptySet(), divisionService.getAllNames());
		
		
		//Filling the repository with test data
		String prefixName = "GetAllNames_";
		Set<String> exceptedSetOfNames = new HashSet<>();
		for(int i = 0; i < 10; i++) {
			Division division = createExampleDivision(prefixName + i);
			exceptedSetOfNames.add(division.getName());
			assumeTrue(divisionService.add(division));
		}
		
		//Testing getting the set of all division names
		assertEquals(exceptedSetOfNames, divisionService.getAllNames());
		
	}

	
	@Test
	@DisplayName("Getting the division by name")
	public void testGetByName() throws DBException {
		
		//Testing getting by null
		String nullName = null;
		assertNull(divisionService.getByName(nullName));
		
		//Testing getting the division
		Division one = createExampleDivision("GetByName_" + 1);
		assumeTrue(divisionService.add(one));
		assertEquals(one, divisionService.getByName(one.getName()));
		
		//Testing getting division by wrong name
		String wrongName = "WrongName";
		assertNull(divisionService.getByName(wrongName));
		
	}

	@Test
	@DisplayName("Deleting the division by name")
	public void testDeleteByName() throws DBException {
		
		//Testing deleting null
		assertFalse(divisionService.deleteByName(null));
		
		//Testing deleting by wrong name
		String wrongName = "WrongName";
		assertFalse(divisionService.deleteByName(wrongName));
		
		// TODO add in the division users, equipments and save divisions
		Division one = createExampleDivision("DeleteByName_" + 1);
		
	}
	
	@SuppressWarnings("unchecked")
	private static void deleteAllDivision() {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.createQuery("DELETE FROM Equipment").executeUpdate();
			session.createQuery("DELETE FROM Division").executeUpdate();
			// TODO create one statement that deletes all divisions with equipments 
			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	static Division createExampleDivision(String name) {
		String adress = name + "_adress";
		Division div = new Division(name, adress);
		div.setName(name);
		div.setPhone(name + "_phone");
		
		return div;
	}

}
