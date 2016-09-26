/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.Role;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;

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

    public void processsModifyUser(User user) throws NotFoundException, SQLException {
        System.out.println("porcessmodifyuser");
        usrdao.save(user);
        
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param userId
     * @throws NotFoundException
     */
    public void processDeletUser(String userId)throws NotFoundException, SQLException  {
        User user;
        user = new User(userId);
        usrdao.delete(user);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void processCreateUser(User user) {
        try {
            usrdao.create(user);
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (SQLException ex) {
            Logger.getLogger(UserManagementService.class.getName()).log(Level.SEVERE, null, ex);
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
