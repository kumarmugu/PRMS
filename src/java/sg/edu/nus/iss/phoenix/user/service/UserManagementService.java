/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.service;

import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.authenticate.entity.Role;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.core.exceptions.UserProgramConstraintsException;

/**
 *
 * @author misitesawn
 */
public class UserManagementService {
    DAOFactoryImpl factory;
    UserDao usrdao;
    RoleDao roledao;
    

   
    /**
     * 
     */
    public UserManagementService() {
          super();
		// TODO Auto-generated constructor stub
                factory = new DAOFactoryImpl();
		usrdao = factory.getUserDAO();
		roledao = factory.getRoleDAO();
    }
    /**
     * This method will Load all the User Roles
     * @return ArrayList<Role>
     */
    public ArrayList<Role> processLoadAllRoles(){
        ArrayList<Role> roles = new ArrayList<>();
        try {
            roles = (ArrayList<Role>) roledao.loadAll();
        } catch (SQLException ex) {
            Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return roles;
    }
    
    /**
     * 
     * @return
     * @throws NotFoundException 
     */
    public ArrayList<User> processLoadAllUser() throws NotFoundException{
        ArrayList<User> alluser = new ArrayList<>();
        try {
            alluser  = (ArrayList<User>) usrdao.loadAll();
            for(User user:alluser){
                for(Role role:user.getRoles()){
                    role.setAccessPrivilege(roledao.getObject(role.getRole()).getAccessPrivilege());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return alluser;
    }

    public void processsModifyUser(User user) throws NotFoundException, SQLException, UserProgramConstraintsException {
        System.out.println("porcessmodifyuser");
        boolean isRoleContainsPresenter = false;
        boolean isRoleContainsProcedure = false;
        List<Role> userRoles = new ArrayList<>();
        
        if ( user.getRoles() != null ){
            userRoles = user.getRoles();
                   
        }
        // check if user roles in clude procedure/presenter
        if ( userRoles.size() >= 1){
            for (Role role : userRoles) {
                if(role.getRole().equalsIgnoreCase("presenter") ){
                    isRoleContainsPresenter = true;
                }else if(role.getRole().equalsIgnoreCase("producer")){
                    isRoleContainsProcedure = true;
            }
            
        }
        
        if(! isRoleContainsProcedure ){
             
            if (usrdao.isUserAssignedAsProcedure(user.getId())){
                    throw new UserProgramConstraintsException("User is assigned in shceduled program as Procedure!");
                }
        }
        if (!isRoleContainsPresenter){
                  if (usrdao.isUserAssignedAsPresenter(user.getId())){
                    throw new UserProgramConstraintsException("User is assigned in shceduled program as Presenter!");
                }
        }
        
       
        usrdao.save(user,false);
        
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    }
    /**
     *
     * @param userId
     * @throws NotFoundException
     */
    public void processDeletUser(String userId)throws NotFoundException, SQLException, UserProgramConstraintsException {
        User user;
        user = new User(userId);
        if ( usrdao.isUserDeletable(userId) != true){
            throw new UserProgramConstraintsException("User is assigned in scheduled program!");
        }
        usrdao.delete(user);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void processCreateUser(User user) throws SQLException{
        try {
            usrdao.create(user);
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (SQLException ex) {
            
            Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex.getMessage());
        }
    }

    public ArrayList<User> processFindUser(String userId) throws SQLException {
        ArrayList<User> users  = new ArrayList();
        User user = new User(userId);
        if(  user.getId().equalsIgnoreCase("superuser")){
            try {
                List<Role> roles =   roledao.loadAll();
                user.setRoles((ArrayList<Role>) roles);
            } catch (SQLException ex) {
                Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
            }
            users.add(user);
            return users;
            
        }
        
        users = (ArrayList<User>) usrdao.searchById(userId);
        return users;
        
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
    
}
