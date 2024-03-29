/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.core.exceptions.UserProgramConstraintsException;
import sg.edu.nus.iss.phoenix.user.delegate.UserDelegate;

/**
 *
 * @author misitesawn
 */
@Action("deleteuser")
public class DeleteUserCmd implements Perform{
    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserDelegate del = new UserDelegate();
        String id = req.getParameter("id");
        
        try {
            del.processDeleteUser(id);
        } catch (UserProgramConstraintsException ex) {
            req.setAttribute("deleteErrMsg", "User:'" + id + "' can not delete.User is assigned in scheduled program!");
            Logger.getLogger(DeleteUserCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        List<User> data = new ArrayList<>()  ;
        try {
            data = del.processLoadAllUser();
        } catch (NotFoundException ex) {
            Logger.getLogger(DeleteUserCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("deleteErrMsg", "User cannot be deleted!");
            req.setAttribute("users", data);
            return "/pages/cruduser.jsp";
           
        }
        
        req.setAttribute("users", data);
        return "/pages/cruduser.jsp";
    }
}
