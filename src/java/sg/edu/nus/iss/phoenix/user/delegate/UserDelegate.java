/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.delegate;

import java.util.ArrayList;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
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

    
    public ArrayList<User> processLoadAllUser(){
        return usrservice.getAllUser();
    }
    
     
    public ArrayList<User> processFindUser(String userName){
        return usrservice.processFindUser(userName);
    }
    
    public void processCreateUser(User user){
        usrservice.processCreateUser(user);
    }
    
     public void processModifyUser(User user){
         usrservice.processsModifyUser(user);
    }
     
    public void processDeleteUser(User user){
        usrservice.processDeletUser(user);
    }
      
}
