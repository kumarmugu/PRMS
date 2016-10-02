package sg.edu.nus.iss.phoenix.radioprogram.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.authenticate.entity.Role;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.user.delegate.UserDelegate;

/**
 *
 * @author misitesawn
 */
@Action("createmodifyuser")
public class CreateModifyUserCmd implements Perform{

    /**
     *
     * @param path
     * @param req
     * @param resp
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserDelegate del = new UserDelegate();
        ArrayList<User> users = new ArrayList();
        List<Role> roles = new ArrayList();
        List<Role> userRoles = new ArrayList();
        List<String> listUserRole = new ArrayList<String>();
       
        if (req.getParameter("roles") != null) {
             listUserRole = Arrays.asList(req.getParameter("roles").split(":"));
            for (String strUserRole : listUserRole) {
                userRoles.add(new Role(strUserRole));
            }
        }
      
        try {

            roles = del.processLoadAllRoles();
        } catch (SQLException ex) {
            Logger.getLogger(CreateModifyUserCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      
        req.setAttribute("insert", req.getParameter("insert"));
        req.setAttribute("errMsg", "");
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("listUserRole", req.getParameter("roles"));
        req.setAttribute("roles", roles);
        
        return "/pages/setupuser.jsp";
    }
    
}
