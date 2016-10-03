/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sg.edu.nus.iss.phoenix.core.dao.DBConstants;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;

/**
 *
 * @author THIRILWIN
 */
/**
 * Producer Data Access Object (DAO). This class contains all database handling that
 * is needed to permanently store and retrieve Producer object instances.
 */
public class ProducerDAOImpl implements ProducerDAO {
         
       Connection connection;
    
        @Override
	public Producer createValueObject() {
		return new Producer();
	}
    
    @Override
    public  List<Producer> loadAll() throws SQLException{
        openConnection();
		String sql = "SELECT * FROM phoenix.`user` where role like '%producer%' ORDER BY `ID` ASC; ";
		List<Producer> searchResults = listQuery(connection
				.prepareStatement(sql));
		closeConnection();
		System.out.println("record size"+searchResults.size());
		return searchResults;
    }
    
    private void openConnection() {
		try {
			Class.forName(DBConstants.COM_MYSQL_JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			this.connection = DriverManager.getConnection(DBConstants.dbUrl,
					DBConstants.dbUserName, DBConstants.dbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    private void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
	 * databaseQuery-method. This method is a helper method for internal use. It
	 * will execute all database queries that will return multiple rows. The
	 * result set will be converted to the List of valueObjects. If no rows were
	 * found, an empty List will be returned.
	 * 
	 * @param stmt
	 *            This parameter contains the SQL statement to be executed.
     * @return ListOfProducer
     * @throws java.sql.SQLException
	 */
    private List<Producer> listQuery(PreparedStatement stmt) throws SQLException {
                 ArrayList<Producer> searchResults = new ArrayList<>();
		ResultSet result = null;
		openConnection();
		try {
			result = stmt.executeQuery();

			while (result.next()) {
				Producer temp = createValueObject();
                                temp.setId(result.getString("id"));
				temp.setName(result.getString("name"));
				
				searchResults.add(temp);
			}

		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
			closeConnection();
		}

		return (List<Producer>) searchResults;   
    }

    
       @Override
    public List<Producer> findProducer(String producerName) throws SQLException {
         openConnection();
		//String sql = "SELECT * FROM phoenix.`user` where role='producer' and name like '%" + producerName +"%' ORDER BY `ID` ASC;";
		// Zehua modified, to support finding producer, when this user has multiple roles
                //String sql = "SELECT * FROM phoenix.`user` where role like '%producer%' and name = '" + producerName +"' ORDER BY `ID` ASC;";
                // Thiri modified , to support List<Producer>
                String sql = "SELECT * FROM phoenix.`user` where role like '%producer%' and name like '%" + producerName +"%' ORDER BY `ID` ASC;";
		List<Producer> searchResults = listQuery(connection
				.prepareStatement(sql));
		closeConnection();
		System.out.println("record size"+searchResults.size());
		return searchResults;
    }

    @Override
    public Producer getObject(String id) throws NotFoundException, SQLException {
                Producer valueObject = createValueObject();
                valueObject.setId(id);
		//load(valueObject);
		return valueObject;
    }

}

