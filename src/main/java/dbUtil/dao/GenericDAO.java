package dbUtil.dao;

import dbUtil.dataSets.Model;


/**
 * Interface for working with CRUD operations
 * 
 * @author Alexey Kopylov
 *
 * @param <T> is the dataset
 * 
 * @version 1.0
 */
public interface GenericDAO <T extends Model> {
	/** Save object newInstance in data base
	 * 
	 * @param newInstance is a dataset
	 * 
	 * @return a primary key of added field in data base
	 * */
    long create(T newInstance);
 
    /** 
     * Find object by id and read it
     * 
     * @param id is the primary key of an entity
     * 
     * @return The object which has the primary key equals id or null
     * 
     */
    T read(long id);
    
    /**
     * Save changes that was made in the object
     * 
     * @param transientObject is the object which was changed
     * 
     */
    void update(T transientObject);
 
    /**
     * Delete the object
     * 
     * @param persistentObject is the object which will be deleted 
     *
     */
    void delete(T persistentObject);
    
    
}
