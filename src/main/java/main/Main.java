package main;

import dbUtil.DBException;
import dbUtil.DBService;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;
import dbUtil.service.UserService;

public class Main {

	public static void main(String[] args) throws DBException {
		DBService db = new DBService();
		UserDAO userDAO = new UserService(db.getSessionFactory());
		
		Division kmem = getDivision("KMEM");
		Division dmem = getDivision("DMEM");
		Division svem = getDivision("SVEM");
		
		User admin = getUser("admin", Role.ADMIN);
		User user = getUser("user", Role.USER);
		
		user.addDivision(kmem);
		user.addDivision(svem);
		admin.addDivision(dmem);
		
		System.out.println("-------------------Add users----------------------------");
		userDAO.add(admin);
		//userDAO.add(admin);
		userDAO.add(user);
		
		System.out.println("-------------------Read users----------------------------");
		
		System.out.println(userDAO.getByRole(Role.ADMIN));
		//System.out.println(userDAO.getAllLogins());
		
		/*System.out.println("-------------------Users added----------------------------");
	
	
		
		
		System.out.println("-------------------Divisions readed----------------------------");
		*/
		// TODO Auto-generated method stub

	}

	private static User getUser(String name, Division ... divisions) {
		User user = new User();
		user.setEmail(name + "@gmail.com");
		
		for (Division div : divisions) {
			user.addDivision(div);
		}
		
		user.setFirstName("name_" +name);
		user.setLastName("lastName_" + name);
		user.setLogin("login_"+ name);
		user.setPassword("password_" + name);
		user.setPhone("phone_" + name);
		user.setTitle("title_" +name);
		
		return user;
	}

	private static User getUser(String name, Role role) {
		User user = new User();
		user.setEmail(name + "@gmail.com");
		user.setRole(role);
		user.setFirstName("name_" +name);
		user.setLastName("lastName_" + name);
		user.setLogin("login_"+ name);
		user.setPassword("password_" + name);
		user.setPhone("phone_" + name);
		user.setTitle("title_" +name);
		
		return user;
	}
	
	private static Division getDivision(String name) {
		Division div = new Division();
		
		div.setAdress("adress_" + name);
		div.setName(name);
		div.setPhone("phone_" + name);
		return div;
	}

}
