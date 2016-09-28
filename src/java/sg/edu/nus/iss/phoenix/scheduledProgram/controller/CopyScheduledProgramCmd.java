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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.exceptions.AnnualSchedueNotExistException;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ReviewAndSelectScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.delegate.ScheduledProgramDelegate;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
/**
 *
 * @author Rong
 */
@Action("copysp")
public class CopyScheduledProgramCmd implements Perform {
    ScheduledProgramDelegate spDelegate = new ScheduledProgramDelegate();
            
    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        
        ProgramSlot newProgramSlot = null;  
        String msg = "", mode = "copy";
        if (validateFormat(req))
        {
            newProgramSlot = spDelegate.getProgramSlot(req);
            try {
                spDelegate.ProcessCopy(newProgramSlot);
                msg = "Successfully created.";
                mode = "";
            } catch (Exception ex) {
                msg = "Error: " + ex.getMessage();
                Logger.getLogger(ModifyScheduledProgramCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ReviewAndSelectScheduledProgramDelegate del = new ReviewAndSelectScheduledProgramDelegate();
        WeeklySchedule ws ;//= new WeeklySchedule();
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        try {
            ws = del.reviewSelectScheduledProgram(year, week);
            req.setAttribute("events", ws.getProgramSlots());
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
    
    private boolean validateFormat(HttpServletRequest req) {
        
        String programName = req.getParameter("program");
        String presenter = req.getParameter("presenterId");
        String producer = req.getParameter("producerId");
        
        Date startDate = getDate(req.getParameter("date"));
        Date startTime = getTime(req.getParameter("startTime"));
        Date endTime = getTime(req.getParameter("endTime"));
        
        Date startDateTime = ProgramSlot.AddDateTime(startDate, startTime);
        Date endDateTime = ProgramSlot.AddDateTime(startDate, endTime);
        //Date duration = new Date(endDateTime.getTime() - startDateTime.getTime());
        
        User user = (User) req.getSession().getAttribute("user");
        
        return programName != null && presenter != null && producer != null  && 
               startDateTime != null && endDateTime != null &&
               user != null;                
    }
    
    private Date getDate(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//SimpleDateFormat("dd/MM/yyyy"); 
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }
    
    private Date getTime(String timeString) {
        DateFormat df = new SimpleDateFormat("HH:mm"); 
        Date time = null;
        try {
            time = df.parse(timeString);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return time;
    }
}
