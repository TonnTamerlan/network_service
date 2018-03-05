package dbUtil.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;

class UserServiceTest {
	
	private static SessionFactory sessionFactory = null;
	private static UserDAO userService = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Configuration cfg = new Configuration().configure("test_hibernate.cfg.xml");
		sessionFactory = cfg.buildSessionFactory();
		userService = new UserService(sessionFactory);
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
	private static void deleteAllUsers() {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Query<Integer> query = session.createQuery("DELETE FROM User");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static User createExampleUser(String name, Role role) {
		User user = new User();
		user.setEmail(name + "@gmail.com");
		user.setRole(role);
		user.setFirstName(name + "_name");
		user.setLastName(name + "_lastName");
		user.setLogin(name + "_login");
		user.setPassword(name + "_password");
		user.setPhone(name + "_phone");
		user.setTitle(name + "_title");
		
		return user;
	}

}
