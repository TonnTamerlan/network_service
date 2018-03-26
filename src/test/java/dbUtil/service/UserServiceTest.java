package dbUtil.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
	@DisplayName("Getting a user by id")
	void testGetById() throws DBException {
		String userPrefix = "GetById_";
		String divisionPrefix = "UserServiceGetById_";
		
		//Testing getting by id which doesn't exist
		Long wrongId = 0L;
		assertNull(userService.getById(wrongId));
		
		//Testing getting the user that exists in the repository 
		User oneUser = createExampleUser(userPrefix + 1, Role.ADMIN);
		assumeTrue(userService.add(oneUser));
		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		oneDivision.addUser(oneUser);
		assumeTrue(divisionService.add(oneDivision));
		assertEquals(oneUser, userService.getById(oneUser.getId()));
		assertTrue(oneUser.getDivisions().contains(oneDivision));
	}

	@Test
	@DisplayName("Getting the user by login")
	void testGetByLogin() throws DBException {
		String userPrefix = "GetByLogin_";
		String divisionPrefix = "UserServiceGetByLogin_";
		
		//Testing getting the user by login null
		String nullName = null;
		assertNull(userService.getByLogin(nullName));
		
		//Testing getting the user by login that doesn't exist
		String nameNotExist = "Not_exist";
		assertNull(userService.getByLogin(nameNotExist));
		
		//Testing getting the user that exists in the repository
		User oneUser = createExampleUser(userPrefix + 1, Role.ADMIN);
		assumeTrue(userService.add(oneUser));
		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		oneDivision.addUser(oneUser);
		assumeTrue(divisionService.add(oneDivision));
		assertEquals(oneUser, userService.getById(oneUser.getId()));
		assertTrue(oneUser.getDivisions().contains(oneDivision));
		
	}

	@Test
	@DisplayName("Getting all user logins")
	void testGetAllLogins() throws DBException {
		String userPrefix = "GetAllLogins_";
		int numberOfUsers = 20;
		Set<String> setOfUserLogins = new HashSet<>();
		
		//Prepare the repository
		clearRepository();
		
		//Testing getting the set of all user logins if the repository doesn't contain any users
		Set<String> emptySet = Collections.emptySet();
		assertEquals(emptySet, userService.getAllLogins());
		
		//Testing getting set of user logins
		try {
			for (int i = 0; i < numberOfUsers; i++) {
				User user = createExampleUser(userPrefix + i, Role.ADMIN);
				setOfUserLogins.add(user.getLogin());
				assumeTrue(userService.add(user));
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
		assertTrue(setOfUserLogins.equals(userService.getAllLogins()));
	}

	@Test
	@DisplayName("Getting set of user logins by role")
	void testGetByRole() throws DBException {
		String userPrefix = "GetAllByRole_";
		Role admin = Role.ADMIN;
		Role user = Role.USER;
		int numberOfUser = 20;
		Set<String> setOfUserLoginsUser = new HashSet<>();
		Set<String> setOfUserLoginsAdmin = new HashSet<>();
		
		//Prepare the repository
		clearRepository();
		
		//Testing getting the set of user logins when users with this role don't exist
		Set<String> emptySet = Collections.emptySet();
		assertEquals(emptySet, userService.getByRole(user));
		assertEquals(emptySet, userService.getByRole(admin));
		
		//Testing getting the set of user logins by role
		try {
			for (int i = 0; i < numberOfUser; i++) {
				if (i % 2 == 0) {
					User userAdmin = createExampleUser(userPrefix + i, admin);
					setOfUserLoginsAdmin.add(userAdmin.getLogin());
					assumeTrue(userService.add(userAdmin));
				} else {
					User userUser = createExampleUser(userPrefix + i, user);
					setOfUserLoginsUser.add(userUser.getLogin());
					assumeTrue(userService.add(userUser));
				}
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
		assertTrue(setOfUserLoginsAdmin.equals(userService.getByRole(admin)));
		assertTrue(setOfUserLoginsUser.equals(userService.getByRole(user)));
		
	}

	@Test
	@DisplayName("Getting the set of user logins by division name")
	void testGetByDivision() throws DBException {
		String userPrefix = "GetByDivision_";
		String divisionPrefix = "UserServiceGetByDivision_";
		Set<String> emtySetOfUserLogins = Collections.emptySet();
		
		//prepare the repository
		clearRepository();
		
		//Testing getting by null
		String nameNull = null;
		assertEquals(emtySetOfUserLogins, userService.getByDivision(nameNull));
		
		//Testing getting the set of user logins by division name, 
		//that doesn't exist in the repository
		String wrongDivisionName = "Wrong_division_name";
		assertEquals(emtySetOfUserLogins, userService.getByDivision(wrongDivisionName));

		//Testing getting the set of user logins
		Set<String> oneSet = new HashSet<>();
		Set<String> twoSet = new HashSet<>();
		Set<String> threeSet = new HashSet<>();
		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		assumeTrue(divisionService.add(oneDivision));
		Division twoDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 2);
		assumeTrue(divisionService.add(twoDivision));
		Division threeDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 3);
		assumeTrue(divisionService.add(threeDivision));
		try {
			for (int i = 0; i < 20; i++) {
				User user = createExampleUser(userPrefix + i, Role.ADMIN);
				if(i % 2 == 0) {
					user.addDivision(oneDivision);
					oneSet.add(user.getLogin());
					assumeTrue(userService.add(user));
				} else {
					user.addDivision(twoDivision);
					twoSet.add(user.getLogin());
					assumeTrue(userService.add(user));
				}
				threeDivision.addUser(user);
				threeSet.add(user.getLogin());
				assumeTrue(divisionService.update(threeDivision));
			}
		} catch(Exception e) {
			assumeNoException(e);
		}
		assertEquals(oneSet, userService.getByDivision(oneDivision.getName()));
		assertEquals(twoSet, userService.getByDivision(twoDivision.getName()));
		assertEquals(threeSet, userService.getByDivision(threeDivision.getName()));
		
	}

	@Test
	@DisplayName("Deleting by Id")
	void testDeleteById() throws DBException {
		String userPrefix = "DeleteById_";
		String divisionPrefix = "UserServiceDeleteById_";
		
		//Preparing the repository
		clearRepository();
		
		//Testing deleting by id which doesn't exist
		int notExistId = 42;
		assertFalse(userService.deleteById(notExistId));
		
		//Testing deleting by id
		User oneUser = createExampleUser(userPrefix + 1, Role.ADMIN);
		assumeTrue(userService.add(oneUser));
		User twoUser = createExampleUser(userPrefix + 2, Role.USER);
		assumeTrue(userService.add(twoUser));
		User threeUser = createExampleUser(userPrefix + 3, Role.ADMIN);
		assumeTrue(userService.add(threeUser));
		
		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		oneDivision.addUser(oneUser);
		oneDivision.addUser(threeUser);
		assumeTrue(divisionService.add(oneDivision));
		Division twoDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 2);
		twoDivision.addUser(twoUser);
		twoDivision.addUser(threeUser);
		assumeTrue(divisionService.add(twoDivision));
		
		assertTrue(userService.deleteById(oneUser.getId()));
		assertNull(userService.getById(oneUser.getId()));
		assertFalse(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		assertTrue(userService.deleteById(twoUser.getId()));
		assertNull(userService.getById(twoUser.getId()));
		assertFalse(divisionService.getById(twoDivision.getId()).getUsers().contains(twoUser));
		assertTrue(userService.deleteById(threeUser.getId()));
		assertNull(userService.getById(threeUser.getId()));
		assertFalse(divisionService.getById(oneDivision.getId()).getUsers().contains(threeUser));
		assertFalse(divisionService.getById(twoDivision.getId()).getUsers().contains(threeUser));
		
	}

	@Test
	@DisplayName("Deleting by login")
	void testDeleteByLogin() throws DBException {
		String userPrefix = "DeleteByLogin_";
		String divisionPrefix = "UserServiceDeleteByLogin_";
	
		//Preparing the repository
		clearRepository();
		
		//Testing deleting the user by login that doesn't exist in the repository
		String notExistLogin = "Not_exist";
		assertFalse(userService.deleteByLogin(notExistLogin));
		
		//Testing deleting user by login
		//Testing deleting by id
		User oneUser = createExampleUser(userPrefix + 1, Role.ADMIN);
		assumeTrue(userService.add(oneUser));
		User twoUser = createExampleUser(userPrefix + 2, Role.USER);
		assumeTrue(userService.add(twoUser));
		User threeUser = createExampleUser(userPrefix + 3, Role.ADMIN);
		assumeTrue(userService.add(threeUser));

		Division oneDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 1);
		oneDivision.addUser(oneUser);
		oneDivision.addUser(threeUser);
		assumeTrue(divisionService.add(oneDivision));
		Division twoDivision = DivisionServiceTest.createExampleDivision(divisionPrefix + 2);
		twoDivision.addUser(twoUser);
		twoDivision.addUser(threeUser);
		assumeTrue(divisionService.add(twoDivision));

		assertTrue(userService.deleteByLogin(oneUser.getLogin()));
		assertNull(userService.getById(oneUser.getId()));
		assertFalse(divisionService.getById(oneDivision.getId()).getUsers().contains(oneUser));
		assertTrue(userService.deleteByLogin(twoUser.getLogin()));
		assertNull(userService.getById(twoUser.getId()));
		assertFalse(divisionService.getById(twoDivision.getId()).getUsers().contains(twoUser));
		assertTrue(userService.deleteByLogin(threeUser.getLogin()));
		assertNull(userService.getById(threeUser.getId()));
		assertFalse(divisionService.getById(oneDivision.getId()).getUsers().contains(threeUser));
		assertFalse(divisionService.getById(twoDivision.getId()).getUsers().contains(threeUser));

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
