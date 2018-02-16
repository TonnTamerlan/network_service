package dbUtil;

public class DBException extends Exception {
	
	private static final long serialVersionUID = 1892107180801376600L;

	public DBException(String message) {
		super(message);
	}
	
	public DBException(String message, Exception e) {
		super(message, e);
	}

}
