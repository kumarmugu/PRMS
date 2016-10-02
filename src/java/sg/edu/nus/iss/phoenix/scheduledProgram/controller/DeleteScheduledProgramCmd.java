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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.core.exceptions.ScheduledProgramNotDeletableException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

/**
 *
 * @author THIRILWIN
 */
@Action("deletesp")
public class DeleteScheduledProgramCmd implements Perform{
       
    @Override
    public String perform(String string, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ScheduledProgramDelegate spDelegate = new ScheduledProgramDelegate();
        String msg = "",  mode = "delete";
        ProgramSlot delProgramSlot = null;
        try {
            delProgramSlot = spDelegate.getProgramSlot(req);
            try {
                spDelegate.processDelete(delProgramSlot);
                msg = "Successfully deleted.";
                mode = "";
            } catch (NotFoundException | SQLException |ScheduledProgramNotDeletableException ex) {
                msg = "Error: " + ex.getMessage();
                Logger.getLogger(DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch (Exception ex) {
            msg = "Error: Fails to construct scheduled program object, due to " + ex.getMessage();
            Logger.getLogger(DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        WeeklySchedule ws ;
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        if (delProgramSlot == null) delProgramSlot = new ProgramSlot();
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", ws.getProgramSlots());
            req.setAttribute("default", delProgramSlot);
            req.setAttribute("startDate", ws.getStartDate());
            req.setAttribute("isAnnualScheduleExist", true);
            req.setAttribute("weekNo", ws.getWeekNo());
            req.setAttribute("currentYear", ws.getYear());
            req.setAttribute("mode", mode);
            req.setAttribute("msg", msg);
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(
                    DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
        } catch (SQLException ex) {
            Logger.getLogger(DeleteScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/pages/crudsp.jsp";
    }
}
