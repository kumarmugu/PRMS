/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.dao;

import java.sql.SQLException;
import java.util.List;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;

/**
 *
 * @author THIRILWIN
 */
public interface ProducerDAO {
     
    public abstract Producer createValueObject();
    public abstract Producer getObject(String id)
			throws NotFoundException, SQLException;
    public abstract List<Producer> loadAll() throws SQLException;
    public abstract List<Producer> findProducer(String producerName) throws SQLException;
    
}
