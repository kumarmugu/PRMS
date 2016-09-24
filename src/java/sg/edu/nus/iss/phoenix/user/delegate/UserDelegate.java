/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.delegate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.user.service.UserManagementService;

/**
 *
 * @author misitesawn
 */
public class UserDelegate {
    private UserManagementService usrservice;

    

    public UserDelegate() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       super();
       usrservice = new UserManagementService();
    }

    
    public ArrayList<User> processLoadAllUser() throws NotFoundException{
        return usrservice.processLoadAllUser();
    }
    
     
    public ArrayList<User> processFindUser(String userName) throws SQLException{
        return usrservice.processFindUser(userName);
    }
    
    public void processCreateUser(User user){
        usrservice.processCreateUser(user);
    }
    
     public void processModifyUser(User user){
         usrservice.processsModifyUser(user);
    }
     
    public void processDeleteUser(String userId){
        try {
            usrservice.processDeletUser(userId);
        } catch (NotFoundException ex) {
            Logger.getLogger(UserDelegate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserDelegate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
}
