package main;

import java.util.Set;

import dbUtil.DBException;
import dbUtil.DBService;
import dbUtil.dao.DivisionDAO;
import dbUtil.dao.UserDAO;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;
import dbUtil.service.DivisionService;
import dbUtil.service.UserService;

public class Main {

	public static void main(String[] args) throws DBException {
		DBService db = new DBService();
		UserDAO userDAO = new UserService(db.getSessionFactory());
		DivisionDAO divDAO = new DivisionService(db.getSessionFactory());
		
		Division kmem = getDivision("KMEM");
		Division dmem = getDivision("DMEM");
		Division svem = getDivision("SVEM");
		Division kmemRES_1 = getDivision("KMEM_RES_1");
		Division kmemRES_2 = getDivision("KMEM_RES_2");
		Division kmemRES_3 = getDivision("KMEM_RES_3");
		
		
		System.out.println("-------------------Add divisions----------------------------");
		divDAO.add(kmem);
		divDAO.add(dmem);
		divDAO.add(svem);
		divDAO.add(kmemRES_1);
		divDAO.add(kmemRES_2);
		divDAO.add(kmemRES_3);
		
		
		kmemRES_1.setMasterDivision(kmem);
		kmemRES_2.setMasterDivision(kmem);
		kmemRES_3.setMasterDivision(kmem);
		
		divDAO.update(kmemRES_1);
		divDAO.update(kmemRES_2);
		divDAO.update(kmemRES_3);
		
		Set<Division> set = divDAO.getById(kmem.getId()).getSlaveDivisions();
		
		set.forEach(div->System.out.println(div.getName()));
		
		
		
		User admin = getUser("admin", Role.ADMIN);
		User userOne = getUser("userOne", Role.USER);
		User userTwo = getUser("userTwo", Role.USER);
		
		userOne.addDivision(kmem);
		userOne.addDivision(svem);
		userOne.addDivision(dmem);
		userTwo.addDivision(svem);
		admin.addDivision(dmem);
		admin.addDivision(kmem);
		userTwo.addDivision(kmemRES_1);
		
		
		System.out.println("-------------------Add users----------------------------");
		userDAO.add(admin);
		userDAO.add(userOne);
		userDAO.add(userTwo);
		
		System.out.println("-------------------Read users----------------------------");
		
		System.out.println(userDAO.getById(admin.getId()));
		admin.getDivisions().remove(kmem);
		System.out.println(userDAO.update(admin));
		System.out.println(userDAO.getById(admin.getId()));
		System.out.println(userDAO.addDivision(admin.getLogin(), kmem));
		System.out.println(userDAO.getById(admin.getId()));
		System.out.println(divDAO.delete(kmem));
		System.out.println(userDAO.deleteById(2));

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
