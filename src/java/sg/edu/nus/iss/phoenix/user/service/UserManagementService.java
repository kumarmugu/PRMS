/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;

/**
 *
 * @author misitesawn
 */
public class UserManagementService {
    DAOFactoryImpl factory;
    UserDao usrdao;
    RoleDao roledao;

   

    public UserManagementService() {
          super();
		// TODO Auto-generated constructor stub
                factory = new DAOFactoryImpl();
		usrdao = factory.getUserDAO();
		roledao = factory.getRoleDAO();
    }
    
    
    public ArrayList<User> getAllUser(){
        ArrayList<User> alluser = new ArrayList<>();
        try {
            alluser  = (ArrayList<User>) usrdao.loadAll();
        } catch (SQLException ex) {
            Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return alluser;
    }
    
}
