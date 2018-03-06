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
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dbUtil.DBException;
import dbUtil.dao.DivisionDAO;
import dbUtil.dataSets.Division;


public class DivisionServiceTest {

	private static SessionFactory sessionFactory = null;
	private static DivisionDAO divisionService = null;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		divisionService = new DivisionService(sessionFactory);
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		if (sessionFactory.isOpen()) {
			sessionFactory.close();
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Adding the devision in the repository")
	public void testAdd() throws DBException {
		
		// Testing adding null. The method must throw IllegalArgumenException
		Division nullDivision = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(nullDivision));
		assertEquals("The division is null", exception.getMessage());

		Division one = createExampleDivision("Add_" + 1);

		// Testing adding the devision when it doesn't exist in the repository
		assertTrue(divisionService.add(one));
		

		// Testing adding the division when it already exists in the repository. The method
		// must throw IllegalArgumentException
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(one));
		assertEquals("The division " + one + " already exists or some its fields are wrong", exception.getMessage());

		// Testing adding the division when it doesn't exist in the repository, but its fields
		// are wrong. The method must throw IllegalArgumentException
		Division two = createExampleDivision("Add_" + 2);
		two.setName(null);
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(two));
		assertEquals("The division " + two + " already exists or some its fields are wrong", exception.getMessage());

		//Testing adding the division with name which already exsits in repository
		two.setName(one.getName());
		exception = assertThrows(IllegalArgumentException.class, () -> divisionService.add(two));
		assertEquals("The division " + two + " already exists or some its fields are wrong", exception.getMessage());
		
	}

	@Test
	@DisplayName("Getting the division by id")
	public void testGetById() throws DBException {
		
		//Testing getting the division by id, that doesn't exist
		assertNull(divisionService.getById(0));
		
		//Testing getting the division
		Division one = createExampleDivision("GetByID_" + 1);
		assumeTrue(divisionService.add(one));
		assertEquals(one, divisionService.getById(one.getId()));
		
	}

	@Test
	@DisplayName("Deleting the division by the entity")
	public void testDelete() throws DBException {
		
		//Test deleting null
		Division nullDivision = null;
		assertFalse(divisionService.delete(nullDivision));
		
		// TODO add in the division users, equipments and save divisions
		//Testing deleting the division which exists in the repository
		Division one = createExampleDivision("Delete_" + 1);
		assumeTrue(divisionService.add(one));
		assertTrue(divisionService.delete(one));
		assertNull(divisionService.getById(one.getId()));
		
		//Testing deleting the division which doesn't exist in the repository
		Division two = createExampleDivision("Delete_" + 2);
		assertFalse(divisionService.delete(two));
		
	}

	@Test
	@DisplayName("Deleting the division by id")
	public void testDeleteById() throws DBException {
		
		//Testing deleting the division by id when it doesn't exist
		assertFalse(divisionService.deleteById(0));
		
		// TODO add in the division users, equipments and save divisions
		//Testing deleting the division when the repository has a division with specific id
		Division one = createExampleDivision("DeletByID_" + 1);
		assumeTrue(divisionService.add(one));
		assertTrue(divisionService.deleteById(one.getId()));
		
	}

	@Test
	@DisplayName("Updating the division")
	public void testUpdate() throws DBException {
		
		//Testing updating null. The method must throw DBException
		Division nullDivision = null;
		Throwable exception = assertThrows(DBException.class, () -> divisionService.update(nullDivision));
		assertEquals("Cannot update the division " + nullDivision, exception.getMessage());
		
		//Testing updating the division which doesn't exist in the repository.  The method must throw DBException
		Division one = createExampleDivision("Update_" + 1);
		exception = assertThrows(DBException.class, () -> divisionService.update(one));
		assertEquals("Cannot update the division " + one, exception.getMessage());
		
		//Testing updating the division
		assumeTrue(divisionService.add(one));
		one.setName("UpdatedName_" + one.getName());
		assertTrue(divisionService.update(one));
		
		//Testing updating the division with the name, which already exist in the repository
		Division two = createExampleDivision("Update_" + 2);
		assumeTrue(divisionService.add(two));
		two.setName(one.getName());
		exception = assertThrows(DBException.class, () -> divisionService.update(two));
		assertEquals("Cannot update the division " + two, exception.getMessage());
		
		
		//Testing updating the division with wrong parameters
		one.setName(null);
		exception = assertThrows(DBException.class, () -> divisionService.update(one));
		assertEquals("Cannot update the division " + one, exception.getMessage());
	
	}

	@Test
	@DisplayName("Getting the set of all division names")
	public void testGetAllNames() throws DBException {
		
		//Preparing the repository
		try {
			deleteAllDivision();
		} catch (Exception e) {
			assumeNoException("Cannot delete all devision after testing method getAllNames", e);
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
			Query<Integer> query = session.createQuery("DELETE FROM Division");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Division createExampleDivision(String name) {
		Division div = new Division();
		
		div.setAdress(name + "_adress");
		div.setName(name);
		div.setPhone(name + "_phone");
		
		return div;
	}

}
