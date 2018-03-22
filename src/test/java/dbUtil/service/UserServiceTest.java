package dbUtil.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;

class UserServiceTest {
	
	private static SessionFactory sessionFactory = null;
	private static UserDAO userService = null;
	private static DivisionDAO divisionService = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		userService = new UserService(sessionFactory);
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
	@DisplayName("Adding the user in the repository")
	void testAdd() throws DBException {
		String userPefix = "Add_";
		String divisionPrefix = "UserServiceAdd_";
		
		//Testing adding null. The method must throw IllegalArgumentException
		User nullUser = null;
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.add(nullUser));
		assertEquals("The user is null", exception.getMessage());
		
		//Testing adding the user with wrong parameters
		User userWithLoginNull = createExampleUser(null, Role.ADMIN);
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(userWithLoginNull));
		assertEquals("The user" + userWithLoginNull + " already exists or some its fields are wrong!", 
				exception.getMessage());
		User userWithPasswordNull = createExampleUser("User_password_null", Role.ADMIN);
		userWithPasswordNull.setPassword(null);
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(userWithPasswordNull));
		assertEquals("The user" + userWithPasswordNull + " already exists or some its fields are wrong!", 
				exception.getMessage());
		User userWithRollNull = createExampleUser("User_role_null", null);
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(userWithRollNull));
		assertEquals("The user" + userWithRollNull + " already exists or some its fields are wrong!", 
				exception.getMessage());
		
		//Testing adding the user with a division that doesn't exist in the repository
		//The method must throw IllegalArgumentException
		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		User userWithNotexistingDivision = createExampleUser("User_Division_not_exist", Role.USER);
		userWithNotexistingDivision.addDivision(oneDivision);
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(userWithNotexistingDivision));
		assertEquals("The user" + userWithNotexistingDivision + " already exists or some its fields are wrong!", 
				exception.getMessage());
		
		//Testing adding the user that doesn't exist in the repository and all fields is correct
		oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		assumeTrue(divisionService.add(oneDivision));
		User oneUser = createExampleUser(userPefix + 1, Role.ADMIN);
		oneUser.addDivision(oneDivision);
		assertTrue(userService.add(oneUser));
		assertTrue(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		
		//Testing adding the user when it already exists in the repository. Method must throw IllegalArgumentException
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(oneUser));
		assertEquals("The user" + oneUser + " already exists or some its fields are wrong!", 
				exception.getMessage());
		
		//Testing adding the user with the login that already exists in the repository.
		//Method must throw IllegalArgumentException
		User userWithSameLogin = createExampleUser("User_same_login", Role.ADMIN);
		userWithSameLogin.setLogin(oneUser.getLogin());
		exception = assertThrows(IllegalArgumentException.class, ()->userService.add(userWithSameLogin));
		assertEquals("The user" + userWithSameLogin + " already exists or some its fields are wrong!", 
				exception.getMessage());
		
	}

	@Test
	void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByLogin() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllLogins() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByRole() {
		fail("Not yet implemented");
	}

	@Test
	void testGetByDivision() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteByLogin() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testAddDivision() {
		fail("Not yet implemented");
	}
	
	
	@SuppressWarnings("unchecked")
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
	
	public static User createExampleUser(String login, Role role) {
		User user = new User(login, login + "_password", role);
		user.setEmail(login + "@mail.com");
		user.setFirstName(login + "_name");
		user.setLastName(login + "_lastName");
		user.setPhone(login + "_phone");
		user.setTitle(login + "_title");
		
		return user;
	}

}
