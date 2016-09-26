/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;

/**
 *
 * @author Mugunthan
 */
@Action("managesp")
public class ManageScheduledProgramCmd implements Perform {

    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        WeeklySchedule ws = new WeeklySchedule();
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", ws.getProgramSlots());
            req.setAttribute("default", new ProgramSlot());            
            req.setAttribute("startDate", ws.getStartDate());
            req.setAttribute("isAnnualScheduleExist", true);
            req.setAttribute("weekNo", ws.getWeekNo());
            req.setAttribute("currentYear", ws.getYear());
        } catch (AnnualSchedueNotExistException ex) {
            Logger.getLogger(ManageScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("isAnnualScheduleExist", false);
        }
        return "/pages/crudsp.jsp";
    }
}
