/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
/**
 *
 * @author Rong
 */
@Action("modifysp")
public class ModifyScheduledProgramCmd implements Perform {
    ScheduledProgramDelegate spDelegate = new ScheduledProgramDelegate();
            
    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        
        ProgramSlot newProgramSlot = null;
        String msg = "", mode = "modify";
        long id = Long.parseLong(req.getParameter("scheduledProgramId"));
        try
        {
            newProgramSlot = spDelegate.getProgramSlot(req);
            try {            
            spDelegate.processModify(id, newProgramSlot);
            msg = "Successfully updated.";
            mode = "";
            } catch (Exception ex) {
                msg = "Error: " + ex.getMessage();
                Logger.getLogger(ModifyScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }catch (Exception ex) {
            msg = "Error: Fails to construct scheduled program object.";
            Logger.getLogger(ModifyScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        WeeklySchedule ws ;//= new WeeklySchedule();
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", ws.getProgramSlots());
            if ("modify".equals(mode))
                newProgramSlot.setStartTime(new Date(id));
            req.setAttribute("default", newProgramSlot);
            req.setAttribute("startDate", ws.getStartDate());
            req.setAttribute("isAnnualScheduleExist", true);
            req.setAttribute("weekNo", ws.getWeekNo());
            req.setAttribute("currentYear", ws.getYear());
            req.setAttribute("mode", mode);
            req.setAttribute("msg", msg);
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(
                    ManageScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
        } catch (SQLException ex) {
            Logger.getLogger(ModifyScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "/pages/crudsp.jsp";
    }
    
}
