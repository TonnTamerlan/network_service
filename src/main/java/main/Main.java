package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dbUtil.DBException;
import dbUtil.dataSets.Division;
import dbUtil.dataSets.Role;
import dbUtil.dataSets.User;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);
	
	
	public static void main(String[] args) throws DBException {
		//DOMConfigurator.configure("src\\main\\resources\\log4j2.xml");//d:\Git\network_service\src\main\resources\hibernate.cfg.xml
		//BasicConfigurator.configure();
		logger.error("Application is started");
		
	}

	private static User createExampleUser(String login, Role role) {
		User user = new User(login, login + "_password", role);
		user.setEmail(login + "@gmail.com");
		user.setFirstName(login + "_name");
		user.setLastName(login + "_lastName");
		user.setPhone(login + "_phone");
		user.setTitle(login + "_title");
		
		return user;
	}
	
	static Division createExampleDivision(String name) {
		String adress = name + "_adress";
		Division div = new Division(name, adress);
		div.setName(name);
		div.setPhone(name + "_phone");
		
		return div;
	}

}
