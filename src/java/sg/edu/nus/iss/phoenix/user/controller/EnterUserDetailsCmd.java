/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.user.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
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
        ArrayList<Role> roles = new ArrayList<Role>();
        //role hardcoded
        roles.add(new Role("manager"));
        usr.setName(req.getParameter("name"));
        usr.setId(req.getParameter("id"));
        usr.setRoles((ArrayList<Role>) roles);
        System.out.println(usr.toString());
        
        String ins = (String) req.getParameter("ins");
        Logger.getLogger(getClass().getName()).log(Level.INFO,
                        "Insert Flag: " + ins);
        if (ins.equalsIgnoreCase("true")) {
                del.processCreateUser(usr);
        } else {
                del.processModifyUser(usr);
        }
        
       // ReviewSelectProgramDelegate rsdel = new ReviewSelectProgramDelegate();
        //List<RadioProgram> data = rsdel.reviewSelectRadioProgram();
        
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
