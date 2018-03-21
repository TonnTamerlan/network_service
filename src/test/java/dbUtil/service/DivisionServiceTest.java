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
		String userPrefix = "DivisionServiceAdd_";
		
		// Testing adding null. The method must throw IllegalArgumenException
		Division nullDivision = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(nullDivision));
		assertEquals("The division is null", exception.getMessage());

		//Testing adding the division with an equipment with wrong parameter equipment
		Division divisionWithWrongEquipment = createExampleDivision(divisionPrefix + "wrong_equipment");
		Equipment oneEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, divisionWithWrongEquipment);
		Equipment wrongParameterEquipment = EquipmentServiceTest.createExampleEquipment("Wrong parametr equipment", divisionWithWrongEquipment);
		wrongParameterEquipment.setName(null);
		exception = assertThrows(IllegalArgumentException.class, ()->divisionService.add(divisionWithWrongEquipment));
		assertEquals("The division " + divisionWithWrongEquipment + " already exists or some its fields are wrong", exception.getMessage());
		
		
		//TODO testing adding the division with user that doesn't exist
		
		// Testing adding the devision when it doesn't exist in the repository
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		Equipment twoEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		Equipment threeEquip = EquipmentServiceTest.createExampleEquipment(equipmentPrefix + 1, oneDivision);
		User oneUser = UserServiceTest.createExampleUser(userPrefix + 1, Role.ADMIN);
		User twoUser = UserServiceTest.createExampleUser(userPrefix + 2, Role.USER);
		assumeTrue(userService.add(oneUser));
		assumeTrue(userService.add(twoUser));
		oneDivision.addUser(oneUser);
		oneDivision.addUser(twoUser);
		assertTrue(divisionService.add(oneDivision));
		assertEquals(threeEquip, equipmentService.getById(threeEquip.getId()));
		assertEquals(twoEquip, equipmentService.getById(twoEquip.getId()));
		assertTrue(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		assertTrue(divisionService.getById(oneDivision.getId()).getUsers().contains(twoUser));
		assertTrue(userService.getById(oneUser.getId()).getDivisions().contains(oneDivision));
		assertTrue(userService.getById(twoUser.getId()).getDivisions().contains(oneDivision));

		
		// Testing adding the division when it already exists in the repository. The method
		// must throw IllegalArgumentException
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(oneDivision));
		assertEquals("The division " + oneDivision + " already exists or some its fields are wrong", exception.getMessage());

		// Testing adding the division when it doesn't exist in the repository, but its fields
		// are wrong. The method must throw IllegalArgumentException
		String nullName = null;
		Division two = createExampleDivision(nullName);
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
		assertTrue(divisionService.update(oneDivision));
		assertTrue(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		
		//Testing updating the division (delete user)
		oneDivision.deleteUser(oneUser);
		assertTrue(divisionService.update(oneDivision));
		assertFalse(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		
		//Testing updating the division (add the user with which doesn't exist in the repository)
		User twoUser = UserServiceTest.createExampleUser(userPrefix + 2, Role.ADMIN);
		User dontExistUser = UserServiceTest.createExampleUser(userPrefix + "don't_exists", Role.USER);
		assumeTrue(userService.add(twoUser));
		oneDivision.addUser(oneUser);
		oneDivision.addUser(twoUser);
		oneDivision.addUser(dontExistUser);
		exception = assertThrows(DBException.class, ()->divisionService.update(oneDivision));
		assertEquals("Cannot update the division " + oneDivision, exception.getMessage());
		
		
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
		String divisionPrefix = "GetAllNames_";
		
		//Preparing the repository
		try {
			clearRepository();
		} catch (Exception e) {
			assumeNoException("Cannot delete all devision befor testing method getAllNames", e);
		}
		
		//Testing getting the set of division names when repository is empty
		assertEquals(Collections.emptySet(), divisionService.getAllNames());
		
		//Filling the repository with test data
		Set<String> exceptedSetOfNames = new HashSet<>();
		for(int i = 0; i < 10; i++) {
			Division division = createExampleDivision(divisionPrefix + i);
			exceptedSetOfNames.add(division.getName());
			assumeTrue(divisionService.add(division));
		}
		
		//Testing getting the set of all division names
		assertEquals(exceptedSetOfNames, divisionService.getAllNames());
		
	}

	
	@Test
	@DisplayName("Getting the division by name")
	public void testGetByName() throws DBException {
		String divisionPrefix = "GetByName_";
		
		//Testing getting by null
		String nullName = null;
		assertNull(divisionService.getByName(nullName));
		
		//Testing getting the division
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		assumeTrue(divisionService.add(oneDivision));
		assertEquals(oneDivision, divisionService.getByName(oneDivision.getName()));
		
		//Testing getting division by wrong name
		String wrongName = "WrongName";
		assertNull(divisionService.getByName(wrongName));
		
	}

	@Test
	@DisplayName("Deleting the division by name")
	public void testDeleteByName() throws DBException {
		String divisionPrefix = "DeleteByName_";
		String userPrefix = "DivisionServiceDeleteByName_";
		String equipPrefix =  "DivisionServiceDeleteByName_";
		
		//Testing deleting null
		String nullName = null;
		assertFalse(divisionService.deleteByName(nullName));
		
		//Testing deleting by wrong name
		String wrongName = "WrongName";
		assertFalse(divisionService.deleteByName(wrongName));
		
		//Testing the division with correct parameters 
		Division oneDivision = createExampleDivision(divisionPrefix + 1);
		Equipment oneEquip = EquipmentServiceTest.createExampleEquipment(equipPrefix + 1, oneDivision);
		Equipment twoEquip = EquipmentServiceTest.createExampleEquipment(equipPrefix + 2, oneDivision);
		User oneUser = UserServiceTest.createExampleUser(userPrefix + 1, Role.USER);
		User twoUser = UserServiceTest.createExampleUser(userPrefix + 2, Role.ADMIN);
		assumeTrue(userService.add(oneUser));
		assumeTrue(userService.add(twoUser));
		oneDivision.addUser(oneUser);
		oneDivision.addUser(twoUser);
		assumeTrue(divisionService.add(oneDivision));
		assumeTrue(oneEquip.equals(equipmentService.getById(oneEquip.getId())));
		assumeTrue(twoEquip.equals(equipmentService.getById(twoEquip.getId())));
		assumeTrue(userService.getById(oneUser.getId()).getDivisions().contains(oneDivision));
		assumeTrue(userService.getById(twoUser.getId()).getDivisions().contains(oneDivision));
		assertTrue(divisionService.deleteByName(oneDivision.getName()));
		assertNull(equipmentService.getById(oneEquip.getId()));
		assertNull(equipmentService.getById(twoEquip.getId()));
		assertNull(divisionService.getById(oneDivision.getId()));
		Set<Division> emptyDivisionSet = new HashSet<>();
		assertEquals(emptyDivisionSet, userService.getById(oneUser.getId()).getDivisions());
		assertEquals(emptyDivisionSet, userService.getById(twoUser.getId()).getDivisions());
		

		
	}
	
	private static void clearRepository() {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.createQuery("DELETE FROM Equipment").executeUpdate();
			session.createQuery("DELETE FROM Division").executeUpdate();
			session.createQuery("DELETE FROM User").executeUpdate();
			session.createNativeQuery("DELETE FROM user_division").executeUpdate();
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
