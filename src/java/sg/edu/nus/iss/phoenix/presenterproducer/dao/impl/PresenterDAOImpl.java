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
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;

/**
 *
 * @author THIRILWIN
 */
public class PresenterDAOImpl implements PresenterDAO{
    Connection connection;
    
        @Override
	public Presenter createValueObject() {
		return new Presenter();
	}
    
        @Override
	public Presenter getObject(String id) throws NotFoundException, SQLException {

		Presenter valueObject = createValueObject();
		valueObject.setId(id);
		//load(valueObject);
		return valueObject;
	}
        
    @Override
    public  List<Presenter> loadAll() throws SQLException{
        openConnection();
		String sql = "SELECT * FROM phoenix.`user` where role like '%presenter%' ORDER BY `ID` ASC; ";
		List<Presenter> searchResults = listQuery(connection
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

    private List<Presenter> listQuery(PreparedStatement stmt) throws SQLException {
                 ArrayList<Presenter> searchResults = new ArrayList<>();
		ResultSet result = null;
		openConnection();
		try {
			result = stmt.executeQuery();

			while (result.next()) {
				Presenter temp = createValueObject();
                                temp.setId(result.getString("id"));
				temp.setName(result.getString("name"));
				temp.setDescription(result.getString("role"));
				//temp.setTypicalDuration(result.getTime("typicalDuration"));

				searchResults.add(temp);
			}

		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
			closeConnection();
		}

		return (List<Presenter>) searchResults;   
    }

    

    @Override
    public List<Presenter> findPresenter(String presenterName) throws SQLException {
         openConnection();
		//String sql = "SELECT * FROM phoenix.`user` where role='presenter' and name like '%" + presenterName +"%' ORDER BY `ID` ASC;";
                // Zehua modified, to support finding presenter, when this user has multiple roles
                // String sql = "SELECT * FROM phoenix.`user` where role like '%presenter%' and name = '" + presenterName +"' ORDER BY `ID` ASC;";
                // Thiri modifed again to supporting finding List of presenter
                String sql = "SELECT * FROM phoenix.`user` where role like '%presenter%' and name = '%" + presenterName +"%' ORDER BY `ID` ASC;";
		List<Presenter> searchResults = listQuery(connection
				.prepareStatement(sql));
		closeConnection();
		System.out.println("record size"+searchResults.size());
		return searchResults;
    }
}
