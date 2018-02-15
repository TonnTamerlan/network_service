package main;

import org.hibernate.Session;

import dbUtil.DBService;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.User;

public class Main {

	public static void main(String[] args) {
		DBService db = new DBService();
		Session ses = db.getSessionFactory().openSession();
		
		/*Division kmem = getDivision("KMEM");
		Division dmem = getDivision("DMEM");
		Division svem = getDivision("SVEM");
		
		ses.beginTransaction();
		System.out.println(divisionDAO.create(kmem));
		System.out.println(divisionDAO.create(dmem));
		System.out.println(divisionDAO.create(svem));
		ses.getTransaction().commit();
		
		System.out.println("-------------------Divisions added----------------------------");
		
		ses.beginTransaction();
		User admin = getUser("admin", divisionDAO.read(1), divisionDAO.read(2), divisionDAO.read(3));
		User user = getUser("user", divisionDAO.read(2), divisionDAO.read(3));
		ses.getTransaction().commit();
	
		ses.beginTransaction();
		System.out.println(userDAO.create(admin));
		System.out.println(userDAO.create(user));
		ses.getTransaction().commit();
		
		System.out.println("-------------------Users added----------------------------");
	
		ses.beginTransaction();
		System.out.println(userDAO.read(1));
		System.out.println(userDAO.read(2));
		ses.getTransaction().commit();
	
		System.out.println("-------------------Users readed----------------------------");
		ses.beginTransaction();
		Division tempDiv = divisionDAO.read(1);
		System.out.println(tempDiv);
		User tempUser = userDAO.read(1);
		System.out.println(tempUser.equals(tempDiv.getUsers().toArray(new User[0])[0]));
		tempDiv.addUser(tempUser);
		divisionDAO.update(tempDiv);
		ses.getTransaction().commit();*/
		
		ses.beginTransaction();
		ses.getTransaction().commit();
		
		
		System.out.println("-------------------Divisions readed----------------------------");
		
		
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

	private static Division getDivision(String name) {
		Division div = new Division();
		
		div.setAdress("adress_" + name);
		div.setName(name);
		div.setPhone("phone_" + name);
		return div;
	}

}
