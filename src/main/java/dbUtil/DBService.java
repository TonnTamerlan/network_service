package dbUtil;

/**
 * Interface for working with Data Base
 * 
 * 
 * @author Alexey Kopylov
 *
 */
public interface DBService {

	public void stop();

	public boolean isWorked();

	public void start();
}
