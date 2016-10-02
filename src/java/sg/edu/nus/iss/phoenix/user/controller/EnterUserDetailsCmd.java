/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.authenticate.entity.Role;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.radioprogram.delegate.ProgramDelegate;
import sg.edu.nus.iss.phoenix.radioprogram.delegate.ReviewSelectProgramDelegate;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.user.delegate.UserDelegate;

/**
 *
 * @author misitesawn
 */
@Action("enteruser")
public class EnterUserDetailsCmd implements Perform {
     @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserDelegate del = new UserDelegate();
        User usr = new User();
        ArrayList<Role> userRoles = new ArrayList<Role>();
        
        String strUserRoles = "";
        ArrayList<Role> roles = new ArrayList<>();
       
        /*
        ArrayList<User> users = new ArrayList<>();
        
         try {
             users = del.processFindUser("superUser");
         } catch (SQLException ex1) {
             Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex1);
         }

         for (User user : users) {
             if (user.getId().equalsIgnoreCase("superUser")) {
                 for (Role role : user.getRoles()) {
                     roles.add(role);
                 }
             }
         }

*/     
         try {
             roles  = del.processLoadAllRoles();
         } catch (SQLException ex) {
             Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex);
         }

        usr.setName(req.getParameter("name"));
        usr.setId(req.getParameter("id"));
        usr.setPassword(req.getParameter("password"));
        
        if ( usr.getName().equalsIgnoreCase("") || usr.getPassword().equalsIgnoreCase("") 
                || usr.getId().equalsIgnoreCase("")){
            req.setAttribute("errMsg", "User Name, User Id and Password cannot be empty ");
            //set the page attributes again      
            req.setAttribute("listUserRole", strUserRoles);
            req.setAttribute("roles", roles);
            req.setAttribute("name", usr.getName());
            req.setAttribute("id", usr.getId());
            req.setAttribute("insert", req.getParameter("insert"));
            return "/pages/setupuser.jsp";
        }
        
        String[] arrRoles = req.getParameterValues("roleName") ;
       if( arrRoles != null)
       {
           for (int i = 0; i< arrRoles.length ;i ++){
                userRoles.add(new Role(arrRoles[i]));
                if (i <= 0) {
                     strUserRoles += ":";
                 }
                 strUserRoles += arrRoles[i];
           }
           usr.setRoles((ArrayList<Role>) userRoles);
       }   
       
       else {
           
            req.setAttribute("errMsg", "Please select user role.");
            //set the page attributes again      
            req.setAttribute("listUserRole", strUserRoles);
            req.setAttribute("roles", roles);
            req.setAttribute("name", usr.getName());
            req.setAttribute("id", usr.getId());
            req.setAttribute("insert", req.getParameter("insert"));
            return "/pages/setupuser.jsp";
       }
     
       
        String insert = (String) req.getParameter("insert");
        Logger.getLogger(getClass().getName()).log(Level.INFO,
                        "Insert Flag: " + insert);
       
        if (insert.equalsIgnoreCase("true")) {
            try {
                del.processCreateUser(usr);
            } catch (SQLException ex) {
                Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex);
                
                //set error message 
                req.setAttribute("errMsg", "User Cannot insert because id already exists");
                //set the page attributes again
                
                req.setAttribute("listUserRole", strUserRoles);
                req.setAttribute("roles", roles);
                req.setAttribute("name", usr.getName());
                req.setAttribute("id", usr.getId());
                req.setAttribute("insert", req.getParameter("insert"));
                return "/pages/setupuser.jsp";
                }
            
        } else {
            try {
                del.processModifyUser(usr);
            } catch (NotFoundException ex) {
                Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
      
        
        List<User> data= new ArrayList<User>();
         try {
             data = del.processLoadAllUser();
         } catch (NotFoundException ex) {
             Logger.getLogger(EnterUserDetailsCmd.class.getName()).log(Level.SEVERE, null, ex);
         }
        req.setAttribute("users", data);
        return "/pages/cruduser.jsp";
    }
}
