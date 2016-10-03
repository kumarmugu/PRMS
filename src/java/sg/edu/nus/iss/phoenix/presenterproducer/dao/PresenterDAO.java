/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.dao;

import java.sql.SQLException;
import java.util.List;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;

/**
 *
 * @author THIRILWIN
 */
public interface PresenterDAO {
    
    /**
    * createValueObject-method. This method is used when the Dao class needs
    * to create new value object instance. The reason why this method exists
    * is that sometimes the programmer may want to extend also the valueObject
    * and then this method can be overwrite to return extended valueObject.
    * NOTE: If you extend the valueObject class, make sure to override the
    * clone() method in it!
    * @return Presenter Return the presenter object
    */
    public abstract Presenter createValueObject();
    /**
	 * getObject-method. This will create and load valueObject contents from database 
	 * using given Primary-Key as identifier. This method is just a convenience method 
	 * for the real load-method which accepts the valueObject as a parameter. Returned
	 * valueObject will be created using the createValueObject() method.
     * @param id PresenterID
     * @return Presenter Return the presenter object
     * @throws sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException Exception will return when presenter not found in database
     * @throws java.sql.SQLException Exception will return for SQLException
	 */
    public abstract Presenter getObject(String id)
			throws NotFoundException, SQLException;
    /**
	 * loadAll-method. This will read all contents from database table and
	 * build a List containing valueObjects. Please note, that this method
	 * will consume huge amounts of resources if table has lot's of rows. 
	 * This should only be used when target tables have only small amounts
	 * of data.
     * @return List<Presenter>  Return list of presenters having presenter roles from user table.
     * @throws java.sql.SQLException Exception will return for SQLException
     */
    public abstract List<Presenter> loadAll() throws SQLException;
    
    /**
	 * findPresenter-method. This will read all contents from database table based on the @param value and
	 * build a List containing valueObjects. Please note, that this method
	 * will consume huge amounts of resources if table has lot's of rows. 
	 * This should only be used when target tables have only small amounts
	 * of data.
	 *
     * @param presenterName  This parameter contains presenterName to retrieve contents from database table.
     * @return List<Presenter> List of presenters having presenter roles from user table based on the presenterName from @param
     * @throws java.sql.SQLException Exception will return for SQLException
     */
    public abstract List<Presenter> findPresenter(String presenterName) throws SQLException;
}
