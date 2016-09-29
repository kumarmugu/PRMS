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
 * @author Mugunthan
 */
public class DBConnection {

    private Connection myConnection = null;

    /**
     *
     * @return
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

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        return myConnection.prepareStatement(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        myConnection.setAutoCommit(autoCommit);
    }

    public boolean isAutoCommit() throws SQLException {
        if (myConnection == null || myConnection.isClosed()) {
            openConnection();
        }
        return myConnection.getAutoCommit();
    }

    public void commit() throws SQLException {
        if (myConnection != null && !myConnection.isClosed()) {
            myConnection.commit();
        }
    }

    public void rollback() throws SQLException {
        if (myConnection != null && !myConnection.isClosed()) {
            myConnection.rollback();
        }
    }

    /**
     *
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
