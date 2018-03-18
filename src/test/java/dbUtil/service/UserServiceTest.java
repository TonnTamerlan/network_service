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
