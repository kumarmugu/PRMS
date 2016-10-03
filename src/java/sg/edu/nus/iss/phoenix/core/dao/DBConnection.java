/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.core.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mugunthan, Zehua
 */
public class DBConnection {

    private Connection myConnection = null;

    /**
     * Open DB connection when it is close or null
     * 
     */
    private void openConnection() throws SQLException {
        if (myConnection != null && !myConnection.isClosed()) {
            return;
        }

        try {
            Class.forName(DBConstants.COM_MYSQL_JDBC_DRIVER);
            myConnection = DriverManager.getConnection(DBConstants.dbUrl,
                    DBConstants.dbUserName, DBConstants.dbPassword);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Unknown SQL Connection class.");
        }
    }
    /**
     * Get SQL prepare statement object for the connection
     * @param sql - SQL query in string
     * @return SQL prepare statement object
     * @throws SQLException upon SQL error
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        return myConnection.prepareStatement(sql);
    }

    /**
     * Set auto commit mode for this connection
     * @param autoCommit - set auto commit mode
     * @throws SQLException upon SQL error
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        myConnection.setAutoCommit(autoCommit);
    }

    /**
     * get current auto commit mode
     * @return auto commit mode
     * @throws SQLException upon SQL error
     */
    public boolean isAutoCommit() throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        return myConnection.getAutoCommit();
    }

    /**
     * Commit changes, this only required when it is not in auto commit mode
     * @throws SQLException upon SQL error
     */
    public void commit() throws SQLException {
        if (myConnection != null && !myConnection.isClosed()) {
            myConnection.commit();
        }
    }
    /**
     * Roll back the changes(before commit), only work in non-auto commit mode
     * @throws SQLException upon SQL error
     */
    public void rollback() throws SQLException {
        if (myConnection != null && !myConnection.isClosed()) {
            myConnection.rollback();
        }
    }

    /**
     * Close current SQL connection
     *
     */
    public void closeConnection() {
        try {
            myConnection.close();
            myConnection = null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
